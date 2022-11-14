package requests;

import javax.ws.rs.QueryParam;

/**
 * POJO of request parameters for Create New Order Request
 */
public class CreateNewOrderRequest {

    @QueryParam("noOfBricks")
    private int noOfBricks;
    @QueryParam("customerName")
    private String customerName;

    public CreateNewOrderRequest() {
    }

    public CreateNewOrderRequest(final String customerName, final int noOfBricks){
        this.customerName=customerName;
        this.noOfBricks=noOfBricks;
    }

    public int getNoOfBricks() {
        return noOfBricks;
    }

    public String getCustomerName() {
        return customerName;
    }
}
