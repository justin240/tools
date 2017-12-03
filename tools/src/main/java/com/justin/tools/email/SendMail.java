package com.justin.tools.email;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	public static void main(String[] args) {
		
		String[] addressList = { "justin@localhost", "test@localhost", "shervin@localhost" };
		for (int i = 1; i <= 110; i++) {
			String addr = addressList[(i % addressList.length)];
			send(addr, i);
		}
	}

	private static void send(String fromId, int mailNumber) {
		try {
			// create properties field
			Properties props = new Properties();

			// Connect to the server
			Session session = Session.getDefaultInstance(props, null);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromId));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("libin@localhost"));
			message.setHeader("ActivityId", "" + new Random().nextInt());
			message.setSubject("Mail not sent (" + mailNumber + ")");
			message.setText("Hi, This mail is to inform you... Mail Number : " + mailNumber);

			Transport.send(message);

			System.out.println(mailNumber + " Message sent success !");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
