package com.example.starter.controller;

import com.example.starter.service.LoginService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    public void login(RoutingContext ctx) {

        JsonObject body = ctx.body().asJsonObject();

        if (body == null) {
            ctx.response()
                .setStatusCode(400)
                .end(new JsonObject()
                    .put("error", "Invalid JSON")
                    .encode());

            return;
        }

        String username = body.getString("username");
        String password = body.getString("password");

        loginService.login(username, password)
            .onSuccess(result -> {

                ctx.response()
                    .putHeader("content-type", "application/json")
                    .end(result.encode());

            })
            .onFailure(err -> {
                     ctx.response()
                    .setStatusCode(401)
                    .end(new JsonObject()
                        .put("error", err.getMessage())
                        .encode());

            });
    }
  public void register(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        if (body == null) {
            ctx.response()
                .setStatusCode(400)
                .end(new JsonObject()
                    .put("error", "Invalid JSON")
                    .encode());
            return;
        }
        String username = body.getString("username");
        String password = body.getString("password");
        // Registration logic to be implemented
        loginService.register(username, password)
            .onSuccess(result -> {
                ctx.response()
                    .putHeader("content-type", "application/json")
                    .end(result.encode());
            })
            .onFailure(err -> {
                ctx.response()
                    .setStatusCode(501)
                    .end(new JsonObject()
                        .put("error", err.getMessage())
                        .encode());
            });
    }
}