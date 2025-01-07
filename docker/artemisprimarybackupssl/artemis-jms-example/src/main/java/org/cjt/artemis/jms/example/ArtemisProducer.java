package org.cjt.artemis.jms.example;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class ArtemisProducer {

	public static void main(String[] args) throws Exception {
		try (Connection connection = ArtemisUtil.getConnection("admin", "admin12345$", "tcp://127.0.0.1:50292");
				Session session = ArtemisUtil.getSession(connection, Session.AUTO_ACKNOWLEDGE);
				MessageProducer messageProducer = ArtemisUtil.getProducerQueue(session, "APP.JOBS3");) {
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
