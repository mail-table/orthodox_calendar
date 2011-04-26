<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.List, javax.jdo.PersistenceManager, klim.orthodox_calendar.PMF, klim.orthodox_calendar.Orthodox_calendarServlet, klim.orthodox_calendar.Day, java.text.SimpleDateFormat, java.util.*" %>
<?xml version="1.0" encoding="utf-8"?>
<rss version="2.0">
  <channel>
    <title>Православный календарь</title>
    <link>http://calendar.rop.ru</link>
    <description>Православный календарь</description>
    <language>ru</language>
    <pubDate><%=(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", new Locale("us"))).format(Day.cutDate(new Date())) %></pubDate>
    <lastBuildDate><%=(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", new Locale("us"))).format(new Date()) %></lastBuildDate>
    <ttl>480</ttl>
    <image>
        <utl>/ic1.gif</url>
    </image>
    <managingEditor>mail-table@yandex.ru</managingEditor>
<%
    PersistenceManager pm = PMF.get().getPersistenceManager();
    List<Day> days = (List<Day>) pm.newQuery(PMF.query).execute();
    if (!days.isEmpty()) {
        for (Day d : days) {
%>
    <item>
      <pubDate><%=(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", new Locale("us"))).format(d.getPubDate()) %></pubDate>
      <title><%= d.getTitle() %></title>
      <comments><%= d.getComments() %></comments>
      <link><%= d.getLink() %></link>
      <description><%= d.getDescription(true) %></description>
    </item>
<%
        }
    }
%>
  </channel>
</rss>