package com.example.starter.controller;

import com.example.starter.service.LoginService;
import com.example.starter.model.User;
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
        User user= new User();
        user.setUsername(body.getString("username"));
        user.setPassword(body.getString("password"));
       

        loginService.login(user)
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
    public void getAllUsers(RoutingContext context){
        loginService.getAllUsers()
        .onSuccess(result -> {
            context.response()
                .putHeader("content-type", "application/json")
                .end(result.encode());
        })
        .onFailure(err -> {
            context.response()
                .setStatusCode(500)
                .end(new JsonObject()
                    .put("error", err.getMessage())
                    .encode());
        });
    }
}