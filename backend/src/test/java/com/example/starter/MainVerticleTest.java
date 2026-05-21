package com.example.starter;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(VertxExtension.class)
public class MainVerticleTest {

  private WebClient client;

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    client = WebClient.create(vertx);
    // Deploy the MainVerticle before running each test
    vertx.deployVerticle(new MainVerticle())
      .onComplete(testContext.succeedingThenComplete());
  }

  @Test
  void test_list_items(VertxTestContext testContext) {
    client.get(8888, "localhost", "/items")
      .send()
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(200, response.statusCode());
        JsonArray array = response.bodyAsJsonArray();
        // Verifies the 2 sample items added by addSampleData()
        assertEquals(2, array.size());
        assertEquals("Item 1", array.getJsonObject(0).getString("name"));
        testContext.completeNow();
      })));
  }

  @Test
  void test_get_valid_item(VertxTestContext testContext) {
    client.get(8888, "localhost", "/items/1")
      .send()
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(200, response.statusCode());
        JsonObject item = response.bodyAsJsonObject();
        assertEquals(1, item.getInteger("id"));
        assertEquals("Item 1", item.getString("name"));
        testContext.completeNow();
      })));
  }

  @Test
  void test_get_item_not_found(VertxTestContext testContext) {
    client.get(8888, "localhost", "/items/999")
      .send()
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(404, response.statusCode());
        assertEquals("Item not found", response.bodyAsString());
        testContext.completeNow();
      })));
  }

  @Test
  void test_create_item(VertxTestContext testContext) {
    JsonObject newItem = new JsonObject()
      .put("name", "New Item")
      .put("description", "Testing creation");

    client.post(8888, "localhost", "/items")
      .sendJson(newItem)
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(201, response.statusCode());
        JsonObject created = response.bodyAsJsonObject();
        assertNotNull(created.getInteger("id"));
        assertEquals("New Item", created.getString("name"));
        testContext.completeNow();
      })));
  }

  @Test
  void test_update_item(VertxTestContext testContext) {
    JsonObject updateData = new JsonObject()
      .put("name", "Updated Name");

    client.put(8888, "localhost", "/items/1")
      .sendJson(updateData)
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(200, response.statusCode());
        JsonObject updated = response.bodyAsJsonObject();
        assertEquals(1, updated.getInteger("id"));
        assertEquals("Updated Name", updated.getString("name"));
        assertEquals("First item", updated.getString("description")); // kept original description
        testContext.completeNow();
      })));
  }

  @Test
  void test_delete_item(VertxTestContext testContext) {
    client.delete(8888, "localhost", "/items/2")
      .send()
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(200, response.statusCode());
        JsonObject msg = response.bodyAsJsonObject();
        assertEquals("Item deleted", msg.getString("message"));
        testContext.completeNow();
      })));
  }
}
