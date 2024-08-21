package dev.jlkeesh.controller;

import com.sun.net.httpserver.HttpExchange;
import dev.jlkeesh.criteria.UserCriteria;
import dev.jlkeesh.dto.UserCreateDto;
import dev.jlkeesh.dto.UserDto;
import dev.jlkeesh.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static dev.jlkeesh.config.ApplicationConfig.GSON;

public class UserController extends AbstractController<UserService> {

    protected UserController(UserService service) {
        super(service);
    }

    @Override
    public void handle(HttpExchange http) throws IOException {
        String requestMethod = http.getRequestMethod();
        switch (requestMethod) {
            case "GET" -> doGet(http);
            case "POST" -> doPost(http);
            case "PUT" -> doPut(http);
            case "DELETE" -> doDelete(http);
            default -> doUnhandled(http);
        }
    }

    @Override
    protected void doPost(HttpExchange http) throws IOException {
        http.sendResponseHeaders(200, 0);
        InputStream is = http.getRequestBody();
        UserCreateDto dto = GSON.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), UserCreateDto.class);
        Long id = service.create(dto);
        OutputStream os = http.getResponseBody();
        os.write(id.toString().getBytes());
        os.close();
    }

    @Override
    protected void doPut(HttpExchange http) throws IOException {
        http.sendResponseHeaders(200, 0);
    }

    @Override
    protected void doGet(HttpExchange http) throws IOException {
        http.sendResponseHeaders(200, 0);
        OutputStream os = http.getResponseBody();
        List<UserDto> users = service.getAll(new UserCriteria());
        String responseData = GSON.toJson(users);
        os.write(responseData.getBytes());
        os.close();
    }

    @Override
    protected void doDelete(HttpExchange http) throws IOException {
        http.sendResponseHeaders(200, 0);
    }

    @Override
    protected void doUnhandled(HttpExchange http) throws IOException {

    }
}
