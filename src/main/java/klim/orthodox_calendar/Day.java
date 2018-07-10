package klim.orthodox_calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Day {
  @Id private Long id;

  @Index private Date dayToParse;

  private Date pubDate;

  public void setPubDate(Date pubDate) {
    this.pubDate = pubDate;
  }

  public Date getPubDate() {
    return pubDate;
  }

  public Date getDayToParse() {
    return dayToParse;
  }

  public void setDayToParse(Date dayToParse) {
    this.dayToParse = dayToParse;
    initAllFields();
  }

  @Ignore private Calendar calendarDay;

  @Ignore private Calendar calendarNewDay;

  @Ignore private String title;

  @Ignore private String link;

  @Ignore private String comments;

  private String description;

  public Long getId() {
    return id;
  }

  public String getDescription() {
    return getDescription(false);
  }

  public String getDescription(boolean env) {
    if (env) return escapeHtml(description);
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Ignore private SimpleDateFormat c;

  @Ignore private SimpleDateFormat c1;

  @Ignore private SimpleDateFormat c2;

  @Ignore private Boolean isInitialized;

  public Day(Date day) {
    dayToParse = Day.cutDate(day);
    initAllFields();
    description = new String(parseDay(day));

    this.pubDate = new Date();
  }

  public Day() {}

  private void initAllFields() {
    if (this.isInitialized == null || !isInitialized.booleanValue()) {
      this.c = new SimpleDateFormat("D", new DateFormatSymbols(new java.util.Locale("en")));
      this.c1 = new SimpleDateFormat("d MMMM", new DateFormatSymbols(new java.util.Locale("ru")));
      this.c2 =
          new SimpleDateFormat(
              "d MMMM '20'yy EEEE", new DateFormatSymbols(new java.util.Locale("ru")));

      this.calendarNewDay = new GregorianCalendar();
      this.calendarNewDay.setTime(dayToParse);
      this.calendarDay = new GregorianCalendar();
      this.calendarDay.setTime(dayToParse);
      this.calendarDay.add(Calendar.DATE, -13);

      this.isInitialized = new Boolean(true);
    }
  }

  public String getLink() {
    this.initAllFields();
    this.link =
        "https://calendar.rop.ru/?idd=" + c.format(this.calendarNewDay.getTime()).toLowerCase();

    return this.link;
  }

  public String getTitle() {
    this.initAllFields();
    this.title =
        this.c1.format(this.calendarDay.getTime())
            + " / "
            + this.c2.format(this.calendarNewDay.getTime());
    return this.title;
  }

  public String getComments() {
    this.initAllFields();
    this.comments = getLink();

    return this.comments;
  }

  public String parseDay() {
    return parseDay(new Date());
  }

  public String parseDay(Date day) {
    String ret = new String();
    setCalendarDay(day);

    javax.net.ssl.TrustManager[] trustAllCerts =
        new TrustManager[] {
          new javax.net.ssl.X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
              return null;
            }

            public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {}

            public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {}
          }
        };

    try {
      javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      try {
        this.setCalendarDay(day);
        String toOpen =
            "https://calendar.rop.ru/?idd=" + c.format(getCalendarDay().getTime()).toLowerCase();
        URL url = new URL(toOpen);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(true);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
            || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
          url = new URL(connection.getHeaderField("Location"));
          connection = (HttpURLConnection) url.openConnection();
        }

        ret = "";
        InputStream is = connection.getInputStream();
        ret += toString(is);

        Pattern p =
            Pattern.compile(
                "(<td[^>]*id=.center_td.*>.*)<img[^>]*src=.img/line.*gif.", Pattern.DOTALL);

        Matcher m = p.matcher(ret);
        if (m.find()) {
          ret = m.group(1);
          ret += "</div>";

          Pattern p1 = Pattern.compile("href=\"([^\"]*)\"", Pattern.DOTALL);
          Matcher matcher = p1.matcher(ret);
          StringBuffer buf = new StringBuffer();
          while (matcher.find()) {
            String replaceStr = "href=\"https://calendar.rop.ru/" + matcher.group(1) + "\"";
            matcher.appendReplacement(buf, replaceStr);
          }
          matcher.appendTail(buf);
          ret = buf.toString();
        } else ret = "";

        description = new String(ret);
      } catch (MalformedURLException e) {
        System.err.println(e);
      } catch (IOException e) {
        System.err.println(e);
      }
    } catch (Exception e) {
      System.err.println(e);
    }

    return ret;
  }

  public Calendar getCalendarDay() {
    return this.calendarDay;
  }

  public void setCalendarDay(Date calendarDay) {
    Calendar tmp = new GregorianCalendar();
    tmp.setTime(calendarDay);
    setCalendarDay(tmp);
  }

  public void setCalendarDay(Calendar calendarDay) {
    this.calendarNewDay = calendarDay;

    this.calendarDay = new GregorianCalendar();
    this.calendarDay.setTime(calendarDay.getTime());

    this.title = null;
    this.link = null;
    this.comments = null;
    this.description = new String("");
  }

  private String escapeHtml(String html) {
    if (html == null) {
      return null;
    }
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
  }

  private String toString(InputStream inputStream) throws IOException {
    String string;
    StringBuilder outputBuilder = new StringBuilder();
    if (inputStream != null) {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(inputStream, /* "windows-1251" */ "UTF8"));
      while (null != (string = reader.readLine())) {
        outputBuilder.append(string).append('\n');
      }
    }
    return outputBuilder.toString();
  }

  public static final Date cutDate(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);

    return c.getTime();
  }
}
