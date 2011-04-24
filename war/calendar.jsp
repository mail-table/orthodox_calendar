<%@page trimDirectiveWhitespaces="true" %>
<%@page contentType="text/html;charset=windows-1251" language="java" %>
<%@page import="java.util.List,javax.jdo.PersistenceManager,klim.orthodox_calendar.PMF,klim.orthodox_calendar.Orthodox_calendarServlet,klim.orthodox_calendar.Day" %>
<?xml version="1.0" encoding="windows-1251"?>
<rss version="2.0">
  <channel>
    <title>ЏђЂ‚Ћ‘‹Ђ‚Ќ›‰ ЉЂ‹…Ќ„Ђђњ</title>
    <link>http://calendar.rop.ru</link>
    <description>ЏђЂ‚Ћ‘‹Ђ‚Ќ›‰ ЉЂ‹…Ќ„Ђђњ</description>
    <language>ru</language>
    <managingEditor>mail-table@yandex.ru</managingEditor>
<%
    PersistenceManager pm = PMF.get().getPersistenceManager();
    String query = "select from " + Day.class.getName();
    List<Day> days = (List<Day>) pm.newQuery(query).execute();
    if (!days.isEmpty()) {
        for (Day g : days) {
%>    
    <item>
      <title><%= g.getTitle() %></title>
      <comments><%= g.getComments() %></comments>
      <link><%= g.getLink() %></link>
      <description><%= g.getDescription() %></description>
    </item>
<%
        }
    }
%>
  </channel>
</rss>
