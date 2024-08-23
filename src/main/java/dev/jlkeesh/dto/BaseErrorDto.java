package dev.jlkeesh.dto;

public record BaseErrorDto(String code, String message) {
    public BaseErrorDto(String message) {
        this(null, message);
    }
}
