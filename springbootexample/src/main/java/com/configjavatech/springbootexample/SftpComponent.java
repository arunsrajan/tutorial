package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;

//@Component
public class SftpComponent extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("sftp://localhost:2222/upload?username=john&password=RAW(p@ssw0rd)&strictHostKeyChecking=no&move=.completed&moveFailed=.failed")
		.to("sftp://localhost:2223/download?username=tom&password=RAW(p@ssw0rd)&strictHostKeyChecking=no&disconnect=true");

	}

}
