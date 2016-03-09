package com.mazebert.presenters.jaxrs.lifecycle;

import com.mazebert.BusinessLogic;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebApplicationLifecycle implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BusinessLogic.getInstance().start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        BusinessLogic.getInstance().shutdown();
    }
}
