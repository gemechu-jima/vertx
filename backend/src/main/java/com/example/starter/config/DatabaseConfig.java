package com.example.starter.config;

import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.mysqlclient.MySQLBuilder;

public class DatabaseConfig {

    public static Pool getClient(Vertx vertx) {

        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
            .setHost("localhost")
            .setPort(3306)
            .setDatabase("vertxdb")
            .setUser("app_user")
            .setPassword("1234");

        PoolOptions poolOptions = new PoolOptions()
            .setMaxSize(5);

        return MySQLBuilder
            .pool()
            .with(poolOptions)
            .connectingTo(connectOptions)
            .using(vertx)
            .build();
    }
}