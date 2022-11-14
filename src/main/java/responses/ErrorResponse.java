package responses;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response for rest endpoints to display error messages during an invocation of an endpoint
 */
public class ErrorResponse {

    @JsonProperty
    private final String errorMessage;

    public ErrorResponse(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
