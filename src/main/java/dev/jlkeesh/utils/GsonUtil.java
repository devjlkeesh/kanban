package dev.jlkeesh.utils;

import com.google.gson.Gson;
import dev.jlkeesh.config.ApplicationConfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class GsonUtil {
    private static final Gson GSON = ApplicationConfig.GSON;

    private GsonUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String objectToJson(Object obj) {
        Objects.requireNonNull(obj, "serializing object can not be null");
        return GSON.toJson(obj);
    }

    public static byte[] jsonStringToByteArray(String inputText) {
        Objects.requireNonNull(inputText, "object can not be null");
        return inputText.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] objectToByteArray(Object obj) {
        Objects.requireNonNull(obj, "serializing object can not be null");
        return jsonStringToByteArray(objectToJson(obj));
    }

    public static <T> T fromJson(InputStream is, Class<T> clazz) {
        try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return GSON.fromJson(reader, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing JSON", e);
        }
    }

}
