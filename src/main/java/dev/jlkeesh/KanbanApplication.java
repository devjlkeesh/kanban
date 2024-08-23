package dev.jlkeesh;

import com.sun.net.httpserver.HttpServer;
import dev.jlkeesh.controller.UserController;
import dev.jlkeesh.dao.UserDao;
import dev.jlkeesh.dao.impl.PostgresUserDao;
import dev.jlkeesh.mapper.app.UserMapper;
import dev.jlkeesh.mapper.db.PostgresUserRowMapper;
import dev.jlkeesh.mapper.db.UserRowMapper;
import dev.jlkeesh.properties.DatabaseProperties;
import dev.jlkeesh.service.UserService;
import dev.jlkeesh.service.impl.RestUserService;
import dev.jlkeesh.utils.UserSession;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;

@Log
public class KanbanApplication {
    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection(
                DatabaseProperties.url,
                DatabaseProperties.username,
                DatabaseProperties.password
        );
        UserSession userSession = new UserSession();
        UserRowMapper userRowMapper = new PostgresUserRowMapper();
        UserMapper userMapper = new UserMapper();

        UserDao userDao = new PostgresUserDao(connection, userRowMapper, userSession);
        UserService userService = new RestUserService(userDao, userMapper);
        UserController userController = new UserController(userService);

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(9090), 0);
        httpServer.createContext("/users", userController);
        httpServer.setExecutor(Executors.newSingleThreadExecutor());
        httpServer.start();
        log.info("Kanban application started");
    }
}