package com.revolt;

import com.revolt.utils.FeedReader;
import com.revolt.utils.RatesExtractor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;

public class App {

    public static void main( String[] args ) throws Exception {
        RatesExtractor.getActualRates();

//        File customersFeed;
//
//        try {
//            customersFeed = new File("data/customers.json");
//            FeedReader.parseFoundJSONfeed(customersFeed);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            System.exit(0);
//        }
//
//        ResourceConfig config = new ResourceConfig();
//        config.packages("com.revolt");
//        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
//
//        Server server = new Server(2222);
//        ServletContextHandler context = new ServletContextHandler(server, "/*");
//        context.addServlet(servlet, "/*");
//
//
//        try {
//            server.start();
//            server.join();
//        } finally {
//            server.destroy();
//        }
    }
}
