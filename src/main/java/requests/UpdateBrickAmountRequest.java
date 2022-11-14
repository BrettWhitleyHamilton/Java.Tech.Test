package requests;

import javax.ws.rs.QueryParam;

public class UpdateBrickAmountRequest {

    @QueryParam("orderRefNumber")
    private String orderRefNumber;
    @QueryParam("noOfBricks")
    private int noOfBricks;

    public UpdateBrickAmountRequest() {
    }

    public UpdateBrickAmountRequest(final String orderRefNumber, final int noOfBricks) {
        this.orderRefNumber = orderRefNumber;
        this.noOfBricks = noOfBricks;
    }

    public String getOrderRefNumber() {
        return orderRefNumber;
    }

    public int getNoOfBricks() {
        return noOfBricks;
    }
}
