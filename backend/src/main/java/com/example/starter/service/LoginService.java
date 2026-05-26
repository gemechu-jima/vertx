package com.example.starter.service;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Tuple;

public class LoginService {

    // database client
    private final Pool client;
    // constructor
    public LoginService(Pool client) {
        // initialize database client
        this.client = client;
    }

    public Future<JsonObject> login(String username, String password) {

        Promise<JsonObject> promise = Promise.promise();

        client.preparedQuery(
                "SELECT * FROM users WHERE username = ? AND password = ?"
        )
        .execute(Tuple.of(username, password))
        .onSuccess(rows -> {

            if (rows.size() > 0) {

                JsonObject response = new JsonObject()
                        .put("status", "success")
                        .put("message", "Login successful");

                promise.complete(response);

            } else {

                promise.fail("Invalid username or password");
            }

        })
        .onFailure(err -> {

            promise.fail(err.getMessage());

        });

        return promise.future();
    }

    public Future<JsonObject> register(String username, String password) {
        // Registration logic to be implemented
        return Future.failedFuture("Registration not implemented");
    }
}