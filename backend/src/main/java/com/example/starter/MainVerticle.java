package com.example.starter;

import com.example.starter.config.DatabaseConfig;
import com.example.starter.controller.LoginController;
import com.example.starter.service.LoginService;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.Vertx;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import io.vertx.sqlclient.Pool;

public class MainVerticle extends VerticleBase {

    @Override
    public Future<?> start() {

        // Create database pool

        Pool client = DatabaseConfig.getClient(vertx);

        // Create service

        LoginService loginService = new LoginService(client);

        // Create controller

        LoginController loginController =new LoginController(loginService);

        // Router

        Router router = Router.router(vertx);

        // Enable JSON body

        router.route().handler(BodyHandler.create());

        // Routes

        router.post("/login").handler(loginController::login);
        router.post("/register").handler(loginController::register);
        router.get("/users").handler(loginController::getAllUsers);

        // Start server

        return vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888)
                .onSuccess(server -> {

                    System.out.println(
                            "Server started on port 8888"
                    );

                });
    }
    public static void main(String[] args) {
      Vertx vertx = Vertx.vertx();
      vertx.deployVerticle(new MainVerticle());
      System.out.println("Starting Vert.x application...");
    }
  }