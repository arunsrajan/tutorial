package org.cjt.artemis.jms.example;

import static org.cjt.artemis.jms.example.ArtemisConstants.TRUST_STORE;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ArtemisConsumerSsl {
	public static void main(String[] args) throws Exception {
		ArtemisUtil.systemProperty(TRUST_STORE, "admin54321$");
		try (Connection connection = ArtemisUtil.getConnectionHa("admin", "admin",
				ArtemisUtil.getTransportConfiguration(61616, "primarybroker", true),
				ArtemisUtil.getTransportConfiguration(61617, "backupbroker", true));
				Session session = ArtemisUtil.getSession(connection, Session.AUTO_ACKNOWLEDGE);
				MessageConsumer messageConsumer = ArtemisUtil.getConsumerQueue(session, "TestQueue::TestQueue");) {
			int messageCount = 0;
			int totalMessageToPush = 100;
			while (messageCount++ < totalMessageToPush) {
				TextMessage textMessage = (TextMessage) messageConsumer.receive();
				System.out.println(textMessage.getText());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
