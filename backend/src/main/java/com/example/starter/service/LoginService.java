package com.example.starter.service;
import com.example.starter.model.User;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class LoginService {

    // database client
    private final Pool client;
    // constructor
    public LoginService(Pool client) {
        // initialize database client
        this.client = client;
    }

    public Future<JsonObject> login(User user) {

        Promise<JsonObject> promise = Promise.promise();

        client.preparedQuery(
                "SELECT * FROM users WHERE username = ? AND password = ?"
        )
        .execute(Tuple.of(user.getUsername(), user.getPassword()))
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
    Promise<JsonObject> promise = Promise.promise();
    
    // Step 1: Check if the username already exists
    client.preparedQuery("SELECT id FROM users WHERE username = ?")
        .execute(Tuple.of(username))
        .onSuccess(rows -> {
            if (rows.size() > 0) {
                // Username is already taken
                promise.complete(new JsonObject()
                    .put("status", "error")
                    .put("message", "Username is already taken"));
            } else {
                // Step 2: Username is unique, proceed with insertion
                client.preparedQuery("INSERT INTO users (username, password) VALUES (?, ?)")
                    .execute(Tuple.of(username, password))
                    .onSuccess(result -> {
                        promise.complete(new JsonObject()
                            .put("status", "success")
                            .put("message", "User registered successfully"));
                    })
                    .onFailure(err -> promise.fail(err.getMessage()));
            }
        })
        .onFailure(err -> promise.fail(err.getMessage()));

    return promise.future();
}

    public Future<JsonObject> getAllUsers() {
    Promise<JsonObject> promise = Promise.promise();

    client.preparedQuery("SELECT id, username FROM users")
        .execute()
        .onSuccess(rows -> {
            // Convert RowSet into a valid JsonArray
            JsonArray usersArray = rowsToJsonArray(rows); 

            JsonObject response = new JsonObject()
                .put("status", "success")
                .put("users", usersArray); // Pass the JSON array here
                
            promise.complete(response);
        })
        .onFailure(err -> {
            promise.fail(err.getMessage());
        });

    return promise.future();
}
private JsonArray rowsToJsonArray(RowSet<Row> rowSet) {
        JsonArray array = new JsonArray();
        for (Row row : rowSet) {
            JsonObject json = new JsonObject();
            for (int i = 0; i < row.size(); i++) {
                json.put(row.getColumnName(i), row.getValue(i));
            }
            array.add(json);
        }
        return array;
    }
}