package org.clh.kafka.messaging;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

//Create java class named "SimpleProducer"
public class SimpleProducer {
   
   public static void main(String[] args) throws Exception{
      
      //Assign topicName to string variable
      String topicName = "test1";
      String bsconfig = "127.0.0.1:9194";
	  int start = 1;
              Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bsconfig);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
      
      //Set acknowledgements for producer requests.      
      properties.put("acks", "all");
      
      //If the request fails, the producer can automatically retry,
      properties.put("retries", 10);
      
      //Specify buffer size in config
      properties.put("batch.size", 16384);
      
      //Reduce the no of requests less than 0   
      properties.put("linger.ms", 1);
      
      //The buffer.memory controls the total amount of memory available to the producer for buffering.   
      properties.put("buffer.memory", 33554432);
      KafkaProducer<String, String> producer = new KafkaProducer
         <String, String>(properties);
            
      for(long i = start; i < start+100; i++){
         producer.send(new ProducerRecord<String, String>(topicName, null, "Message sent successfully"+i));
         producer.flush();
		 System.out.println("Message sent successfully");
	  }
      System.out.println("ALL Message sent successfully");
      producer.close();
   }
}