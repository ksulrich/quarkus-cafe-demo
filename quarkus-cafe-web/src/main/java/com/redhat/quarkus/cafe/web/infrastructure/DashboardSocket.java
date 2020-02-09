package com.redhat.quarkus.cafe.web.infrastructure;

import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonBuilderFactory;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.quarkus.vertx.ConsumeEvent;

@ServerEndpoint("/dashboard/updates/{username}")
@ApplicationScoped
public class DashboardSocket {

    private static final Logger LOG = Logger.getLogger(DashboardSocket.class);

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    Jsonb jsonb = JsonbBuilder.create();

    @ConsumeEvent("updates")
    public void consume(String event) {
        System.out.println("Consuming: " + event);
        broadcast(event);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        LOG.error("onError", throwable);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        if (message.equalsIgnoreCase("_ready_")) {
            broadcast("User " + username + " joined");
        } else {
            broadcast(message);
        }
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }


}
