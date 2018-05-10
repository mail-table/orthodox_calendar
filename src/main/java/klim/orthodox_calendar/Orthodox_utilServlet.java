package klim.orthodox_calendar;

import java.io.IOException;
import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.Key;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class Orthodox_utilServlet extends HttpServlet {
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		Day d = new Day(new java.util.Date());
		try {
		} catch (Exception e) {
		} finally {
		}
		resp.sendRedirect("/index.jsp");
	}
}
