package application;

import application.endpoint.OrderRestEndpoint;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


/**
 * Class to identify Rest Endpoints
 */
@ApplicationPath("/api")
public class RestClient extends Application {
    final Set<Object> singletons= new HashSet<>();

    public RestClient() {
        singletons.add(new OrderRestEndpoint());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
