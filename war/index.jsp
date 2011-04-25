<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.List,javax.jdo.PersistenceManager,klim.orthodox_calendar.PMF,klim.orthodox_calendar.Orthodox_calendarServlet,klim.orthodox_calendar.Day" %>
<html>
  <body>
    <ul>
<%
    PersistenceManager pm = PMF.get().getPersistenceManager();
    String query = "select from " + Day.class.getName();
    List<Day> days = (List<Day>) pm.newQuery(query).execute();
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
  </body>
</html>