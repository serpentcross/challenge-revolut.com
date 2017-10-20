package com.revolut.challenge;

import com.revolut.challenge.controllers.MainController;
import com.revolut.challenge.utils.FeedReader;
import com.revolut.challenge.utils.RatesExtractor;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;

public class App {

    public static void main( String[] args ) throws Exception {

        RatesExtractor.getActualRates();

        File customersFeed;

        try {
            customersFeed = new File("data/customers.json");
            FeedReader.parseFoundJSONfeed(customersFeed);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.exit(0);
        }

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.setContextPath("/");

        Server server = new Server(2222);

        server.setHandler(context);

        ServletHolder servletHolder = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");

        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter("jersey.config.server.provider.classnames", MainController.class.getCanonicalName());

        try {
            server.start();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }
}
