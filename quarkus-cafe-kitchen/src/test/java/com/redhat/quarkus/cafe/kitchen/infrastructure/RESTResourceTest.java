package com.redhat.quarkus.cafe.kitchen.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class RESTResourceTest {

    @Test
    public void testOrderInEvent() {

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("eventType", "KITCHEN_ORDER_IN");
        jsonAsMap.put("item", "CAKEPOP");
        jsonAsMap.put("itemId", "fbd7386b-d2db-4d20-82f4-816586a19772");
        jsonAsMap.put("name", "Gooofy");
        jsonAsMap.put("orderId", "f0c9998b-5cb8-4ff8-b06d-65fcc60324de");

        given()
                .contentType(ContentType.JSON)
                .body(jsonAsMap)
                .when()
                .post("/api/orderIn")
                .then()
                .statusCode(200);
    }

}
