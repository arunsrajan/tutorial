package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class InvokeRestService extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("jetty://http://localhost:1091/invokeservice")
		.setHeader("CamelHttpQuery").simple("name=${header.name}")
		.circuitBreaker().to("http://localhost:8080/api/v1/user/getname?httpMethod=get&bridgeEndpoint=true")
		.log("${body} ${headers}")
		.onFallback().log("Invoke Rest Endpoint Failed")
		.endCircuitBreaker();

	}

}
