package dev.jlkeesh.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

public class ApplicationConfig {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
}
