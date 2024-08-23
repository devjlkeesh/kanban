package dev.jlkeesh.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.jlkeesh.config.HandlerAdvice;
import dev.jlkeesh.dto.BaseErrorDto;
import dev.jlkeesh.dto.BaseResponse;
import dev.jlkeesh.dto.auth.LoginDto;
import dev.jlkeesh.service.UserService;
import dev.jlkeesh.utils.GsonUtil;

import java.io.IOException;
import java.io.OutputStream;

public class AuthLoginController implements HttpHandler {
    private final UserService userService;

    public AuthLoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange http) throws IOException {
/*      String token = http.getRequestHeaders().getFirst("Authorization");
        String decrypt = AESUtil.decrypt(token);
        decrypt.split(":");
        SessionUserDto sessionUserDto = new SessionUserDto();
        ThreadLocal*/
        if (http.getRequestMethod().equals("POST")) {
            LoginDto dto = GsonUtil.fromJson(http.getRequestBody(), LoginDto.class);
            String token = userService.login(dto);
            BaseResponse<String> response = new BaseResponse<>(token);
            OutputStream os = http.getResponseBody();
            http.sendResponseHeaders(200, 0);
            os.write(GsonUtil.objectToByteArray(response));
            os.close();
        } else {
            HandlerAdvice.handlerAdvice(http, new BaseErrorDto("bad request"), 400);
        }
    }
}
