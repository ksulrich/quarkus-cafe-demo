package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.Kitchen;
import com.redhat.quarkus.cafe.kitchen.domain.OrderEvent;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiResource {

    @Inject
    Kitchen kitchen;

    @POST
    @Path(("/orderIn"))
    public Response orderIn(OrderEvent orderInEvent) {

        kitchen.orderIn(orderInEvent);
        return Response.ok().build();
    }

}
