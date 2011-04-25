package klim.orthodox_calendar;

import java.io.IOException;

import javax.servlet.http.*;
import javax.jdo.*;

import java.util.logging.Logger;
import klim.orthodox_calendar.Day;
import klim.orthodox_calendar.PMF;
import java.util.*;
import java.text.*;

@SuppressWarnings("serial")
public class Orthodox_calendarServlet extends HttpServlet {
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		Date date;
		
		if (req.getParameter("date") != null) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
			try {
				date = df.parse(req.getParameter("date"));
			} 
			catch (Exception e)	{
				logger.warning("Can't parse date from request: " + req.getParameter("date"));
				date = new Date();
			}
		} else
			date = new Date();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Day d = new Day(date);
	    try {
	    	Query query = pm.newQuery(Day.class.getName(), "dayToParse < :date");
	    	List<Day> results = (List<Day>)query.execute(date);
	    	if (results.isEmpty())
	    		pm.makePersistent(d);
	    	else
				logger.warning("In DB already present date: " + date.toString());
	    } catch (Exception e) {
	    } finally {
	        pm.close();
	    }

	    resp.sendRedirect("/calendar.jsp");
	}
}
