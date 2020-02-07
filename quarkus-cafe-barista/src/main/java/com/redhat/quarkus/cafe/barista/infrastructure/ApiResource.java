package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.Barista;
import com.redhat.quarkus.cafe.barista.domain.BeverageOrder;

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
    Barista barista;

    @POST
    @Path("/orders")
    public Response orderIn(BeverageOrder orderInEvent) {

//        barista.orderIn(orderInEvent);
        return Response.ok().build();
    }
}
