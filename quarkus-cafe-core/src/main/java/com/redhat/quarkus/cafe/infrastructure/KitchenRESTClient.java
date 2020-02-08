package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.OrderEvent;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/orders")
@RegisterRestClient
public interface KitchenRESTClient {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response orderIn(OrderEvent orderEvent);
}
