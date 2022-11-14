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
    @JsonProperty
    private boolean dispatched;

    public Order() {
    }

    public Order(final String orderRef, final String customerName, final int noOfBricks,boolean dispatched) {
        this.orderRef = orderRef;
        this.customerName = customerName;
        this.noOfBricks = noOfBricks;
        this.dispatched =dispatched;
    }

    public Order(final String orderRef,
                 final CreateNewOrderRequest createNewOrderRequest) {
        this(orderRef, createNewOrderRequest.getCustomerName(), createNewOrderRequest.getNoOfBricks(),false);
    }

    public Order(final Order order){
        this.customerName=order.getCustomerName();
        this.orderRef=order.getOrderRef();
        this.noOfBricks=order.getNoOfBricks();
        this.dispatched =order.isDispatched();
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

    public boolean isDispatched() {
        return dispatched;
    }
}
