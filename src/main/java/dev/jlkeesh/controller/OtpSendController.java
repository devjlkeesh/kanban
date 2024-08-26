package dev.jlkeesh.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.jlkeesh.config.HandlerAdvice;
import dev.jlkeesh.dto.BaseErrorDto;
import dev.jlkeesh.dto.BaseResponse;
import dev.jlkeesh.dto.auth.OtpSendDto;
import dev.jlkeesh.exception.ServiceException;
import dev.jlkeesh.service.UserService;
import dev.jlkeesh.utils.ExceptionUtil;
import dev.jlkeesh.utils.GsonUtil;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.OutputStream;

@Log
public class OtpSendController implements HttpHandler {
    private final UserService userService;

    public OtpSendController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange http) throws IOException {
        String method = http.getRequestMethod();
        try {
            if (method.equals("POST")) {
                OtpSendDto dto = GsonUtil.fromJson(http.getRequestBody(), OtpSendDto.class);
                boolean sent = userService.otpSend(dto);
                OutputStream os = http.getResponseBody();
                http.sendResponseHeaders(200, 0);
                BaseResponse<Boolean> response = new BaseResponse<>(sent);
                os.write(GsonUtil.objectToByteArray(response));
                os.close();
            } else {
                HandlerAdvice.handlerAdviceBadRequest(http);
            }
        } catch (
                ServiceException e) {
            HandlerAdvice.handlerAdvice(http, new BaseErrorDto(e.getMessage()), e.getCode());
            log.severe(ExceptionUtil.getStackTrace(e));
        } catch (Exception e) {
            HandlerAdvice.handlerAdvice(http, new BaseErrorDto("internal server error"), 500);
            log.severe(ExceptionUtil.getStackTrace(e));
        }
    }
}
