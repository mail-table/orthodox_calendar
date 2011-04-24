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

//		resp.setContentType("text/plain");
//		resp.getWriter().println("Hello, world");

		//Day d = new Day(new java.util.Date(2011,3,16));
		Day d = new Day(new java.util.Date());
		
		//logger.warning(d.getLink());
		//logger.warning(d.parseDay(new java.util.Date(2011,1,5)));
		
        System.out.println("-----1");
		PersistenceManager pm = PMF.get().getPersistenceManager();
	    try {
	        pm.makePersistent(d);
	    	/*
	    	String query = "select from " + Day.class.getName();
	        List<Day> days = (List<Day>) pm.newQuery(query).execute();
	        System.out.println("------>" + new Integer(days.size()).toString());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        pm.deletePersistent(days.iterator().next());
	        
	        System.out.println("------1");
	        */
	    } catch (Exception e) {
	    	System.out.println("0000 " + e.getMessage());
	    } finally {
	        System.out.println("------2");
	        pm.close();
	    }
		
	    resp.sendRedirect("/calendar.jsp");
	}
}
