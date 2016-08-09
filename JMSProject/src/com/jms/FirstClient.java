package com.jms;

import java.util.Properties;
import java.util.Scanner;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.JMSException;
import javax.jms.Destination;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class FirstClient {
	Context context = null;
	ConnectionFactory factory = null;
	Connection connection = null;
	Destination destination = null;
	Session session = null;
	MessageProducer producer = null;

	public FirstClient() {

	}

	public void sendMessage() {
		Properties initialProperties = new Properties();
		initialProperties.put(InitialContext.INITIAL_CONTEXT_FACTORY,
				"org.exolab.jms.jndi.InitialContextFactory");
		initialProperties.put(InitialContext.PROVIDER_URL,
				"tcp://localhost:3035");

		try {

			context = new InitialContext(initialProperties);
			factory = (ConnectionFactory) context.lookup("ConnectionFactory");
			destination = (Destination) context.lookup("queue10");
			connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			producer = session.createProducer(destination);
			connection.start();
			Scanner ss = new Scanner(System.in);
			while (true) {
			System.out.print("Write msg : ");
				String msg = ss.nextLine();
				TextMessage message = session.createTextMessage();
				message.setText(msg);
				producer.send(message);
				System.out.println("Sent: " + message.getText());
				if (msg.equals("exit")) {
					System.exit(0);
				}
			}
		} catch (JMSException ex) {
			ex.printStackTrace();
		} catch (NamingException ex) {
			ex.printStackTrace();
		}

		if (context != null) {
			try {
				context.close();
			} catch (NamingException ex) {
				ex.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		FirstClient firstClient = new FirstClient();
		firstClient.sendMessage();
	}

}