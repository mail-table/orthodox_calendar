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
		List<Date> dates = new ArrayList<Date>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery("select from "
					+ Configure.class.getName());
			List<Configure> cfg = (List<Configure>) query.execute();
			Configure c;

			if (cfg.isEmpty()) {
				c = new Configure(4, 14);
			} else
				c = cfg.get(0);

			if (req.getParameter("date") != null) {
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
						Locale.US);
				try {
					dates.add(df.parse(req.getParameter("date")));
				} catch (Exception e) {
					logger.warning("Can't parse date from request: "
							+ req.getParameter("date"));
					dates.add(new Date());
				}
			} else if (req.getParameter("daysForward") != null
					|| c.getDaysForward() > 0) {
				int forward = c.getDaysForward();
				if (req.getParameter("daysForward") != null)
					forward = (new Integer(req.getParameter("daysForward")))
							.intValue();
				Calendar day = new GregorianCalendar();
				dates.add(Day.cutDate(day.getTime()));
				for (int i = 0; i < forward; i++) {
					day.add(Calendar.DATE, 1);
					dates.add(Day.cutDate(day.getTime()));
				}
			} else
				dates.add(new Date());

			Calendar delete_date = new GregorianCalendar();
			delete_date.add(Calendar.DATE, -c.getDaysToStore().intValue());
			query = pm.newQuery("select from " + Day.class.getName());
			query.setFilter("dayToParse < date");
			query.declareParameters("java.util.Date date");
			List<Day> results = (List<Day>) query
					.execute(delete_date.getTime());
			pm.deletePersistentAll(results);

			for (Date date : dates) {
				Day d = new Day(date);
				date = Day.cutDate(date);

				query = pm.newQuery("select from " + Day.class.getName());
				query.setFilter("dayToParse == date");
				query.declareParameters("java.util.Date date");
				results = (List<Day>) query.execute(date);
				if (results.isEmpty()) {
					pm.makePersistent(d);
				}
			}
		} catch (Exception e) {
			logger.warning("Error to store: " + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			pm.close();
		}

		try {
			req.getRequestDispatcher("/calendar.jsp").forward(req, resp);
		} catch (Exception e) {
			resp.sendRedirect("/calendar.jsp");
		}
	}
}
