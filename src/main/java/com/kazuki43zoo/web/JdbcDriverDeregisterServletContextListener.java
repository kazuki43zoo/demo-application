package com.kazuki43zoo.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.DriverManager;
import java.util.Collections;

@WebListener
public class JdbcDriverDeregisterServletContextListener implements ServletContextListener {

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    Collections.list(DriverManager.getDrivers()).forEach(x -> {
      try {
        sce.getServletContext().log("Deregister the JDBC driver '" + x.getClass().getName() + "'");
        DriverManager.deregisterDriver(x);
      } catch (Exception e) {
        sce.getServletContext().log("Ignored an exception during deregister jdbc driver '" + x.getClass().getName() + "'", e);
      }
    });
  }

}
