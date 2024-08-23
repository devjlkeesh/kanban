package dev.jlkeesh.controller;

import com.sun.net.httpserver.HttpExchange;
import dev.jlkeesh.service.ProjectService;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class ProjectController extends AbstractController<ProjectService> {

    protected ProjectController(ProjectService service) {
        super(service);
    }

    @Override
    protected void doPost(HttpExchange http) throws IOException {

    }

    @Override
    protected void doPut(HttpExchange http) throws IOException {

    }

    @Override
    protected void doGet(HttpExchange http) throws IOException {

    }

    @Override
    protected void doDelete(HttpExchange http) throws IOException {

    }

}
