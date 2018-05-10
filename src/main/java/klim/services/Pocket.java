package klim.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.utils.SystemProperty;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.Key;

public class Pocket {
	private static String serviceURL = "";
	private static String serviceEmail;

	public static String getServiceEmail() {
		return serviceEmail;
	}

	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


	Pocket(String email) {
		serviceEmail = email;
	}

	public boolean sendEmail(String subject, String message, String toAddr) {
		return this.sendEmail(subject, message, toAddr.trim().length() > 0 ? toAddr : Pocket.serviceEmail, this.getClass().getName() + "@" + SystemProperty.applicationId.get() + ".appspotmail.com");
	}

	public boolean sendEmail(String subject, String message) {
		return this.sendEmail(subject, message, Pocket.serviceEmail);
	}

	public boolean sendEmail(String subject, String message, String addr, String fromAddr) {
		List<PocketUrl> results = ofy().load().type(PocketUrl.class).list();

		//	save only new urls
		if (results.size() == 0) {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			try {
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(fromAddr));
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(addr));
				msg.setSubject(subject);
				msg.setText(message);
				Transport.send(msg);

				PocketUrl url = new PocketUrl(message);
				ofy().save().entity(url);
			} catch (AddressException e) {
				logger.warning(e.getMessage());
			} catch (MessagingException e) {
				logger.warning(e.getMessage());
			}
		}
		return true;
	}
}
