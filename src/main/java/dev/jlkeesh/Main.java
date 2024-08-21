package dev.jlkeesh;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import dev.jlkeesh.dao.UserDao;
import dev.jlkeesh.dao.impl.PostgresUserDao;
import dev.jlkeesh.mapper.db.PostgresUserRowMapper;
import dev.jlkeesh.properties.DatabaseProperties;
import dev.jlkeesh.utils.UserSession;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection(
                DatabaseProperties.url,
                DatabaseProperties.username,
                DatabaseProperties.password
        );
        UserSession userSession = new UserSession();
        UserDao userDao = new PostgresUserDao(connection, new PostgresUserRowMapper(), userSession);

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(9090), 0);
        httpServer.createContext("/auth_user", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream os = httpExchange.getResponseBody();
                os.write("Hello guys".getBytes());
                os.close();
            }
        });
        httpServer.setExecutor(Executors.newSingleThreadExecutor());
        httpServer.start();
    }
}