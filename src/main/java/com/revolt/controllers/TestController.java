package com.revolt.controllers;

import com.revolt.models.Bank;
import com.revolt.utils.FeedReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import java.util.Iterator;
import java.util.Set;

@Path("api")
public class TestController {

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_HTML)
    public String helloWorld() {

        String bbb = null;

        for (Bank singleBank: FeedReader.getBanks()) {
            bbb = singleBank.toString();
        }

        return "<b>HEllo!</b>";
    }

    @GET
    @Path("bank/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String bankInfo(@PathParam("id") int id) {

        String result = "Unfortunatelly, nothing was found. Please try again. Thank you.";

        Set<Bank> banks = FeedReader.getBanks();

        for (Iterator<Bank> bankIterator = banks.iterator(); bankIterator.hasNext();) {
            Bank bank = bankIterator.next();
            if (bank.getId() == id) {
                result = bank.toString();
            }
        }

        return result;
    }

    @GET
    @Path("bank/{id}/customer/{num}")
    @Produces(MediaType.TEXT_HTML)
    public String customerInfo(@PathParam("id") int id, @PathParam("num") int num) {

        String result = "Unfortunatelly, nothing was found. Please try again. Thank you.";

        return result;
    }


    @POST
    @Path("/transfer")
    @Consumes("application/json")
    @Produces(MediaType.TEXT_HTML)
    public String makeTransfer() {
        return " ";
    }

}
