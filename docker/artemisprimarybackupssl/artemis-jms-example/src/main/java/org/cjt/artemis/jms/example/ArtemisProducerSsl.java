package org.cjt.artemis.jms.example;

import static org.cjt.artemis.jms.example.ArtemisConstants.TRUST_STORE;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class ArtemisProducerSsl {

	public static void main(String[] args) throws Exception {
		ArtemisUtil.systemProperty(TRUST_STORE, "admin54321$");
		try (Connection connection = ArtemisUtil.getConnectionHa("admin", "admin",
				ArtemisUtil.getTransportConfiguration(61616, "primarybroker", true),
				ArtemisUtil.getTransportConfiguration(61617, "backupbroker", true));
				Session session = ArtemisUtil.getSession(connection, Session.AUTO_ACKNOWLEDGE);
				MessageProducer messageProducer = ArtemisUtil.getProducerQueue(session, "TestQueue");) {
			int nummessage = 0;
			while (nummessage < 100) {
				messageProducer.send(session.createTextMessage("Hi!!!!!!!!!!!!!!!!!" + nummessage));
				nummessage++;
				System.out.println("Writing Messages");
				Thread.sleep(4000);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
