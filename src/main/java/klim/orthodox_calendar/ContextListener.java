package klim.orthodox_calendar;

import javax.servlet.ServletContextListener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
                ObjectifyService.init();
        ObjectifyService.register(Day.class);
        ObjectifyService.register(Configure.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
