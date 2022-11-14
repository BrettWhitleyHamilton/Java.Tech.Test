package responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRefNumberResponse {
    @JsonProperty
    private String orderRefNumber;

    //Default constructor
    public OrderRefNumberResponse(){}
    public OrderRefNumberResponse(final String orderRefNumber) {
        this.orderRefNumber = orderRefNumber;
    }

    public String getOrderRefNumber() {
        return orderRefNumber;
    }
}
