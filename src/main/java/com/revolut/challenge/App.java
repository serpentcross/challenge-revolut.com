package com.revolut.challenge;

import com.revolut.challenge.constants.EndPoints;
import com.revolut.challenge.constants.ServerConfig;
import com.revolut.challenge.controllers.MainController;
import com.revolut.challenge.utils.FeedReader;
import com.revolut.challenge.utils.RatesExtractor;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.xml.transform.TransformerException;

import java.io.File;

import java.net.MalformedURLException;

public class App {

    private static Server server;

    public static void main( String[] args ) throws Exception {
        loadData();
        serverStart();
    }

    private static void serverStart() throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(EndPoints.CONTEXT_PATH);

        server = new Server(ServerConfig.SERVER_PORT);
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
            serverStop();
        }
    }

    private static void serverStop() {
        server.destroy();
    }

    public static void loadData() throws TransformerException, MalformedURLException {

        RatesExtractor.getActualRates();

        try {
            File customersFeed = new File("data/customers.json");
            FeedReader.parseFoundJSONfeed(customersFeed);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
