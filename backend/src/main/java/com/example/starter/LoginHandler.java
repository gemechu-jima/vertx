package com.example.starter;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Tuple;

public class LoginHandler {

    private final Pool client;

    public LoginHandler(Pool client) {
        this.client = client;
    }

    public void loginUser(RoutingContext ctx) {

        ctx.request().bodyHandler(buffer -> {

        JsonObject requestBody;

        try {
            requestBody = buffer.toJsonObject();
            System.out.println("Received login request: " + requestBody.encode());

        } catch (Exception e) {
            ctx.response()
                .setStatusCode(400)
                .end(new JsonObject()
                    .put("error", "Invalid JSON")
                    .encode());
            return;
        }

        String username = requestBody.getString("username");
        String password = requestBody.getString("password");

        if (username == null || password == null) {
            ctx.response().setStatusCode(400)
                    .end(new JsonObject().put("error", "Missing fields").encode());
            return;
        }

        client.preparedQuery(
                "SELECT id FROM users WHERE username = ? AND password = ?")
                .execute(Tuple.of(username, password))
                .onSuccess(rows -> {

                    if (rows.size() > 0) {
                        ctx.response()
                                .setStatusCode(200)
                                .end(new JsonObject().put("status", "success").encode());
                    } else {
                        ctx.response()
                                .setStatusCode(401)
                                .end(new JsonObject().put("status", "error").encode());
                    }

                })
                .onFailure(err -> {
                    ctx.response()
                            .setStatusCode(500)
                            .end("Database error");
                });
        });
    }

    public void registerUser(RoutingContext ctx) {
        ctx.request().bodyHandler(buffer -> {

        JsonObject requestBody;

        try {
            requestBody = buffer.toJsonObject();
            System.out.println("Received registration request: " + requestBody.encode());

        } catch (Exception e) {
            ctx.response()
                .setStatusCode(400)
                .end(new JsonObject()
                    .put("error", "Invalid JSON")
                    .encode());
            return;
        }

        String username=requestBody.getString("username");
        String password=requestBody.getString("password");
        if(username==null || password==null){
            ctx.response().setStatusCode(400)
            .end(new JsonObject().put("error", "Missing fields")
            .encode());
        }
        client.preparedQuery(
                "INSERT INTO users (username, password) VALUES (?, ?)")
                .execute(Tuple.of(username, password))
                .onSuccess(res -> {
                    ctx.response()
                            .setStatusCode(201)
                            .end(new JsonObject().put("status", "registered").encode());
                })
                .onFailure(err -> {
                    ctx.response()
                            .setStatusCode(500)
                            .end("Database error");
                });
        });
    }
}