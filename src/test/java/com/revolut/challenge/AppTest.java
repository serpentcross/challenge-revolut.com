package com.revolut.challenge;

import com.revolut.challenge.constants.EndPoints;
import com.revolut.challenge.constants.Parameters;
import com.revolut.challenge.constants.ServerConfig;
import com.revolut.challenge.controllers.MainController;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.CoreMatchers.containsString;

public class AppTest {

    private Server server;
    private ServletContextHandler context;
    private ServletHolder servletHolder;

    @Before
    public void setup() throws Exception {

        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(EndPoints.CONTEXT_PATH);

        server = new Server(ServerConfig.SERVER_PORT);
        server.setHandler(context);

        servletHolder = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter("jersey.config.server.provider.classnames", MainController.class.getCanonicalName());

        RestAssured.baseURI  = ServerConfig.SERVER_HOST;
        RestAssured.port     = ServerConfig.SERVER_PORT;
        RestAssured.basePath = EndPoints.API;
        RestAssured.registerParser(MediaType.TEXT_PLAIN, Parser.TEXT );

        App.loadData();

        server.start();

    }

    @After
    public void shutdownServer() throws Exception {
        server.stop();
    }

    @Test
    public void testUserInfo1() {
        RestAssured.given()
                .queryParam(Parameters.ID, 1)
                .when()
                .get(EndPoints.CUSTOMER)
                .then()
                .body(containsString("9047484839"));
    }

    @Test
    public void testUserInfo2() {
        RestAssured.given()
                .queryParam(Parameters.ID, 2)
                .when()
                .get(EndPoints.CUSTOMER)
                .then().
                statusCode(200);
    }

    @Test
    public void testUserInfo3() {
        RestAssured.given()
                .queryParam(Parameters.ID, 10)
                .when()
                .get(EndPoints.CUSTOMER)
                .then()
                .body(containsString("Unfortunatelly, nothing was found. Please try again. Thank you."));
    }

    @Test
    public void testTransaction1() {
        RestAssured.given()
                .queryParam(Parameters.SENDER_ID, 1)
                .queryParam(Parameters.ACCOUNT_ID, 1)
                .queryParam(Parameters.AMOUNT, 200)
                .queryParam(Parameters.PHONE, "9224940293")
                .when()
                .get(EndPoints.TRANSFER)
                .then().
                statusCode(200);
    }

    @Test
    public void testTransaction2() {
        RestAssured.given()
                .queryParam(Parameters.SENDER_ID, 1)
                .queryParam(Parameters.ACCOUNT_ID, 1)
                .queryParam(Parameters.AMOUNT, 20000)
                .queryParam(Parameters.PHONE, "9769393020")
                .when()
                .get(EndPoints.TRANSFER)
                .then()
                .body(containsString("Not enough funds"));
    }
}
