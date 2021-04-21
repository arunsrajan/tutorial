package test.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

public class ArrivalDelay {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		SparkConf sparkconf = new SparkConf();
		
		sparkconf.set("spark.master", "local");
		
		sparkconf.setAppName("SparkAirlineApp");
		
		SparkContext sc = SparkContext.getOrCreate(sparkconf);

		JavaRDD<String> airlines = sc.textFile("hdfs://127.0.0.1:9000/airline/*", 1).toJavaRDD();
		
		JavaRDD<String[]> filterdataarrdelay= airlines.map(linetosplit -> linetosplit.split(","))
				.filter(linetosplit -> linetosplit[14] != null && !linetosplit[14].equals("NA")
						&& !linetosplit[14].equals("ArrDelay"));
				
		JavaPairRDD<String, Long> carrierarrvdelay = filterdataarrdelay.mapToPair(line -> new Tuple2<String,Long>(line[8], Long.parseLong(line[14]))).reduceByKey((a,b)->a+b);
		
		
		JavaRDD<String> carriers = sc.textFile("hdfs://127.0.0.1:9000/carriers/*", 1).toJavaRDD();

		JavaPairRDD<String, String> carriersinfo = carriers.map(linetosplit -> linetosplit.split(","))
				.filter(linetosplit -> linetosplit[0] != null && !linetosplit[0].equals("Code"))
				.mapToPair(line -> new Tuple2(line[0].substring(1, line[0].length() - 1),
						line[1].substring(1, line[1].length() - 1)));
		
		//JavaPairRDD<String, Long> numofrecordsarrvdelay = filterdataarrdelay
		//		.mapToPair(line -> new Tuple2<String,Long>(line[8], 1l)).reduceByKey((a,b)->a+b);
		
		//JavaPairRDD<String, Tuple2<Tuple2<Long, String>, Long>> combinearrvdelaynumrecords = carrierarrvdelay.join(carriersinfo).join(numofrecordsarrvdelay);
		JavaPairRDD<String, Tuple2<Long, String>> combinearrvdelay = carrierarrvdelay.join(carriersinfo);
		combinearrvdelay.coalesce(1).saveAsTextFile("hdfs://127.0.0.1:9000/airlinecarriers/arrdelay" + System.currentTimeMillis());
	}

}
