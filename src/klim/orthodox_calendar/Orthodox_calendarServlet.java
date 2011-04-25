package klim.orthodox_calendar;

import java.io.IOException;
import javax.servlet.http.*;
import javax.jdo.PersistenceManager;

import java.util.logging.Logger;
import klim.orthodox_calendar.Day;
import klim.orthodox_calendar.PMF;
import java.util.List;

@SuppressWarnings("serial")
public class Orthodox_calendarServlet extends HttpServlet {
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		Day d = new Day(new java.util.Date());
		PersistenceManager pm = PMF.get().getPersistenceManager();
	    try {
	        pm.makePersistent(d);
	    	/*
	    	String query = "select from " + Day.class.getName();
	        List<Day> days = (List<Day>) pm.newQuery(query).execute();
	        pm.deletePersistent(days.iterator().next());
	        */
	    } catch (Exception e) {
	    } finally {
	        pm.close();
	    }

	    resp.sendRedirect("/calendar.jsp");
	}
}
