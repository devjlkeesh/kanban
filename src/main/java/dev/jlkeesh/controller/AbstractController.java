package dev.jlkeesh.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.jlkeesh.dto.BaseErrorDto;
import dev.jlkeesh.dto.BaseResponse;
import dev.jlkeesh.exception.ServiceException;
import dev.jlkeesh.utils.ExceptionUtil;
import dev.jlkeesh.utils.GsonUtil;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Log
public abstract class AbstractController<S> implements HttpHandler {
    protected final S service;

    protected AbstractController(S service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange http) throws IOException {
        try {
            String requestMethod = http.getRequestMethod();
            switch (requestMethod) {
                case "GET" -> doGetProxy(http);
                case "POST" -> doPostProxy(http);
                case "PUT" -> doPut(http);
                case "DELETE" -> doDelete(http);
                default -> doUnhandled(http);
            }
        } catch (ServiceException e) {
            handlerAdvice(http, new BaseErrorDto(e.getMessage()), e.getCode());
            log.severe(ExceptionUtil.getStackTrace(e));
        } catch (Exception e) {
            handlerAdvice(http, new BaseErrorDto("internal server error"), 500);
            log.severe(ExceptionUtil.getStackTrace(e));
        }
    }

    private void doPostProxy(HttpExchange http) throws IOException {
        UUID traceId = UUID.randomUUID();
        log.info("UserController doPost. incoming request : " + traceId);
        doPost(http);
        log.info("UserController doPost. out coming request : " + traceId);
    }

    protected abstract void doPost(HttpExchange http) throws IOException;

    protected abstract void doPut(HttpExchange http) throws IOException;

    private void doGetProxy(HttpExchange http) throws IOException {
        UUID traceId = UUID.randomUUID();
        log.info("UserController doGet. incoming request : " + traceId);
        doGet(http);
        log.info("UserController doGet. out coming request : " + traceId);
    }

    protected abstract void doGet(HttpExchange http) throws IOException;

    protected abstract void doDelete(HttpExchange http) throws IOException;

    protected void doUnhandled(HttpExchange http) throws IOException {
        handlerAdvice(http, new BaseErrorDto("bad request"), 400);
    }

    private static void handlerAdvice(HttpExchange http, BaseErrorDto error, int code) throws IOException {
        OutputStream os = http.getResponseBody();
        http.sendResponseHeaders(code, 0);
        BaseResponse<Void> response = new BaseResponse<>(error);
        os.write(GsonUtil.objectToByteArray(response));
        os.close();
    }

}
