package com.revolut.challenge;

import com.revolut.challenge.constants.EndPoints;
import com.revolut.challenge.constants.Parameters;
import com.revolut.challenge.constants.ServerConfig;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.CoreMatchers.containsString;

public class AppTest {

    @Before
    public void setup() throws Exception {

        RestAssured.baseURI  = ServerConfig.SERVER_HOST;
        RestAssured.port     = ServerConfig.SERVER_PORT;
        RestAssured.basePath = EndPoints.API;
        RestAssured.registerParser(MediaType.TEXT_PLAIN, Parser.TEXT );

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
