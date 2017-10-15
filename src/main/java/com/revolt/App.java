package com.revolt;

import com.revolt.utils.FeedReader;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;

public class App
{
    public static void main( String[] args ) throws Exception {

        File folderWithFiles = new File("data");

        try {
            FeedReader.readFilesFromFolder(folderWithFiles);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }

        ResourceConfig config = new ResourceConfig();
        config.packages("com.revolt");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(2222);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");


        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}
