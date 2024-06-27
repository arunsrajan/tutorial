package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
public class ExposeHttpService extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
        // and output using pretty print
        .dataFormatProperty("prettyPrint", "true")
        // setup context path and port number that servlet will use
        .contextPath("/").port(8080)
        // add swagger api-doc out of the box
        .apiContextPath("/api-doc")
            .apiProperty("api.title", "User API").apiProperty("api.version", "1.2.3")
            // and enable CORS
            .apiProperty("cors", "true");

    // this user REST service is json only
    rest("/user").description("User rest service")
        .consumes("application/json").produces("application/json")
        .get("/getname").description("Find username by name").outType(ExposeHttp.class)
            .param().name("name").type(RestParamType.query).description("The name of the user to get").dataType("String").endParam()
            .to("bean:exposeBean?method=processExposeHttp(${header.name})");
	}

}
