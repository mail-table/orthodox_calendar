package klim.services;

import klim.orthodox_calendar.Day;
import klim.orthodox_calendar.PMF;
import klim.services.Pocket;

import javax.servlet.http.HttpServlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;


public class PocketServlet extends HttpServlet {
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Pocket p = new Pocket("klim.iv@gmail.com");
		p.sendEmail();
	    resp.sendRedirect("/index.jsp");
	}
}
