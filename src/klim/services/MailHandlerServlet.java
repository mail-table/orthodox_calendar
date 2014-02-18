package klim.services;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Properties; 

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session; 
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import klim.orthodox_calendar.PMF;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.utils.SystemProperty;

public class MailHandlerServlet extends HttpServlet {
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Properties props = new Properties(); 
        Session session = Session.getDefaultInstance(props, null);

		Query q = pm.newQuery("select from " + AdminEmail.class.getName());
		List<AdminEmail> results = (List<AdminEmail>) q.execute();

		if (results.size() > 0 && results.get(0).getEmail() != "empty.appspotmail.com") {
	        try {
				MimeMessage msg = new MimeMessage(session, req.getInputStream());
				InternetAddress a = new InternetAddress(results.get(0).getEmail());
				msg.setFrom(new InternetAddress(this.getClass().getName() + "@" + SystemProperty.applicationId.get() + ".appspotmail.com"));
				msg.setRecipient(Message.RecipientType.TO, a);
				Transport.send(msg);
	        }
	        catch (MessagingException e) {
				logger.warning(e.getMessage());
	        }
		} else {
			//	create empty email for edit it in AppEngine admin console
			AdminEmail e = new AdminEmail("empty.appspotmail.com");
			pm.makePersistent(e);
		}
	}
}
