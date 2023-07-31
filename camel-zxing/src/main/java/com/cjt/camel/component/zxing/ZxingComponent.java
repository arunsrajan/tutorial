package com.cjt.camel.component.zxing;

import java.util.Map;

import org.apache.camel.Endpoint;

import org.apache.camel.support.DefaultComponent;

@org.apache.camel.spi.annotations.Component("zxing")
public class ZxingComponent extends DefaultComponent {
    
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new ZxingEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
