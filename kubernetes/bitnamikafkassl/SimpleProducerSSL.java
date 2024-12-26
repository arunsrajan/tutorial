package org.clh.kafka.messaging;

//import util.props packages
import java.util.Properties;

//import KafkaProducer packages
import org.apache.kafka.clients.producer.KafkaProducer;
//import simple producer packages
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
//import ProducerRecord packages
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

//Create java class named "SimpleProducer"
//lb1 lb2 and lb3 in hosts file
public class SimpleProducerSSL {

	public static void main(String[] args) throws Exception {
//		System.setProperty("javax.net.ssl.trustStore", "kafka-truststore.jks");
//		System.setProperty("javax.net.ssl.trustStorePassword", "admin12345$");
//		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		// System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
		// System.setProperty("javax.net.ssl.keyStorePassword", "01EmHduDZx");

		String bootstrapServers = "kafka:9195";
		// Assign topicName to string variable
		String topicName = "test2";

		// create instance for props to access producer configs
		Properties props = new Properties();

		// Assign localhost id
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		// props.setProperty("ssl.endpoint.identification.algorithm", "");
		// props.put("enable.auto.commit", "true");
		props.put("security.protocol", "SSL");
		props.put("ssl.truststore.type", "JKS");
		props.put("ssl.truststore.location", "kafka.truststore.jks");
		props.put("ssl.truststore.password", "admin12345$");
		props.put("ssl.client.auth", "none");
		props.put("ssl.endpoint.identification.algorithm", "");
//		props.put("acks", "all");
//
//		// If the request fails, the producer can automatically retry,
//		props.put("retries", 10);
//
//		// Specify buffer size in config
//		props.put("batch.size", 1);
//
//		// Reduce the no of requests less than 0
//		props.put("linger.ms", 1);
//
//		// The buffer.memory controls the total amount of memory available to the
//		// producer for buffering.
//		props.put("buffer.memory", 200000000);
//      props.put("sasl.mechanism", "PLAIN");
//      props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"admin\";");		
		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		for (long i = 100; i < 200; i++) {
			producer.send(new ProducerRecord<String, String>(topicName, "Message sent successfully" + (i + 1)));
			producer.flush();
			System.out.println("Message sent successfully " + (i + 1));
		}
		producer.close();
		System.out.println("ALL Message sent successfully");
	}
}