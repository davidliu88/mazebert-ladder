package com.mazebert.presenters.jaxrs.lifecycle;

import com.mazebert.Logic;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebApplicationLifecycle implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Logic.instance.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Logic.instance.shutdown();
    }
}
