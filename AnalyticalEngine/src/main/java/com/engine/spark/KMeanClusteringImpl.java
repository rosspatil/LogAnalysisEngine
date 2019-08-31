package com.engine.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;

import scala.Tuple2;

public class KMeanClusteringImpl {

	public double squaredDistance(Vector a, Vector b) {
		double distance = 0.0;
		int size = a.size();
		for (int i = 0; i < size; i++) {
			double diff = a.apply(i) - b.apply(i);
			distance += diff * diff;
		}
		return distance;
	}

	public Vector buildVector(String features) {
		String[] tokens = StringUtils.split(features, ",");
		double[] d = new double[tokens.length];
		for (int i = 0; i < d.length; i++) {
			d[i] = Double.parseDouble(tokens[i]);
		}
		return new DenseVector(d);
	}

	public int closestPoint(Vector p, List<Vector> centers) {
		int bestIndex = 0;
		double closest = Double.POSITIVE_INFINITY;
		for (int i = 0; i < centers.size(); i++) {
			double tempDist = squaredDistance(p, centers.get(i));
			if (tempDist < closest) {
				closest = tempDist;
				bestIndex = i;
			}
		}
		return bestIndex;
	}

	public Vector average(List<Vector> list) {
		// find sum
		double[] sum = new double[list.get(0).size()];
		for (Vector v : list) {
			for (int i = 0; i < sum.length; i++) {
				sum[i] += v.apply(i);
			}
		}

		// find averages...
		int numOfVectors = list.size();
		for (int i = 0; i < sum.length; i++) {
			sum[i] = sum[i] / numOfVectors;
		}
		return new DenseVector(sum);
	}

	public Vector average(Iterable<Vector> ps) {
		List<Vector> list = new ArrayList<Vector>();
		for (Vector v : ps) {
			list.add(v);
		}
		return average(list);
	}

	public JavaPairRDD<String, Vector> getFeatureizedData(String featurizedData,
			JavaSparkContext context) {
		JavaPairRDD<String, Vector> data = context.textFile(featurizedData)
				.mapToPair(new PairFunction<String, String, Vector>() {
					public Tuple2<String, Vector> call(String in)
							throws Exception {
						// in:
						// <key><#><feature_1><,><feature_2><,>...<,><feature_24>
						String[] parts = StringUtils.split(in, "#");
						return new Tuple2<String, Vector>(parts[0],
								buildVector(parts[1]));
					}
				}).cache();
		return data;
	}

	public Map<Integer, Vector> getNewCentroids(
			JavaPairRDD<Integer, Iterable<Vector>> pointsGroup) {
		Map<Integer, Vector> newCentroids = pointsGroup.mapValues(
				new Function<Iterable<Vector>, Vector>() {
					public Vector call(Iterable<Vector> ps) throws Exception {
						return average(ps);
					}
				}).collectAsMap();
		return newCentroids;
	}

	public JavaPairRDD<Integer, Vector> getClosest(
			JavaPairRDD<String, Vector> data, final List<Vector> centroids) {
		@SuppressWarnings("serial")
		JavaPairRDD<Integer, Vector> closest = data
		.mapToPair(new PairFunction<Tuple2<String, Vector>, Integer, Vector>() {
			public Tuple2<Integer, Vector> call(
					Tuple2<String, Vector> in) throws Exception {
				return new Tuple2<Integer, Vector>(closestPoint(
						in._2(), centroids), in._2());
			}
		});
		return closest;
	}

	public List<Vector> getInitialCentroids(JavaPairRDD<String, Vector> data,
			final int K) {
		List<Tuple2<String, Vector>> centroidTuples = data.takeSample(false, K,
				42);
		final List<Vector> centroids = new ArrayList<Vector>();
		for (Tuple2<String, Vector> t : centroidTuples) {
			centroids.add(t._2());
		}
		return centroids;
	}

	public double getDistance(final List<Vector> centroids,
			final Map<Integer, Vector> newCentroids, final int K) {
		double distance = 0.0;
		for (int i = 0; i < K; i++) {
			distance += squaredDistance(centroids.get(i), newCentroids.get(i));
		}
		return distance;
	}
	
	
	public void clusteringResults(final List<Vector> centroids,JavaPairRDD<String, Vector> data,String folder) {
		for (int i = 0; i < centroids.size(); i++) {
			System.out
			.println("/***************************************************************  cluster "
					+ (1 + i)
					+ "*************************************************/");
			final int index = i;
			
			
			JavaPairRDD<String, Vector> filter = data.filter(new Function<Tuple2<String, Vector>, Boolean>() {
						public Boolean call(Tuple2<String, Vector> in)
								throws Exception {
							return closestPoint(in._2(), centroids) == index;
						}
					}).cache();
			
			
			List<Tuple2<String, Vector>> samples=filter.collect();
			
			Set<Integer> siteNos = new TreeSet<Integer>();
			for (Tuple2<String, Vector> sample : samples) {
//				System.out.println(sample._2());
				Vector vector = sample._2();
				String string = vector.toString();
				string = string.replace('[', ' ');
				string = string.replace(']', ' ');
				Scanner sc = new Scanner(string);
				sc.useDelimiter(",");
				int counter = 0;
				while (sc.hasNext()) {
					++counter;
					String sitevisit = sc.next();
					if (!(sitevisit.equals("0.0"))) {
						double visitNo = Double.parseDouble(sitevisit);
						if (visitNo > 5.0) {
							siteNos.add(counter);
						}
					}
				}
			}
			StringBuffer fileName=new StringBuffer();
			
			for (Integer integer : siteNos) {
				fileName.append(integer+"_");
			}
			filter.saveAsTextFile("hdfs://rosspatil.domain.name:8020/NASA/"+folder+"/Results/");
		}
		
	}

	public void performClustering(String dataDir,String folderName) throws Exception {

		SparkConf conf = new SparkConf();
		conf.setMaster("local[*]").setAppName("SparkSQL").setSparkHome("/usr/hdp/2.3.6.0-3796/spark");
		SparkContext sparkContext = new SparkContext(conf);
		JavaSparkContext context = new JavaSparkContext(sparkContext);
		final int K = 10;
		final double convergeDist = .000001;
		final String featurizedData = dataDir;
		//
		JavaPairRDD<String, Vector> data = getFeatureizedData(featurizedData, context);
		//
		final List<Vector> centroids = getInitialCentroids(data, K);
		//
		double tempDist = 1.0 + convergeDist;
		// make sure that for the first time (tempDist > convergeDist).
		while (tempDist > convergeDist) {
			JavaPairRDD<Integer, Vector> closest = getClosest(data, centroids);
			//
			JavaPairRDD<Integer, Iterable<Vector>> pointsGroup = closest
					.groupByKey();
			Map<Integer, Vector> newCentroids = getNewCentroids(pointsGroup);
			//
			tempDist = getDistance(centroids, newCentroids, K);
			//
			for (Map.Entry<Integer, Vector> t : newCentroids.entrySet()) {
				centroids.set(t.getKey(), t.getValue());
			}
		} // end-while
		
		clusteringResults(centroids, data,folderName);
		
		context.stop();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		KMeanClusteringImpl impl=new KMeanClusteringImpl();
		try {
			impl.performClustering("/home/roshan/SparkPractises/featurized", "table_1489985249760");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
