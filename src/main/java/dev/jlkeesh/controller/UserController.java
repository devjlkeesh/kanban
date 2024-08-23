package dev.jlkeesh.controller;

import com.sun.net.httpserver.HttpExchange;
import dev.jlkeesh.criteria.UserCriteria;
import dev.jlkeesh.dto.BaseErrorDto;
import dev.jlkeesh.dto.BaseResponse;
import dev.jlkeesh.dto.user.UserCreateDto;
import dev.jlkeesh.dto.user.UserDto;
import dev.jlkeesh.exception.ServiceException;
import dev.jlkeesh.service.UserService;
import dev.jlkeesh.utils.GsonUtil;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static dev.jlkeesh.config.ApplicationConfig.GSON;

@Log
public class UserController extends AbstractController<UserService> {

    public UserController(UserService service) {
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
        OutputStream os = http.getResponseBody();
        try {
            InputStream is = http.getRequestBody();
            UserCreateDto dto = GSON.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), UserCreateDto.class);
            Long id = service.create(dto);
            http.sendResponseHeaders(200, 0);
            BaseResponse<Long> response = new BaseResponse<>(id);
            os.write(GsonUtil.objectToByteArray(response));
            os.close();
        } catch (ServiceException e) {
            http.sendResponseHeaders(e.getCode(), 0);
            BaseErrorDto error = new BaseErrorDto(e.getMessage());
            BaseResponse<Long> response = new BaseResponse<>(error);
            os.write(GsonUtil.objectToByteArray(response));
            os.close();
            e.printStackTrace();
        } catch (Exception e) {
            http.sendResponseHeaders(500, 0);
            BaseErrorDto error = new BaseErrorDto("internal service error");
            BaseResponse<Long> response = new BaseResponse<>(error);
            os.write(GsonUtil.objectToByteArray(response));
            os.close();
            e.printStackTrace();
        }
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
