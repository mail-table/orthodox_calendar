<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.List, com.googlecode.objectify.ObjectifyService, com.googlecode.objectify.Result, klim.orthodox_calendar.Orthodox_calendarServlet, klim.orthodox_calendar.Day, java.text.SimpleDateFormat, java.util.*" %>
<html>
  <body>
  	<%=(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", new Locale("us"))).format(Day.cutDate(new Date())) %> <br>
    <%=(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", new Locale("us"))).format(new Date()) %> <br>
    <img src="/ic1.gif" />
    <ul>
<%
    List<Day> days = com.googlecode.objectify.ObjectifyService.ofy().load().type(Day.class).list();
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
    		<li>
    		<%= d.getPubDate() %>
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