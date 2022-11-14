package application.endpoint;

import application.OrderDataManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import orders.Order;
import requests.CreateNewOrderRequest;
import requests.UpdateBrickAmountRequest;
import responses.ErrorResponse;
import responses.OrderRefNumberResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Rest Endpoints that create and modify orders
 */
@Path("/orders")
public class OrderRestEndpoint {
    /**
     * Endpoint to create a new order and return the reference number
     *
     * @param createNewOrderRequest contains the customer name and the number of bricks they request
     * @return ref number
     */
    @GET
    @Path("/createNewOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public String createNewOrder(@BeanParam final CreateNewOrderRequest createNewOrderRequest) {
        if (createNewOrderRequest == null) return toJSON(new ErrorResponse("No Order Request supplied"));
        if (createNewOrderRequest.getCustomerName() == null)
            return toJSON(new ErrorResponse("Customer Name is Required"));
        if (createNewOrderRequest.getNoOfBricks() < 1)
            return toJSON(new ErrorResponse("Minimum amount of bricks to order is: 1"));

        final OrderDataManager dataManager = getOrderDataManager();
        final String orderRefNumber = dataManager.createOrder(createNewOrderRequest);
        return toJSON(new OrderRefNumberResponse(orderRefNumber));
    }

    /**
     * Find and return order in JSON for a given order ref number
     *
     * @param orderRefNumber order ref number
     * @return order in json if order can be found otherwise return a error response
     */
    @GET
    @Path("/getOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public String getOrder(@QueryParam("orderRefNumber") final String orderRefNumber) {
        final Order order = getOrderDataManager().getOrder(orderRefNumber);
        if (order == null) {
            final ErrorResponse errorResponse = new ErrorResponse("No order exists for given order reference number");
            return toJSON(errorResponse);
        } else {
            return toJSON(order);
        }
    }

    /**
     * Return a json list of orders
     *
     * @return
     */
    @GET
    @Path("/getOrders")
    @Produces(MediaType.APPLICATION_JSON)
    public String getOrders() {
        final List<Order> orders = getOrderDataManager().getOrders();
        return toJSON(orders);
    }

    /**
     * Update the brick amount for a given order
     *
     * @param updateBrickAmountRequest order ref and new no of bricks to update to
     * @return order ref number
     */
    @GET
    @Path("/updateBrickAmountForOrder")
    public String updateBrickAmountForOrder(@BeanParam final UpdateBrickAmountRequest updateBrickAmountRequest) {
        final String orderRefNumber = updateBrickAmountRequest.getOrderRefNumber();
        final int newNoOfBricks = updateBrickAmountRequest.getNoOfBricks();
        if (orderRefNumber == null) return toJSON(new ErrorResponse("Order Ref Number is required"));
        if (newNoOfBricks < 1) return toJSON(new ErrorResponse("Minimum amount of bricks is: 1"));

        //Get the existing order
        final Order order = getOrderDataManager().getOrder(orderRefNumber);

        if (order == null) return toJSON(new ErrorResponse("No order exists for order ref number: " + orderRefNumber));

        final Order updatedOrder = new Order(orderRefNumber, order.getCustomerName(), newNoOfBricks, order.isDispatched());
        //Update the bricks for the order ref
        getOrderDataManager().updateOrder(orderRefNumber, updatedOrder);

        //Return the order ref
        return toJSON(new OrderRefNumberResponse(orderRefNumber));
    }


    /**
     * Endpoint to mark order for the given order ref number as dispatched
     * @param orderRefNumber order ref number
     * @return response
     */
    @Path("/fulfillOrder")
    public Response fulfillOrder(final String orderRefNumber) {
        final Order order = getOrderDataManager().getOrder(orderRefNumber);
        if (order == null) {
            final String errorJson = toJSON(new ErrorResponse("No order exists for order ref number: " + orderRefNumber));
            return Response.status(Response.Status.BAD_REQUEST).entity(errorJson).build();
        }
        final Order updatedOrder = new Order(order.getOrderRef(), order.getCustomerName(), order.getNoOfBricks(), true);
        getOrderDataManager().updateOrder(orderRefNumber, updatedOrder);
        return Response.accepted().entity("Order has been marked as dispatch").build();
    }

    /**
     * Return the singleton instance of the Order Data Manager
     *
     * @return Singleton of Order Data Manager
     */
    private static OrderDataManager getOrderDataManager() {
        return OrderDataManager.getInstance();
    }

    /**
     * Convert the given object to a json object
     *
     * @param object value to be turned into json
     * @return json string
     */
    private String toJSON(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
