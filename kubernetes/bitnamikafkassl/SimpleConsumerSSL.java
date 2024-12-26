package org.clh.kafka.messaging;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class SimpleConsumerSSL {
	public static void main(String[] args) throws Exception {
		String bootstrapServers = "kafka:9195";
		// Assign topicName to string variable
		String topicName = "test2";

		// create instance for props to access producer configs
		Properties props = new Properties();

		props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		// props.put("ssl.client.auth", "required");
		props.put("security.protocol", "SSL");
		props.put("ssl.truststore.type", "JKS");
		props.put("ssl.truststore.location", "kafka.truststore.jks");
		props.put("ssl.truststore.password", "admin12345$");
		props.put("ssl.client.auth", "none");
		props.put("ssl.endpoint.identification.algorithm", "");

		// If the request fails, the producer can automatically retry,
		props.put("retries", 10);

		// Specify buffer size in config
		props.put("batch.size", 16384);

		// Reduce the no of requests less than 0
		props.put("linger.ms", 1);
		props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, topicName);
		props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		// Kafka Consumer subscribes list of topics here.
		consumer.subscribe(Arrays.asList(topicName));

		// print the topic name
		System.out.println("Subscribed to topic " + topicName);
		int i = 0;

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(2));
			for (ConsumerRecord<String, String> record : records) {
				// print the offset,key and value for the consumer records.
				System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
			}
			Thread.sleep(1000);
		}
	}
}