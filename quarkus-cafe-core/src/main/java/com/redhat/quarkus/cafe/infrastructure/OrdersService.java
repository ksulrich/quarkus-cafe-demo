package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.EventType;
import com.redhat.quarkus.cafe.domain.OrderEvent;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

@ApplicationScoped
public class OrdersService {

    Logger logger = Logger.getLogger(OrdersService.class);

    @Inject
    @RestClient
    BaristaRESTClient baristaRESTClient;

    @Inject
    @RestClient
    KitchenRESTClient kitchenRESTClient;

    public CompletableFuture<List<OrderEvent>> publishBeveargeOrders(List<OrderEvent> orders) throws ExecutionException, InterruptedException {

        CompletableFuture<List<OrderEvent>> retVal = new CompletableFuture<>();

        final CountDownLatch latch = new CountDownLatch(orders.size());
        final AtomicReference<Throwable> throwable = new AtomicReference<>();

        BiConsumer<OrderEvent, Throwable> consumer = (e, t) -> {
            if (t != null) {
                throwable.set(t);
            }
            latch.countDown();
        };

        orders.forEach(o -> {
            logger.debug(o.toString());
            if (o.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                logger.debug("sending to kitchen");
                kitchenRESTClient.orderIn(o).whenCompleteAsync(consumer);
            } else if (o.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                logger.debug("sending to barista");
                baristaRESTClient.orderIn(o).whenCompleteAsync(consumer);
            }
        });

        try {
            latch.await();
        } catch (InterruptedException ex) {
            retVal.completeExceptionally(new WebApplicationException(ex, 500));
        }

        Throwable t = throwable.get();
        if (t != null) {
            retVal.completeExceptionally(new WebApplicationException(t, 500));
        }

        retVal.complete(orders);
        return retVal;
    }

    public void updateOrders(List<OrderEvent> allEvents) {

        final CountDownLatch latch = new CountDownLatch(allEvents.size());
        final AtomicReference<Throwable> throwable = new AtomicReference<>();

        BiConsumer<OrderEvent, Throwable> consumer = (e, t) -> {
            if (t != null) {
                throwable.set(t);
            }
            latch.countDown();
        };

        allEvents.forEach(o -> {
            logger.debug(o.toString());
            if (o.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                logger.debug("sending to kitchen");
                kitchenRESTClient.orderIn(o).whenCompleteAsync(consumer);
            } else if (o.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                logger.debug("sending to barista");
                baristaRESTClient.orderIn(o).whenCompleteAsync(consumer);
            }
        });

        try {
            latch.await();
        } catch (InterruptedException ex) {
            throw new WebApplicationException(ex, 500);
        }

        Throwable t = throwable.get();
        if (t != null) {
            throw new WebApplicationException("Failure in downstream service",
                    t, 500);
        }
    }
}
