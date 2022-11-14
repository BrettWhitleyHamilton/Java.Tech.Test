package orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import requests.CreateNewOrderRequest;

/**
 * Information about an order
 */
public class Order {

    @JsonProperty
    private String orderRef;
    @JsonProperty
    private String customerName;
    @JsonProperty
    private int noOfBricks;

    public Order() {
    }

    public Order(final String orderRef, final String customerName, final int noOfBricks) {
        this.orderRef = orderRef;
        this.customerName = customerName;
        this.noOfBricks = noOfBricks;
    }

    public Order(final String orderRef,
                 final CreateNewOrderRequest createNewOrderRequest) {
        this(orderRef, createNewOrderRequest.getCustomerName(), createNewOrderRequest.getNoOfBricks());
    }

    public Order(final Order order){
        this.customerName=order.getCustomerName();
        this.orderRef=order.getOrderRef();
        this.noOfBricks=order.getNoOfBricks();
    }

    public int getNoOfBricks() {
        return noOfBricks;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderRef() {
        return orderRef;
    }
}
