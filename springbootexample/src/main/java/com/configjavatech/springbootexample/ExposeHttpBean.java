package com.configjavatech.springbootexample;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component("exposeBean")
public class ExposeHttpBean implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		ExposeHttp http = new ExposeHttp();
		http.setUsername((String) exchange.getIn().getHeader("name"));
		exchange.getIn().setBody(http);
	}
	
	public ExposeHttp processExposeHttp(String name) throws Exception {

		ExposeHttp http = new ExposeHttp();
		http.setUsername(name);
		return http;
	}

}
