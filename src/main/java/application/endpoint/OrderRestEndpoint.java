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
    public Response createNewOrder(@BeanParam final CreateNewOrderRequest createNewOrderRequest) {
        if (createNewOrderRequest == null) return createJSONErrorResponse("No Order Request supplied");
        if (createNewOrderRequest.getCustomerName() == null)
            return createJSONErrorResponse("Customer Name is Required");
        if (createNewOrderRequest.getNoOfBricks() < 1)
            return createJSONErrorResponse("Minimum amount of bricks to order is: 1");

        final OrderDataManager dataManager = getOrderDataManager();
        final String orderRefNumber = dataManager.createOrder(createNewOrderRequest);
        return createJSONOKResponse(new OrderRefNumberResponse(orderRefNumber));
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
    public Response getOrder(@QueryParam("orderRefNumber") final String orderRefNumber) {
        final Order order = getOrderDataManager().getOrder(orderRefNumber);
        if (order == null) {
            return createJSONErrorResponse("No order exists for given order reference number");
        } else {
            return createJSONOKResponse(order);
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
    public Response getOrders() {
        final List<Order> orders = getOrderDataManager().getOrders();
        return createJSONOKResponse(orders);
    }

    /**
     * Update the brick amount for a given order
     *
     * @param updateBrickAmountRequest order ref and new no of bricks to update to
     * @return order ref number
     */
    @GET
    @Path("/updateBrickAmountForOrder")
    public Response updateBrickAmountForOrder(@BeanParam final UpdateBrickAmountRequest updateBrickAmountRequest) {
        final String orderRefNumber = updateBrickAmountRequest.getOrderRefNumber();
        final int newNoOfBricks = updateBrickAmountRequest.getNoOfBricks();
        if (orderRefNumber == null) return createJSONErrorResponse("Order Ref Number is required");
        if (newNoOfBricks < 1) return createJSONErrorResponse("Minimum amount of bricks is: 1");

        //Get the existing order
        final Order order = getOrderDataManager().getOrder(orderRefNumber);

        if (order == null) return createJSONErrorResponse("No order exists for order ref number: " + orderRefNumber);

        if (order.isDispatched()) return createJSONErrorResponse("Unable to change order once order has been dispatched");

        final Order updatedOrder = new Order(orderRefNumber, order.getCustomerName(), newNoOfBricks, order.isDispatched());
        //Update the bricks for the order ref
        getOrderDataManager().updateOrder(orderRefNumber, updatedOrder);

        //Return the order ref
        return createJSONOKResponse(new OrderRefNumberResponse(orderRefNumber));
    }


    /**
     * Endpoint to mark order for the given order ref number as dispatched
     *
     * @param orderRefNumber order ref number
     * @return response
     */
    @GET
    @Path("/fulfillOrder")
    public Response fulfillOrder(final @QueryParam("orderRefNumber") String orderRefNumber) {
        final Order order = getOrderDataManager().getOrder(orderRefNumber);
        if (order == null) {
            return createJSONErrorResponse("No order exists for order ref number: " + orderRefNumber);
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


    /**
     * Create an error response object with a message
     * @param message
     * @return
     */
    private Response createJSONErrorResponse(final String message) {
        final String jsonErrorResponse = toJSON(new ErrorResponse(message));
        return Response.status(Response.Status.BAD_REQUEST).entity(jsonErrorResponse).build();
    }

    private Response createJSONOKResponse(final Object object){
        final String jsonErrorResponse = toJSON(object);
        return Response.ok().entity(jsonErrorResponse).build();
    }

}
