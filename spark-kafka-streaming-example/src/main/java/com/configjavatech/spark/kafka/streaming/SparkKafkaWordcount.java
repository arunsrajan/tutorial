package com.configjavatech.spark.kafka.streaming;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.flume.FlumeUtils;
import org.apache.spark.streaming.flume.SparkFlumeEvent;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import scala.Tuple2;

public class SparkKafkaWordcount {

	public static void main(String[] args) throws Exception {
		SparkConf sparkConf = new SparkConf().setAppName("KafkaFlumeWordCount").setMaster("local[10]");
		JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, new Duration(2000));
		ssc.checkpoint("checkpoint");
		Map<String, Object> kafkaParams = new ConcurrentHashMap<>();
		kafkaParams.put("bootstrap.servers", "localhost:9092");
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", true);
		JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(ssc,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(Arrays.asList("topicspark","topicspark1"), kafkaParams));
		JavaReceiverInputDStream<SparkFlumeEvent> flumeStream =
			 	FlumeUtils.createStream(ssc, "127.0.0.1", 8093);
		JavaDStream<String> lineFlume = flumeStream.map(event->new String(event.event().getBody().array(), "UTF-8"));
		JavaDStream<String> wordsFlume = lineFlume.flatMap(value -> Arrays.asList(value.split(" ")).iterator());
		JavaPairDStream<String, Long> wordFlumepair = wordsFlume.mapToPair(value -> new Tuple2<String, Long>(value, 1L));
		
		JavaDStream<String> lines = stream.map(cr -> cr.value());
		JavaDStream<String> words = lines.flatMap(value -> Arrays.asList(value.split(" ")).iterator());
		JavaPairDStream<String, Long> wordCounts = words.mapToPair(value -> new Tuple2<String, Long>(value, 1L));
		
		wordFlumepair.union(wordCounts).reduceByKeyAndWindow((a,b)->(a+b), new Duration(40000),new Duration(40000)).repartition(1)
		.dstream().saveAsTextFiles("C:/DEVELOPMENT/sparkstreaming/transformeddata", "");
		ssc.start();
		ssc.awaitTermination();

	}

}
