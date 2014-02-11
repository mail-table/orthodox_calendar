package klim.services;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Pocket {
	private static String serviceURL = "";
	private static String serviceEmail;
	
	Pocket(String email) {
		serviceEmail = email;
	}

	public boolean sendEmail() {
		return this.sendEmail(Pocket.serviceEmail);
	}

	public boolean sendEmail(String addr) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom();
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(addr));
			msg.setText("TEST Тест");
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return true;
	}
}
