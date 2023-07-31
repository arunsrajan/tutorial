package com.cjt.camel.component.zxing;

import org.apache.camel.Category;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultEndpoint;

import com.google.zxing.BarcodeFormat;

import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

import java.util.concurrent.ExecutorService;

/**
 * Zxing component which does bla bla.
 *
 * TODO: Update one line description above what the component does.
 */
@UriEndpoint(firstVersion = "0.0.1-SNAPSHOT", scheme = "zxing", title = "Zxing", syntax="zxing:name",
             category = {Category.JAVA})
public class ZxingEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = true)
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;
    
    @UriParam
    private String barcodeFormat=BarcodeFormat.EAN_13.name();
    
    @UriParam
    private int width=500;
    
    @UriParam
    private int height=100;
    
    @UriParam
    private String format="png";


    public ZxingEndpoint() {
    }

    public ZxingEndpoint(String uri, ZxingComponent component) {
        super(uri, component);
    }

    public Producer createProducer() throws Exception {
        return new ZxingProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Consumer for zxing component is not available");
    }

    /**
     * Some description of this option, and what it does
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return option;
    }
    
    

    public String getBarcodeFormat() {
		return barcodeFormat;
	}

    /**
     * Sets the barcode format
     * @param barcodeFormat
     */
	public void setBarcodeFormat(String barcodeFormat) {
		this.barcodeFormat = barcodeFormat;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width of barcode.
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height of barcode
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	public String getFormat() {
		return format;
	}

	/**
	 * Sets the format of barcode
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	public ExecutorService createExecutor() {
        // TODO: Delete me when you implemented your custom component
        return getCamelContext().getExecutorServiceManager().newSingleThreadExecutor(this, "ZxingConsumer");
    }
}
