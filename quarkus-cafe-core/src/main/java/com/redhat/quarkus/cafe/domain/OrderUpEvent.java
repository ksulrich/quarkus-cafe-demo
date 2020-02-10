package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderUpEvent extends OrderEvent {

    public OrderUpEvent(String orderId, String name, Item item, EventType eventType) {
        super(eventType, orderId, name, item);
    }

    public OrderUpEvent() {
    }
}
