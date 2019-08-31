package com.engine.spark;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public interface Featurization {

	public JavaRDD<String> getURL_Host(JavaSparkContext context);

	public JavaPairRDD<String, Tuple2<String, Integer>> modifyToTupleString_Integer(
			JavaRDD<String> cache);

	public JavaPairRDD<String, Iterable<Integer>> getSiteCountByHost(
			JavaPairRDD<String, Iterable<Tuple2<String, Integer>>> pairRDD);

	public void saveRDDToTextFile(
			JavaPairRDD<String, Iterable<Integer>> mapValues, String folderName);

}