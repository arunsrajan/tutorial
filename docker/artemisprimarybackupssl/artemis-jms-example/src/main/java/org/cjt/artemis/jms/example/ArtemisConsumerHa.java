package org.cjt.artemis.jms.example;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ArtemisConsumerHa {
	public static void main(String[] args) throws Exception {		
		try (Connection connection = ArtemisUtil.getConnectionHa("admin", "admin",
				ArtemisUtil.getTransportConfiguration(51760, "localhost", false));
				Session session = ArtemisUtil.getSession(connection, Session.AUTO_ACKNOWLEDGE);
				MessageConsumer messageConsumer = ArtemisUtil.getConsumerTopic(session, "TestQueueMulticast1::TestQueueMulticast1");) {
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
