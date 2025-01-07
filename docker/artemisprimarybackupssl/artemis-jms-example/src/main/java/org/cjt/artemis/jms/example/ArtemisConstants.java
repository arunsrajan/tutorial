package org.cjt.artemis.jms.example;

import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;

public class ArtemisConstants {
	public static final String TRUST_STORE_PATH = "javax.net.ssl.trustStore";
	public static final String TRUST_STORE_PASS = "javax.net.ssl.trustStorePassword";
	public static final String NETTY_CONNECTOR_FACTORY = NettyConnectorFactory.class.getName();
	public static final String TRUST_STORE = "artemis.truststore.jks";
}
