package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.EventType;
import com.redhat.quarkus.cafe.domain.KitchenOrderInEvent;
import com.redhat.quarkus.cafe.domain.OrderEvent;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class OrdersService {

    Logger logger = Logger.getLogger(OrdersService.class);

    @Inject
    @RestClient
    BaristaRESTClient baristaRESTClient;

    @Inject
    @RestClient
    KitchenRESTClient kitchenRESTClient;

    public void updateOrders(List<OrderEvent> allEvents) {

        allEvents.forEach(o -> {
            logger.debug(o.toString());
            if (o.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                logger.debug("sending to kitchen");
                kitchenRESTClient.orderIn(o);
            } else if (o.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                logger.debug("sending to kitchen");
                baristaRESTClient.orderIn(o);
            }
        });
    }
}
