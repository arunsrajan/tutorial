package com.configjavatech.springbootexample;

import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mail.MailConstants;
import org.springframework.stereotype.Component;


//docker run --name mail -p 1025:1025 -p 8025:8025 -d mailhog/mailhog:latest
@Component
public class SendMail extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("jetty://http://0.0.0.0:1090/sendmail")
		.to("velocity:/orderemail.vm")
		.setBody(body().regexReplaceAll("\r", ""))
		.setHeader("mailContent").body()
		.to("pdf:create?pageSize=A4&font=Courier")
		.setHeader(MailConstants.MAIL_SUBJECT).header("Regarding your recent orders")
		.setHeader(MailConstants.MAIL_FROM).constant("noreply@cjt.com")
		.setHeader(MailConstants.MAIL_TO).constant("tom@testserver.com")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				AttachmentMessage in = exchange.getIn(AttachmentMessage.class);				
				org.apache.camel.attachment.DefaultAttachment att = new org.apache.camel.attachment.DefaultAttachment(new com.sun.istack.ByteArrayDataSource(exchange.getIn().getBody(byte[].class), "application/octet-stream"));
				att.addHeader("Content-Description", "Orders");
				Set<String> attachementNames = in.getAttachmentNames();
				if(attachementNames!=null) {attachementNames.stream().forEach(attachment->in.removeAttachment(attachment));}
				in.addAttachmentObject("Orders.pdf", att);
				in.setBody(exchange.getIn().getHeader("mailContent"));
			}
			
		})
		.to("smtp://localhost:1025")
		.setBody(simple("Mail Successfully Sent to tom@testserver.com"));

	}

}
