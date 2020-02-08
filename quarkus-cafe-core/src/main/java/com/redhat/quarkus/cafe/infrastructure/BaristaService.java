package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.OrderEvent;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class BaristaService {


    @Inject
    @RestClient
    BaristaRESTClient baristaRESTClient;
    
    public void updateOrders(List<OrderEvent> allEvents) {

        allEvents.forEach(o -> {
            baristaRESTClient.orderIn(o);
        });
    }
}
