package org.cjt.artemis.jms.example;

import static org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants.HOST_PROP_NAME;
import static org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants.PORT_PROP_NAME;
import static org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants.SSL_ENABLED_PROP_NAME;
import static org.cjt.artemis.jms.example.ArtemisConstants.NETTY_CONNECTOR_FACTORY;
import static org.cjt.artemis.jms.example.ArtemisConstants.TRUST_STORE_PASS;
import static org.cjt.artemis.jms.example.ArtemisConstants.TRUST_STORE_PATH;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

/**
 * A Jms Utility With SSL Support
 */
public class ArtemisUtil {

	/**
	 * sets the trust store system property
	 * @param artemisTrustStorePath
	 * @param trustStorePassword
	 */
	public static void systemProperty(String artemisTrustStorePath, String trustStorePassword) {
		System.setProperty(TRUST_STORE_PATH, artemisTrustStorePath);
        System.setProperty(TRUST_STORE_PASS, trustStorePassword);
	}
	
	/**
	 * Gets the transport configuration for given port, host and sslenabled
	 * @param port
	 * @param host
	 * @param sslEnabled
	 * @return transport configuration object 
	 */
	public static TransportConfiguration getTransportConfiguration(int port, String host, boolean sslEnabled) {
		Map<String, Object> connectionParams = new HashMap<String, Object>();

		connectionParams.put(PORT_PROP_NAME,
				port);
		connectionParams.put(HOST_PROP_NAME,
				host);
		connectionParams.put(
				SSL_ENABLED_PROP_NAME,
				sslEnabled);

		TransportConfiguration transportConfiguration = new TransportConfiguration(NETTY_CONNECTOR_FACTORY,
				connectionParams);
		return transportConfiguration;
	}
	/**
	 * Gets Connection Object
	 * @param username
	 * @param password
	 * @param transportConnection
	 * @return connection object
	 * @throws Exception
	 */
	public static Connection getConnectionHa(String username,String password, TransportConfiguration ...transportConnection) throws Exception {
		ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactoryWithHA(JMSFactoryType.CF, transportConnection);
		cf.setRetryInterval(1000);
		cf.setReconnectAttempts(1000);
		cf.setMaxRetryInterval(60000);
		cf.setRetryIntervalMultiplier(1.5);
		Connection conn = cf.createConnection(username,password);
		conn.setClientID(password);
		conn.start();
		return conn;
	}
	
	/**
	 * Get Connection Object
	 * @param username
	 * @param password
	 * @param hostwithport
	 * @return connection object
	 * @throws Exception
	 */
	public static Connection getConnection(String username,String password, String hostwithport) throws Exception {
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(hostwithport, username, password);
		cf.setRetryInterval(1000);
		cf.setReconnectAttempts(1000);
		cf.setMaxRetryInterval(60000);
		cf.setRetryIntervalMultiplier(1.5);
		Connection conn = cf.createConnection(username,password);
		conn.setClientID(password);
		conn.start();
		return conn;
	}
	
	/**
	 * Gets consumer for the session and queue name
	 * @param session
	 * @param queueName
	 * @return message consumer
	 * @throws Exception
	 */
	public static MessageConsumer getConsumerQueue(Session session, String queueName) throws Exception {
		Queue queue = session.createQueue(queueName);
		return session.createConsumer(queue);
	}
	
	/**
	 * Gets consumer for the session and queue name
	 * @param session
	 * @param queueName
	 * @return message consumer
	 * @throws Exception
	 */
	public static MessageConsumer getConsumerTopic(Session session, String queueName) throws Exception {
		Topic queue = session.createTopic(queueName);
		return session.createConsumer(queue);
	}
	
	/**
	 * Gets producer for the session and queue name
	 * @param session
	 * @param queueName
	 * @return message producer
	 * @throws Exception
	 */
	public static MessageProducer getProducerQueue(Session session, String queueName) throws Exception {
		Queue queue = session.createQueue(queueName);
		return session.createProducer(queue);
	}
	
	/**
	 * Gets producer for the session and queue name
	 * @param session
	 * @param queueName
	 * @return Producer
	 * @throws Exception
	 */
	public static MessageProducer getProducerTopic(Session session, String queueName) throws Exception {
		Topic queue = session.createTopic(queueName);
		return session.createProducer(queue);
	}
	
	
	/**
	 * Get session for the connection and acknowledgemode
	 * @param connection
	 * @param acknowledgeMode
	 * @return session
	 * @throws Exception
	 */
	public static Session getSession(Connection connection, int acknowledgeMode) throws Exception {
		Session session = connection.createSession(false, acknowledgeMode);
		return session;
	}
}
