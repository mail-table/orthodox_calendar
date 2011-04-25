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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Day {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private Date dayToParse;
	
	@NotPersistent
	private Calendar calendarDay;

	@NotPersistent
	private Calendar calendarNewDay;

	@NotPersistent
	private String title;
	
	@NotPersistent
	private String link;
	
	@NotPersistent
	private String comments;

	@Persistent
	private Text description;
	
	public Long getId() {
		return id;
	}

	public Text getDescription() {
		return getDescription(true);
	}
	public Text getDescription(boolean env) {
		if (env)
			return new Text(escapeHtml(description.toString()));
		return description;
	}

	public void setDescription(Text description) {
		this.description = description;
		System.out.println(this.description);
	}

	@NotPersistent
	private SimpleDateFormat c;

	@NotPersistent
	private SimpleDateFormat c1;

	@NotPersistent
	private SimpleDateFormat c2;
	
	@NotPersistent
	private Boolean isInitialized;
	
	public Day(Date day) {
		dayToParse = day;
		initAllFields();
		description = new Text(parseDay(day));
	}
	
	private void initAllFields() {
		if (this.isInitialized == null || !isInitialized.booleanValue()) {
			this.c=new SimpleDateFormat("MMMdd", new DateFormatSymbols(new java.util.Locale("en")));
			this.c1=new SimpleDateFormat("d MMMM", new DateFormatSymbols(new java.util.Locale("ru")));
			this.c2=new SimpleDateFormat("d MMMM '20'yy EEEE", new DateFormatSymbols(new java.util.Locale("ru")));

			this.calendarNewDay = new GregorianCalendar();
			this.calendarNewDay.setTime(dayToParse);
			this.calendarDay = new GregorianCalendar();
			this.calendarDay.setTime(dayToParse);
			this.calendarDay.add(Calendar.DATE, -13);
			
//			System.out.println("============" + this.c2.toString());
//			System.out.println("============" + this.c1.toString());
//			System.out.println("============" + this.calendarDay.toString());
//			System.out.println("============" + this.calendarNewDay.toString());
			
//			title = getTitle();
//			link = getLink();
//			comments = getComments();
			
			isInitialized=new Boolean(true);
		}
	}
	
	public String getLink() {
		if (this.link == null) {
			this.initAllFields();
			this.link = "http://calendar.rop.ru/mes1/" + 
				c.format(this.calendarDay.getTime()).toLowerCase() + ".html";
		}
		return this.link;
	}
	
	public String getTitle() {
		if (this.title == null) {
			this.initAllFields();
			this.title = this.c1.format(
						this.calendarDay.getTime()) +  
				" / " +this.c2.format(this.calendarNewDay.getTime());
		}
		return this.title;
	}
	
	public String getComments() {
		if (this.comments == null) {
			this.initAllFields();
			this.comments = getLink();
		}
		return this.comments;
	}
	
	public String parseDay() {
		return parseDay(new Date());
	}

	public String parseDay(Date day) {
		String ret=new String();
		setCalendarDay(day);

		try {
			this.setCalendarDay(day);
			String toOpen="http://calendar.rop.ru/mes1/" + c.format(getCalendarDay().getTime()).toLowerCase() + ".html";
			URL url = new URL(toOpen);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            
            ret="";
            InputStream is = connection.getInputStream();
            ret += toString(is);
            
            Pattern p=Pattern.compile("(<div[^>]*id=.block.*)<img src=\"img/line1.gif\"", Pattern.DOTALL);
            
            Matcher m=p.matcher(ret);
            if ( m.find() ) {
	            ret = m.group(1);
	            ret += "</div>";

            	Pattern p1 = Pattern.compile("href=\"([^\"]*)\"", Pattern.DOTALL);
            	Matcher matcher = p1.matcher(ret);
            	StringBuffer buf = new StringBuffer();
            	while (matcher.find()) {
            	    String replaceStr = "href=\"http://calendar.rop.ru/mes1/" + matcher.group(1) + "\"";
            	    matcher.appendReplacement(buf, replaceStr);
            	}
            	matcher.appendTail(buf);
            	ret=buf.toString();
            } else
            	ret = "";
            
            /*String out="<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n";
            out += "<rss version=\"2.0\">\n";
            out += "  <channel>\n";

            out += "    <title>������������ ���������</title>\n";
            out += "    <link>http://calendar.rop.ru</link>\n";
            out += "    <description>������������ ���������</description>\n";
            out += "    <language>ru</language>\n";
            out += "    <managingEditor>mail-table@yandex.ru</managingEditor>\n";
            
            out += "    <item>\n";
            out += "      <title>"+ c1.format(cal.getTime()) + " / " +c2.format(cal1.getTime()) +"</title>\n";
            out += "      <comments>"+ toOpen +"</comments>\n";
            out += "      <link>"+ toOpen +"</link>\n";
            out += "      <description>"+ escapeHtml(ret) +"</description>\n";
            
            out += "    </item>\n";
            
            
            out += "  </channel>\n";
            out += "</rss>";*/
            
            /*
            for (int i=0; ; i++) {
                String headerName = connection.getHeaderFieldKey(i);
                String headerValue = connection.getHeaderField(i);
                ret += headerName + " = " + headerValue + "<br>";

                if (headerName == null && headerValue == null) {
                    // No more headers
                    break;
                }
                if (headerName == null) {
                    // The header value contains the server's HTTP version
                }
            }*/            
            description = new Text(ret);
        } catch (MalformedURLException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        } /*catch (Exception e) {
            System.err.println(e);
        }*/		

		return ret;
		/*return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using__:<br>" + userAgent;*/
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
		this.calendarDay.add(Calendar.DATE, -13);		
		
		this.title=null;
		this.link=null;
		this.comments=null;
		this.description=new Text("");
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	private String toString(InputStream inputStream) throws IOException {
	    String string;
	    StringBuilder outputBuilder = new StringBuilder();
	    if (inputStream != null) {
	      BufferedReader reader =
	          new BufferedReader(new InputStreamReader(inputStream,"windows-1251"/*"UTF8"*/));
	      while (null != (string = reader.readLine())) {
	        outputBuilder.append(string).append('\n');
	      }
	    }
	    return outputBuilder.toString();
	  }
}
