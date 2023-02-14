package com.configjavatech.spark.kafka.streaming;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KafkaFlumeDataGenerator extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer:kafka?period=3000")
		.setBody().constant("test data one")
		.to("kafka:topicspark?brokers=localhost:9092");
		
		from("timer:kafka?period=3000")
		.setBody().constant("test data three")
		.to("kafka:topicspark1?brokers=localhost:9092");
		
//		from("timer:netcat?period=300")
//		.setBody().constant("test data two\n")
//		.log("${body}")
//		.to("mina:tcp://127.0.0.1:8094?textline=true&sync=false");

	}

	@Bean
	public List encoders() {
		List encoders = new ArrayList<>();
		encoders.add(new io.netty.handler.codec.string.StringEncoder());
		return encoders;
	}
	
}
