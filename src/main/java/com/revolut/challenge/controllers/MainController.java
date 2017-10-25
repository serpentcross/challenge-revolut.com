package com.revolut.challenge.controllers;

import com.revolut.challenge.constants.EndPoints;
import com.revolut.challenge.constants.Parameters;
import com.revolut.challenge.exceptions.PaymentException;
import com.revolut.challenge.models.Customer;
import com.revolut.challenge.services.ProcessingService;
import com.revolut.challenge.utils.FeedReader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Set;

@Path(EndPoints.API)
public class MainController {

    private ProcessingService processingService = new ProcessingService();

    @GET
    @Path(EndPoints.CUSTOMER)
    @Produces(MediaType.TEXT_PLAIN)
    public String customerInfo(@QueryParam(Parameters.ID) int id) {

        String result = "Unfortunatelly, nothing was found. Please try again. Thank you.";
        Set<Customer> customers = FeedReader.getCustomers();

        for (Customer customer : customers) {
            if (customer.getId() == id) {
                result = customer.toString();
            }
        }
        return result;
    }

    @GET
    @Path(EndPoints.TRANSFER)
    @Produces(MediaType.TEXT_PLAIN)
    public Response makeTransfer(@QueryParam(Parameters.SENDER_ID) int senderId, @QueryParam(Parameters.ACCOUNT_ID) int accountId, @QueryParam(Parameters.AMOUNT) double amount, @QueryParam(Parameters.PHONE) String receiverPhoneNumber) {
        try {
            return Response.ok(processingService.initiateTransfer(senderId, accountId, amount, receiverPhoneNumber)).build();
        } catch(PaymentException ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }
}
