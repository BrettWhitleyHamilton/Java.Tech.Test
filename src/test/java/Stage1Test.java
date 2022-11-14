import application.OrderDataManager;
import application.endpoint.OrderRestEndpoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import orders.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import requests.CreateNewOrderRequest;
import responses.OrderRefNumberResponse;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Stage1Test {

    private OrderRestEndpoint orderRestEndpoint;

    @Before
    public void setUp() throws Exception {
        orderRestEndpoint = new OrderRestEndpoint();
    }

    /**
     * Test to submit a new order using the createNewOrderEndpoint
     */
    @Test
    public void testSubmitNewOrder() {
        final Set<String> uniqueRefNumbers = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            final Response orderReference = orderRestEndpoint.createNewOrder(new CreateNewOrderRequest("Test" + i, i));
            if(!uniqueRefNumbers.contains(orderReference)){
                uniqueRefNumbers.add(orderReference.getEntity().toString());
            }else{
                Assert.fail("Creating new order has returned a duplicate order ref number");
            }

        }
    }


    /**
     * Testing the getOrder method on the Order Rest Endpoint
     */
    @Test
    public void testGetOrder() {
        final String customerName = "Test";
        final int noOfBricks = 5;

        try {
            final CreateNewOrderRequest newRequest = new CreateNewOrderRequest(customerName, noOfBricks);
            final String orderRefNumber = OrderDataManager.getInstance().createOrder(newRequest);

            final Response orderJson = orderRestEndpoint.getOrder(orderRefNumber);
            final Order order = new ObjectMapper().readValue(orderJson.getEntity().toString(), Order.class);
            Assert.assertEquals(customerName,order.getCustomerName());
            Assert.assertEquals(noOfBricks,order.getNoOfBricks());
        } catch (final JsonProcessingException e) {
            Assert.fail(e.getMessage());
        }
    }


    /**
     * Test to retrieve get orders
     */
    @Test
    public void testGetOrdersTest(){
        //Create multiple orders
        orderRestEndpoint.createNewOrder(new CreateNewOrderRequest("Test",1));
        orderRestEndpoint.createNewOrder(new CreateNewOrderRequest("Test",2));

        try {
            //Get Orders will return a json string of the order
            final Response orders = orderRestEndpoint.getOrders();
            final ObjectMapper objectMapper = new ObjectMapper();

            //Turn the JSON to list of orders
            final List<Order> deserialisedListOfOrder = objectMapper.readValue(orders.getEntity().toString(), new TypeReference<List<Order>>() {});

            //Ensure that we have multiple orders in the list
            Assert.assertTrue(deserialisedListOfOrder.size()>1);
        } catch (final JsonProcessingException e) {
            Assert.fail(e.getMessage());
        }
    }

}
