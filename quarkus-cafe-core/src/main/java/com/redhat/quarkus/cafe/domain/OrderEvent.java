package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.UUID;

@RegisterForReflection
public class OrderEvent {

    public String itemId;
    public String orderId;
    public EventType eventType;
    public String name;
    public Item item;

    public OrderEvent() {
    }

    public OrderEvent(EventType eventType, String orderId, String name, Item item) {
        this.itemId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.orderId = orderId;
        this.name = name;
        this.item = item;
    }
}
