package com.configjavatech.springbootexample;

import javax.jms.ConnectionFactory;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

@Component
public class HazelcastIdempotentRoute extends RouteBuilder {
	@Bean("cf1")
	@Scope("singleton")
	public ConnectionFactory cf1() throws Exception {
        ActiveMQConnectionFactory jmsConnectionFactory = ActiveMQJMSClient.createConnectionFactory("tcp://localhost:61616", "cf1");
        jmsConnectionFactory.setUser("artemis");
        jmsConnectionFactory.setPassword("artemis");
        return jmsConnectionFactory;
	}
	@Override
	public void configure() throws Exception {		
		com.hazelcast.client.config.ClientConfig config = new com.hazelcast.client.config.ClientConfig();
		config.setInstanceName("HzInstance");
		config.getNetworkConfig().addAddress("192.168.154.5:5701");
		HazelcastInstance hzlcast = HazelcastClient.getOrCreateHazelcastClient(config);
		HazelcastIdempotentRepository idempotent =new HazelcastIdempotentRepository(hzlcast, "idempotentmessages");
		from("activemq:queue:queue.lb?connectionFactory=#cf1&concurrentConsumers=4&maxConcurrentConsumers=4")
        .idempotentConsumer(body(), idempotent)
        .log("${body}");
	}

}
