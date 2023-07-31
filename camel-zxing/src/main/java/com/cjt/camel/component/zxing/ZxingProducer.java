package com.cjt.camel.component.zxing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class ZxingProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(ZxingProducer.class);
    private ZxingEndpoint endpoint;

    public ZxingProducer(ZxingEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
    	BitMatrix bitMatrix = null;
    	if(endpoint.getBarcodeFormat().equals(BarcodeFormat.EAN_13.name())) {
    		com.google.zxing.oned.EAN13Writer barcodeWriter = new com.google.zxing.oned.EAN13Writer();
    		bitMatrix = barcodeWriter.encode(exchange.getIn().getBody(String.class), BarcodeFormat.EAN_13, 300, 150);
    	} else if(endpoint.getBarcodeFormat().equals(BarcodeFormat.QR_CODE.name())){
    		com.google.zxing.qrcode.QRCodeWriter barcodeWriter = new com.google.zxing.qrcode.QRCodeWriter();
    	    bitMatrix = 
    	      barcodeWriter.encode(exchange.getIn().getBody(String.class), BarcodeFormat.QR_CODE, 200, 200);
    	}
    	BufferedImage bi = MatrixToImageWriter.toBufferedImage(bitMatrix);
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(bi, endpoint.getFormat(), baos);
    	exchange.getIn().setBody(baos.toByteArray());
    }

}
