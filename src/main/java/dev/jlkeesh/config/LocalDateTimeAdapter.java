package dev.jlkeesh.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime value) throws IOException {
        if (value == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(value.format(formatter));
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        if (value != null) {
            return LocalDateTime.parse(value, formatter);
        }
        return null;
    }
}
