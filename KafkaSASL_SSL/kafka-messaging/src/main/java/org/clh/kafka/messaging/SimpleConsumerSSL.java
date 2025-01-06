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
	   System.setProperty("javax.net.ssl.trustStore", "kafka.truststore.jks");
	   System.setProperty("javax.net.ssl.trustStorePassword", "admin12345$");
	   String bootstrapServers = "kafka:9093";
      if(args.length == 0){
         System.out.println("Enter topic name");
         return;
      }
      //Kafka consumer configuration settings
      String topicName = args[0].toString();
      Properties props = new Properties();
      
      props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
      props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
      props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
	  props.put("ssl.client.auth", "required");
	  props.put("security.protocol", "SSL");
	  props.put("group.id", "test");
      KafkaConsumer<String, String> consumer = new KafkaConsumer
         <String, String>(props);
      
      //Kafka Consumer subscribes list of topics here.
      consumer.subscribe(Arrays.asList(topicName));
      
      //print the topic name
      System.out.println("Subscribed to topic " + topicName);
      int i = 0;
      
      while (true) {
         ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(2));
         for (ConsumerRecord<String, String> record : records)
         
         // print the offset,key and value for the consumer records.
         System.out.printf("offset = %d, key = %s, value = %s\n", 
            record.offset(), record.key(), record.value());
         Thread.sleep(1000);
      }
   }
}