package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;

//@Component
public class SftpComponentEncryption extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("sftp://john@localhost:2222/upload?privateKeyFile=C:/DEVELOPMENT/sftp/ssh_source&strictHostKeyChecking=no&move=.completed&moveFailed=.failed&preferredAuthentications=publickey")
		.to("sftp://tom@localhost:2223/download?privateKeyFile=C:/DEVELOPMENT/sftp/ssh_targe&strictHostKeyChecking=no&disconnect=true&preferredAuthentications=publickey");

	}

}
