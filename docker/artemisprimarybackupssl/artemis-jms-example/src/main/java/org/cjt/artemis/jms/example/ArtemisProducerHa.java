package org.cjt.artemis.jms.example;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class ArtemisProducerHa {

	public static void main(String[] args) throws Exception {
		try (Connection connection = ArtemisUtil.getConnectionHa("admin", "admin",
				ArtemisUtil.getTransportConfiguration(51760, "localhost", false));
				Session session = ArtemisUtil.getSession(connection, Session.AUTO_ACKNOWLEDGE);
				MessageProducer messageProducer = ArtemisUtil.getProducerQueue(session, "orders1");) {
			int nummessage = 0;
			while (nummessage < 300) {
				messageProducer.send(session.createTextMessage("Hi!!!!!!!!!!!!!!!!!" + nummessage));
				nummessage++;
				System.out.println("Writing Messages");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
