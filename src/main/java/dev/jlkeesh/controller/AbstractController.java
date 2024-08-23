package dev.jlkeesh.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.jlkeesh.config.HandlerAdvice;
import dev.jlkeesh.dto.BaseErrorDto;
import dev.jlkeesh.exception.ServiceException;
import dev.jlkeesh.utils.ExceptionUtil;
import lombok.extern.java.Log;

import java.io.IOException;

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
                case "GET" -> doGet(http);
                case "POST" -> doPost(http);
                case "PUT" -> doPut(http);
                case "DELETE" -> doDelete(http);
                default -> doUnhandled(http);
            }
        } catch (ServiceException e) {
            HandlerAdvice.handlerAdvice(http, new BaseErrorDto(e.getMessage()), e.getCode());
            log.severe(ExceptionUtil.getStackTrace(e));
        } catch (Exception e) {
            HandlerAdvice.handlerAdvice(http, new BaseErrorDto("internal server error"), 500);
            log.severe(ExceptionUtil.getStackTrace(e));
        }
    }

    protected abstract void doPost(HttpExchange http) throws IOException;

    protected abstract void doPut(HttpExchange http) throws IOException;

    protected abstract void doGet(HttpExchange http) throws IOException;

    protected abstract void doDelete(HttpExchange http) throws IOException;

    protected void doUnhandled(HttpExchange http) throws IOException {
        HandlerAdvice.handlerAdvice(http, new BaseErrorDto("bad request"), 400);
    }

}
