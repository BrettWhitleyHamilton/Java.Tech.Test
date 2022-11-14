import application.OrderDataManager;
import application.endpoint.OrderRestEndpoint;
import orders.Order;
import org.junit.Assert;
import org.junit.Test;
import requests.CreateNewOrderRequest;
import requests.UpdateBrickAmountRequest;

public class Stage2Test {

    @Test
    public void testUpdateOrders(){
        final OrderDataManager instance = OrderDataManager.getInstance();
        final OrderRestEndpoint orderRestEndpoint = new OrderRestEndpoint();

        final String orderRefNumber = instance.createOrder(new CreateNewOrderRequest("Test", 1));
        final Order beforeUpdate = instance.getOrder(orderRefNumber);
        final UpdateBrickAmountRequest updateBrickAmountRequest = new UpdateBrickAmountRequest(orderRefNumber, 100);
        orderRestEndpoint.updateBrickAmountForOrder(updateBrickAmountRequest);
        final Order updatedOrder = instance.getOrder(orderRefNumber);

        Assert.assertEquals(beforeUpdate.getOrderRef(),updatedOrder.getOrderRef());
        Assert.assertEquals(beforeUpdate.getCustomerName(),updatedOrder.getCustomerName());
        Assert.assertNotEquals(beforeUpdate.getNoOfBricks(),updatedOrder.getNoOfBricks());


    }
}
