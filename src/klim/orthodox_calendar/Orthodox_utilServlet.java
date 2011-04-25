package klim.orthodox_calendar;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class Orthodox_utilServlet extends HttpServlet {
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		Day d = new Day(new java.util.Date());
		PersistenceManager pm = PMF.get().getPersistenceManager();
	    try {
	        pm.makePersistent(d);
	    	
	    	String query = "select from " + Day.class.getName();
	        List<Day> days = (List<Day>) pm.newQuery(query).execute();
	        pm.deletePersistentAll(days);
	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    } finally {
	        pm.close();
	    }
	    resp.sendRedirect("/index.jsp");
	}
}
