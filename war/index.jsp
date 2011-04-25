<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.List,javax.jdo.PersistenceManager,klim.orthodox_calendar.PMF,klim.orthodox_calendar.Orthodox_calendarServlet,klim.orthodox_calendar.Day" %>
<html>
  <body>
    <ul>
<%
    PersistenceManager pm = PMF.get().getPersistenceManager();
    List<Day> days = (List<Day>) pm.newQuery(PMF.query).execute();
    if (!days.isEmpty()) {
%>
<%
    	for (Day d : days) {
%>
    		<li>
    		<%= d.getTitle() %>
    		</li>
    		<li>
    		<%= d.getLink() %>
    		</li>
    		<li>
    		<%= d.getDescription(false) %>
    		</li>
<%
    	}
    }
%>
    </ul>
    <form action="/orthodox_calendar">
    	<input name="dt" value="99">
    	<button type="submit">OK</button>
    </form>
  </body>
</html>