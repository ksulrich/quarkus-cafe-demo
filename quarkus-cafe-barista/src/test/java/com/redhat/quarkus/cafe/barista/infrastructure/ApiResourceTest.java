package com.redhat.quarkus.cafe.barista.infrastructure;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ApiResourceTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setUp() {

        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.stubFor(
                WireMock.post("/api/orders").willReturn(
                        WireMock.aResponse()
                                .withStatus(200)));
    }

    @AfterAll
    public static void tearDown() {

        wireMockServer.stop();
    }

    @Test
    public void testOrderInEvent() {

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("eventType", "BEVERAGE_ORDER_IN");
        jsonAsMap.put("item", "COFFEE_BLACK");
        jsonAsMap.put("itemId", "fbd7386b-d2db-4d20-82f4-816586a19772");
        jsonAsMap.put("name", "Gooofy");
        jsonAsMap.put("orderId", "f0c9998b-5cb8-4ff8-b06d-65fcc60324de");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonAsMap)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200);
    }


}
