import application.OrderDataManager;
import application.endpoint.OrderRestEndpoint;
import org.junit.Assert;
import org.junit.Test;
import requests.CreateNewOrderRequest;
import requests.UpdateBrickAmountRequest;

import javax.ws.rs.core.Response;

public class Stage4Test {

    /**
     * Test to check that an bad request response returns when trying to update an order that has already been dispatched
     */
    @Test
    public void preventOrderFromBeingUpdated(){
        final OrderDataManager instance = OrderDataManager.getInstance();
        final String orderRefNumber = instance.createOrder(new CreateNewOrderRequest("Test", 1));
        final OrderRestEndpoint orderRestEndpoint = new OrderRestEndpoint();
        orderRestEndpoint.fulfillOrder(orderRefNumber);

        final Response response = orderRestEndpoint.updateBrickAmountForOrder(new UpdateBrickAmountRequest(orderRefNumber, 100));
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),response.getStatus());
    }
}
