package dev.jlkeesh.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class AbstractController<S> implements HttpHandler {
    protected final S service;

    protected AbstractController(S service) {
        this.service = service;
    }

    protected abstract void doPost(HttpExchange http) throws IOException;

    protected abstract void doPut(HttpExchange http) throws IOException;

    protected abstract void doGet(HttpExchange http) throws IOException;

    protected abstract void doDelete(HttpExchange http) throws IOException;

    protected void doUnhandled(HttpExchange http) throws IOException {

    }
}
