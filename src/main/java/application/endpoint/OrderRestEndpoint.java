package application.endpoint;

import application.OrderDataManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import orders.Order;
import requests.CreateNewOrderRequest;
import responses.ErrorResponse;
import responses.OrderRefNumberResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
        if(createNewOrderRequest==null) return toJSON(new ErrorResponse("No Order Request supplied"));
        if(createNewOrderRequest.getCustomerName()==null) return toJSON(new ErrorResponse("Customer Name is Required"));
        if(createNewOrderRequest.getNoOfBricks()<1) return toJSON(new ErrorResponse("Minimum amount of bricks to order is: 1"));

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
