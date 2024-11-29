package org.clh.kafka.messaging;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class SimpleConsumer {

    public static void main(String[] args) {
    	Properties consumerProps = new Properties();
    	consumerProps.put("bootstrap.servers", "localhost:9092");
    	consumerProps.put("group.id", "my-consumer-group");
    	consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    	consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//    	consumerProps.put("security.protocol", "SASL_SSL");
//    	consumerProps.put("sasl.mechanism", "PLAIN");
//    	consumerProps.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"kafka\" password=\"kafka-password\";");

    	KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);

    	consumer.subscribe(Arrays.asList("test1"));
    	while (true) {
    	    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
    	    for (ConsumerRecord<String, String> record : records) {
    	        System.out.println("Consumed message: " + record.value());
    	    }
    	}

    }
}