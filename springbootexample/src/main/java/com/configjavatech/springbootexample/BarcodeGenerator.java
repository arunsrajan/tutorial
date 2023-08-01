package com.configjavatech.springbootexample;

import org.apache.camel.model.dataformat.BarcodeDataFormat;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import com.google.zxing.BarcodeFormat;
@Component
public class BarcodeGenerator extends RouteBuilder {
	BarcodeDataFormat qrcode = new BarcodeDataFormat();
	BarcodeDataFormat ean13code = new BarcodeDataFormat();
	@Override
	public void configure() throws Exception {
		qrcode.setBarcodeFormat(BarcodeFormat.QR_CODE.name());
		qrcode.setWidth("500");
		qrcode.setHeight("500");
		from("jetty://http://0.0.0.0:1090/qrcode")
		.setBody(header("barcodeText"))
		.marshal(qrcode);
		
		ean13code.setBarcodeFormat(BarcodeFormat.EAN_13.name());
		ean13code.setWidth("600");
		ean13code.setHeight("300");
		from("jetty://http://0.0.0.0:1090/1dean13")
		.setBody(header("barcodeText"))
		.marshal(ean13code);

	}

}
