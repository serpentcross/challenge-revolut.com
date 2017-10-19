package com.revolt.controllers;

import com.revolt.exceptions.PaymentException;
import com.revolt.models.Customer;
import com.revolt.services.ProcessingService;
import com.revolt.utils.FeedReader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Set;

@Path("api")
public class MainController {

    private ProcessingService processingService = new ProcessingService();

    @GET
    @Path("customer/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String bankInfo(@PathParam("id") int id) {

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
    @Path("/transfer")
    @Produces(MediaType.TEXT_HTML)
    public Response makeTransfer(@QueryParam("senderId") int senderId, @QueryParam("accountId") int accountId, @QueryParam("amount") double amount, @QueryParam("phone") String receiverPhoneNumber) {
        try {
            return Response.ok(processingService.initiateTransfer(senderId, accountId, amount, receiverPhoneNumber)).build();
        } catch(PaymentException ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }
}
