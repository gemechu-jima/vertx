package com.example.starter;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import io.vertx.sqlclient.Pool;
public class MainVerticle extends VerticleBase {

  private Map<Integer, JsonObject> items = new HashMap<>();
  private AtomicInteger idCounter = new AtomicInteger(1);

  @Override
  public Future<?> start() {
    addSampleData();
    JsonObject config = config(); 
    int port = config().getInteger("port", 8888);
    Router router = Router.router(vertx);
    Pool client = DatabaseConfig.getClient(vertx);
    LoginHandler loginHandler = new LoginHandler(client);

        // Enable CORS for Angular frontend
    router.route().handler(ctx -> {
      ctx.response()
        .putHeader("Access-Control-Allow-Origin", "*")
        .putHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        .putHeader("Access-Control-Allow-Headers", "Content-Type");
      
      if (ctx.request().method().name().equals("OPTIONS")) {
        ctx.response().setStatusCode(200).end();
      } else {
        ctx.next();
      }
    });
    // Test Database connection
  client.query("SELECT 1")
.execute()
.onSuccess(res -> {
    System.out.println("MySQL Connected OK");
})
.onFailure(err -> {
    System.out.println("MySQL Failed: " + err.getMessage());
});
    // CRUD routes
    router.get("/items").handler(this::listItems);
    router.get("/items/:id").handler(this::getItem);
    router.post("/items").handler(this::createItem);
    router.put("/items/:id").handler(this::updateItem);
    router.delete("/items/:id").handler(this::deleteItem);

    return vertx.createHttpServer()
      .requestHandler(router)
      .listen(port)
      .onSuccess(server -> {
        System.out.println("HTTP server started on port " + server.actualPort());
      });
  }

  // LIST all items
  private void listItems(RoutingContext ctx) {
    JsonArray itemsArray = new JsonArray();
    items.values().forEach(itemsArray::add);
    
    ctx.response()
      .putHeader("content-type", "application/json")
      .end(itemsArray.encodePrettily());
  }

  // READ one item
  private void getItem(RoutingContext ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));
      JsonObject item = items.get(id);
      
      if (item != null) {
        ctx.response()
          .putHeader("content-type", "application/json")
          .end(item.encodePrettily());
      } else {
        ctx.response().setStatusCode(404).end("Item not found");
      }
    } catch (NumberFormatException e) {
      ctx.response().setStatusCode(400).end("Invalid ID");
    }
  }

  // CREATE new item
  private void createItem(RoutingContext ctx) {
    ctx.request().bodyHandler(buffer -> {
      try {
        JsonObject body = new JsonObject(buffer.toString());
        int id = idCounter.getAndIncrement();
        JsonObject item = new JsonObject()
          .put("id", id)
          .put("name", body.getString("name"))
          .put("description", body.getString("description"));
        
        items.put(id, item);
        
        ctx.response()
          .setStatusCode(201)
          .putHeader("content-type", "application/json")
          .end(item.encodePrettily());
      } catch (Exception e) {
        ctx.response().setStatusCode(400).end("Invalid JSON");
      }
    });
  }

  // UPDATE item
  private void updateItem(RoutingContext ctx) {
    ctx.request().bodyHandler(buffer -> {
      try {
        int id = Integer.parseInt(ctx.pathParam("id"));
        JsonObject item = items.get(id);
        
        if (item != null) {
          JsonObject body = new JsonObject(buffer.toString());
          item.put("name", body.getString("name", item.getString("name")));
          item.put("description", body.getString("description", item.getString("description")));
          
          ctx.response()
            .putHeader("content-type", "application/json")
            .end(item.encodePrettily());
        } else {
          ctx.response().setStatusCode(404).end("Item not found");
        }
      } catch (Exception e) {
        ctx.response().setStatusCode(400).end("Invalid request");
      }
    });
  }

  // DELETE item
  private void deleteItem(RoutingContext ctx) {
    try {
      int id = Integer.parseInt(ctx.pathParam("id"));
      JsonObject deleted = items.remove(id);
      
      if (deleted != null) {
        ctx.response()
          .putHeader("content-type", "application/json")
          .end(new JsonObject().put("message", "Item deleted").encodePrettily());
      } else {
        ctx.response().setStatusCode(404).end("Item not found");
      }
    } catch (NumberFormatException e) {
      ctx.response().setStatusCode(400).end("Invalid ID");
    }
  }

  private void addSampleData() {
    items.put(1, new JsonObject().put("id", 1).put("name", "Item 1").put("description", "First item"));
    items.put(2, new JsonObject().put("id", 2).put("name", "Item 2").put("description", "Second item"));
    idCounter.set(3);
  }
  public static void main(String[] args) {
    io.vertx.core.Vertx vertx = io.vertx.core.Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
}
}
