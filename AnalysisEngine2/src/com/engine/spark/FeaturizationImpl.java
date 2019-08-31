package com.engine.spark;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import com.engine.hadoop.HDFSOperation;
import com.engine.hadoop.HDFSOperationsImpl;

import scala.Serializable;
import scala.Tuple2;

public class FeaturizationImpl implements Serializable, Featurization{
	final static Set<String> treeSet = new TreeSet<String>();
	final static Map<String, Integer> siteMap=new TreeMap<>();

	/* (non-Javadoc)
	 * @see com.engine.spark.Featurization#getURL_Host(org.apache.spark.api.java.JavaSparkContext)
	 */
	public JavaRDD<String> getURL_Host(JavaRDD<String> cache2){
		JavaRDD<String> cache = null;

		cache = cache2.map(new Function<String, String>() {
			public String call(String str) throws Exception {
				String[] tokens = str.split("\t");
				String url = tokens[4];
				Pattern p = Pattern.compile("/[a-zA-Z-1-9]*");
				Matcher m = p.matcher(url);
				int hash = 0;
				if (m.find()) {
					url = m.group();
				}
				return tokens[0] + " " + url;
			}
		});
		return cache;
	}


	/* (non-Javadoc)
	 * @see com.engine.spark.Featurization#modifyToTupleString_Integer(org.apache.spark.api.java.JavaRDD)
	 */
	public JavaPairRDD<String, Tuple2<String, Integer>> modifyToTupleString_Integer(JavaRDD<String> cache){
		JavaPairRDD<String, Tuple2<String, Integer>> javaPairRDD = cache
				.mapToPair(
						new PairFunction<String, String, Tuple2<String, Integer>>() {
							public Tuple2<String, Tuple2<String, Integer>> call(
									String rec) {
								String[] tokens = StringUtils.split(rec, " ");
								String host = tokens[0].trim();
								String url = tokens[1].trim();
								Tuple2<String, Integer> tuple = new Tuple2<String, Integer>(
										url, 0);
								return new Tuple2(host, tuple);
							}
						}).cache();

		return javaPairRDD;

	}

	/* (non-Javadoc)
	 * @see com.engine.spark.Featurization#getSiteCountByHost(org.apache.spark.api.java.JavaPairRDD)
	 */
	public JavaPairRDD<String, Iterable<Integer>> getSiteCountByHost(JavaPairRDD<String, Iterable<Tuple2<String, Integer>>> pairRDD ){
		JavaPairRDD<String, Iterable<Integer>> mapValues = pairRDD
				.mapValues(
						new Function<Iterable<Tuple2<String, Integer>>, Iterable<Integer>>() {
							public Iterable<Integer> call(
									Iterable<Tuple2<String, Integer>> itr)
											throws Exception {
								final Map<String, Integer> map = new HashMap<String, Integer>();
								for (Tuple2<String, Integer> tuple : itr) {
									if (map.containsKey(tuple._1)) {
										int i = map.get(tuple._1);
										map.put(tuple._1, ++i);


									} else {
										map.put(tuple._1, 1);
									}

									if(siteMap.containsKey(tuple._1)){
										int i = siteMap.get(tuple._1);
										siteMap.put(tuple._1, ++i);
									}else {
										siteMap.put(tuple._1, 1);
									}
								}
								List<Integer> returnSet = new ArrayList<Integer>();

								for (String string : treeSet) {
									if (map.containsKey(string)) {
										int value = map.get(string);
										returnSet.add(value);
									} else {
										returnSet.add(0);
									}
								}

								return returnSet;
							}
						}).cache();

		return mapValues;
	}

	/* (non-Javadoc)
	 * @see com.engine.spark.Featurization#saveRDDToTextFile(org.apache.spark.api.java.JavaPairRDD, java.lang.String)
	 */
	public void saveRDDToTextFile(JavaPairRDD<String, Iterable<Integer>> mapValues,String folderName){
		JavaRDD<String> javaRDD = mapValues.map(
				new Function<Tuple2<String, Iterable<Integer>>, String>() {
					public String call(Tuple2<String, Iterable<Integer>> tuple)
							throws Exception {
						StringBuffer buffer = new StringBuffer();
						buffer.append(tuple._1);
						buffer.append("#");
						for (Integer integer : tuple._2) {
							buffer.append(integer + ",");
						}

						String dataSet = buffer.toString();
						StringBuffer finalString = new StringBuffer();
						int size = dataSet.length();
						for (int i = 0; i < size; i++) {
							if (i == size - 1) {
								break;
							}
							finalString.append(dataSet.charAt(i));
						}
						return finalString.toString();
					}
				}).cache();
		List<String> featurizedData = javaRDD.collect();
		for (String dataset : featurizedData) {
			System.out.println(dataset);
		}
		javaRDD.saveAsTextFile("hdfs://rosspatil.domain.name:8020/NASA/"+folderName+"/featurized_dataset");


		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(siteMap);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		HDFSOperation hdfsOperation=new HDFSOperationsImpl("hdfs://rosspatil.domain.name:8020");
		hdfsOperation.makeDir(folderName+"/SiteVisited");
		hdfsOperation.insertFile( is, folderName+"/SiteVisited/sites.txt");
		System.out.println(siteMap);
	}

	public static void startFeaturizationProcess(String folderName, JavaSparkContext context) {



		JavaRDD<String> cache = context.textFile("hdfs://rosspatil.domain.name:8020/NASA/"+folderName+"/data/log.tsv")
				.cache();
		cache = new FeaturizationImpl().getURL_Host(cache);

		List<String> collect = cache.collect();
		for (String string : collect) {
			String[] tokens = string.split(" ");
			treeSet.add(tokens[1]);
		}

		JavaPairRDD<String, Tuple2<String, Integer>> javaPairRDD = new FeaturizationImpl().modifyToTupleString_Integer(cache);
		JavaPairRDD<String, Iterable<Tuple2<String, Integer>>> pairRDD = javaPairRDD
				.groupByKey();

		JavaPairRDD<String, Iterable<Integer>> mapValues = new FeaturizationImpl().getSiteCountByHost(pairRDD);

		new FeaturizationImpl().saveRDDToTextFile(mapValues, folderName);

	}

}
