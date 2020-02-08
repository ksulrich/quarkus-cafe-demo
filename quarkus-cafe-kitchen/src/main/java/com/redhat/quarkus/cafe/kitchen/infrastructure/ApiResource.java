package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.Kitchen;
import com.redhat.quarkus.cafe.kitchen.domain.OrderEvent;
import org.jboss.logging.Logger;

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

    Logger logger = Logger.getLogger(ApiResource.class);

    @Inject
    Kitchen kitchen;

    @POST
    @Path(("/orders"))
    public Response orderIn(OrderEvent orderInEvent) {

        logger.debug(orderInEvent.toString());
        System.out.println("Kitchen order in :" + orderInEvent.toString());

//        kitchen.orderIn(orderInEvent);
        return Response.accepted().entity(orderInEvent).build();
    }

}
