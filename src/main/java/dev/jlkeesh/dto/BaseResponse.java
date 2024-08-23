package dev.jlkeesh.dto;

public record BaseResponse<T>(T data, BaseErrorDto error, boolean success) {

    public BaseResponse(T data) {
        this(data, null, true);
    }

    public BaseResponse(BaseErrorDto error) {
        this(null, error, false);
    }
}
