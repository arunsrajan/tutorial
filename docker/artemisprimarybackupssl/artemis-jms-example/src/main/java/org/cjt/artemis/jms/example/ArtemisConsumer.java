package org.cjt.artemis.jms.example;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ArtemisConsumer {
	public static void main(String[] args) throws Exception {		
		try (Connection connection = ArtemisUtil.getConnection("admin", "admin12345$",
				"tcp://127.0.0.1:51595");
				Session session = ArtemisUtil.getSession(connection, Session.AUTO_ACKNOWLEDGE);
				MessageConsumer messageConsumer = ArtemisUtil.getConsumerQueue(session, "orders1");) {
			int messageCount = 0;
			int totalMessageToConsume = 1200;
			while (messageCount++ < totalMessageToConsume) {
				TextMessage textMessage = (TextMessage) messageConsumer.receive();
				System.out.println(textMessage.getText());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
