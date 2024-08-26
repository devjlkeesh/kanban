package dev.jlkeesh;

import com.sun.net.httpserver.HttpServer;
import dev.jlkeesh.controller.AuthLoginController;
import dev.jlkeesh.controller.OtpConfirmController;
import dev.jlkeesh.controller.OtpSendController;
import dev.jlkeesh.controller.UserController;
import dev.jlkeesh.dao.OtpDao;
import dev.jlkeesh.dao.UserDao;
import dev.jlkeesh.dao.impl.PostgresOtpDao;
import dev.jlkeesh.dao.impl.PostgresUserDao;
import dev.jlkeesh.mapper.app.UserMapper;
import dev.jlkeesh.mapper.db.OtpRowMapper;
import dev.jlkeesh.mapper.db.UserRowMapper;
import dev.jlkeesh.mapper.db.impl.PostgresOtpRowMapper;
import dev.jlkeesh.mapper.db.impl.PostgresUserRowMapper;
import dev.jlkeesh.properties.DatabaseProperties;
import dev.jlkeesh.service.UserService;
import dev.jlkeesh.service.impl.MailService;
import dev.jlkeesh.service.impl.RestUserService;
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
        UserRowMapper userRowMapper = new PostgresUserRowMapper();
        UserMapper userMapper = new UserMapper();
        OtpRowMapper otpRowMapper = new PostgresOtpRowMapper();

        MailService mailService = new MailService();

        UserDao userDao = new PostgresUserDao(connection, userRowMapper);
        OtpDao otpDao = new PostgresOtpDao(connection, otpRowMapper);
        UserService userService = new RestUserService(userDao, otpDao, userMapper, mailService);

        UserController userController = new UserController(userService);
        AuthLoginController authLoginController = new AuthLoginController(userService);
        OtpSendController otpSendController = new OtpSendController(userService);
        OtpConfirmController otpConfirmController = new OtpConfirmController(userService);


        HttpServer httpServer = HttpServer.create(new InetSocketAddress(9090), 0);
        httpServer.createContext("/users", userController);
        httpServer.createContext("/auth/login", authLoginController);
        httpServer.createContext("/otp/send", otpSendController);
        httpServer.createContext("/otp/confirm", otpConfirmController);
        httpServer.setExecutor(Executors.newSingleThreadExecutor());
        httpServer.start();
        log.info("Kanban application started");
    }
}