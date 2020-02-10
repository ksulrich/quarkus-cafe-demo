package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.Cafe;
import com.redhat.quarkus.cafe.domain.OrderEvent;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    @Inject
    Cafe cafe;

    @POST
    public CompletionStage<Response> addEvent(OrderEvent orderEvent) {

        return cafe.orderUp(orderEvent)
            .thenApply(s -> {
                return Response.ok().build();
            }).exceptionally(e -> {
                return Response.serverError().entity(e).build();
            }).toCompletableFuture();
    }

}
