package com.redhat.quarkus.cafe.infrastructure;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.Order;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class ApiTest {

    static final Jsonb jsonb = JsonbBuilder.create();

    private static WireMockServer baristaMockServer;

    private static WireMockServer dashboardMockServer;

    private static WireMockServer kitchenMockServer;

    static final String BARISTA_HOST = "localhost";

    static final int BARISTA_PORT = 8081;

    static final String DASHBOARD_HOST = "localhost";

    static final int DASHBOARD_PORT = 8084;

    static final String KITCHEN_HOST = "localhost";

    static final int KITCHEN_PORT = 8082;

    @BeforeAll
    public static void setUp() {

        baristaMockServer = new WireMockServer(new WireMockConfiguration()
                .bindAddress(BARISTA_HOST)
                .port(BARISTA_PORT));

        dashboardMockServer = new WireMockServer(new WireMockConfiguration()
                .bindAddress(DASHBOARD_HOST)
                .port(DASHBOARD_PORT));

        kitchenMockServer = new WireMockServer(new WireMockConfiguration()
                .bindAddress(KITCHEN_HOST)
                .port(KITCHEN_PORT));

        baristaMockServer.start();
        dashboardMockServer.start();
        kitchenMockServer.start();

        WireMock baristaMock = new WireMock(BARISTA_HOST, BARISTA_PORT);
        WireMock dashboardMock = new WireMock(DASHBOARD_HOST, DASHBOARD_PORT);
        WireMock kitchenMock = new WireMock(KITCHEN_HOST, KITCHEN_PORT);

        baristaMock.register(post(
                urlEqualTo("/api/orders"))
                    .willReturn(aResponse().withStatus(200)));

        dashboardMock.register(post(
                urlEqualTo("/update"))
                .willReturn(aResponse().withStatus(200)));

        kitchenMock.register(post(
                urlEqualTo("/api/orders"))
                .willReturn(aResponse().withStatus(200)));
    }

    @AfterAll
    public static void tearDown() {

        baristaMockServer.stop();
        kitchenMockServer.stop();
    }

    @Test
    @Timeout(5)
    public void testOrderIn() {

        List<Order> beverageList = new ArrayList<>();
        beverageList.add(new Order(Item.CAPPUCCINO, "Huey"));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand();
        createOrderCommand.addBeverages(beverageList);

        System.out.println(jsonb.toJson(createOrderCommand));
        given()
                .body(jsonb.toJson(createOrderCommand))
                .contentType(ContentType.JSON)
                .when().post("/order")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }


}
