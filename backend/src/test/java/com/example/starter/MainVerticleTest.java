package com.example.starter;

import io.restassured.RestAssured;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(VertxExtension.class)
public class MainVerticleTest {

    @BeforeEach
    void setup(Vertx vertx, VertxTestContext testContext) {

        vertx.deployVerticle(new MainVerticle())
            .onSuccess(id -> {
                RestAssured.baseURI = "http://localhost";
                RestAssured.port = 8888;
                testContext.completeNow();
            })
            .onFailure(testContext::failNow);
    }

    @Test
    void testListItems() {

        given()
            .when()
            .get("/items")
            .then()
            .statusCode(200);
    }

    @Test
    void testGetSingleItem() {

        given()
            .when()
            .get("/items/1")
            .then()
            .statusCode(200)
            .body("name", equalTo("Item 1"));
    }

    @Test
    void testCreateItem() {

        String body = """
            {
                "name":"Laptop",
                "description":"Dell Laptop"
            }
            """;

        given()
            .header("Content-Type", "application/json")
            .body(body)
            .when()
            .post("/items")
            .then()
            .statusCode(201)
            .body("name", equalTo("Laptop"));
    }

    @Test
    void testUpdateItem() {

        String body = """
            {
                "name":"Updated Item"
            }
            """;

        given()
            .header("Content-Type", "application/json")
            .body(body)
            .when()
            .put("/items/1")
            .then()
            .statusCode(200)
            .body("name", equalTo("Updated Item"));
    }

    @Test
    void testDeleteItem() {

        given()
            .when()
            .delete("/items/1")
            .then()
            .statusCode(200)
            .body("message", equalTo("Item deleted"));
    }
}