import application.OrderDataManager;
import application.endpoint.OrderRestEndpoint;
import orders.Order;
import org.junit.Assert;
import org.junit.Test;
import requests.CreateNewOrderRequest;

public class Stage3Test {

    /**
     * Test to ensure that calling the endpoint fulfillOrder will dispatch the order
     */
    @Test
    public void testFulfillOrder() {
        //Create an order
        final String orderRefNumber = OrderDataManager.getInstance().createOrder(new CreateNewOrderRequest("Test", 1));
        final OrderRestEndpoint orderRestEndpoint = new OrderRestEndpoint();
        //Fulfill the order
        orderRestEndpoint.fulfillOrder(orderRefNumber);
        final Order updatedOrder = OrderDataManager.getInstance().getOrder(orderRefNumber);

        //Ensure the order is marked as dispatched
        Assert.assertTrue(updatedOrder.isDispatched());
    }
}
