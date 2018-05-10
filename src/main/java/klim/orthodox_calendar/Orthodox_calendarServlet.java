package klim.orthodox_calendar;

import java.io.IOException;

import javax.servlet.http.*;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Ignore;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.Key;

import java.util.logging.Logger;
import klim.orthodox_calendar.Day;
import klim.orthodox_calendar.Configure;
import java.util.*;
import java.text.*;

import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class Orthodox_calendarServlet extends HttpServlet {
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		List<Date> dates = new ArrayList<Date>();

		try {
			List<Configure> cfg = ofy().load().type(Configure.class).list();
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

			// delete old days
			Calendar delete_date = new GregorianCalendar();
			delete_date.add(Calendar.DATE, -c.getDaysToStore().intValue());
			Iterable<Key<Day>> deleteKeys = ofy().load().type(Day.class).filter("dayToParse < ", delete_date.getTime()).keys();
			ofy().delete().keys(deleteKeys);

			for (Date date : dates) {
				date = Day.cutDate(date);

				List<Day> res = ofy().load().type(Day.class).filter("dayToParse", date).list();
				if (res.isEmpty()) {
					Day d = new Day(date);
					ofy().save().entity(d).now();
				}
			}
		} catch (Exception e) {
			logger.warning("Error to store: " + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
		}

		try {
			req.getRequestDispatcher("/calendar.jsp").forward(req, resp);
		} catch (Exception e) {
			resp.sendRedirect("/calendar.jsp");
		}
	}
}
