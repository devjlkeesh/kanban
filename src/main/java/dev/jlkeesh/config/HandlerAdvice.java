package dev.jlkeesh.config;

import com.sun.net.httpserver.HttpExchange;
import dev.jlkeesh.dto.BaseErrorDto;
import dev.jlkeesh.dto.BaseResponse;
import dev.jlkeesh.utils.GsonUtil;

import java.io.IOException;
import java.io.OutputStream;

public final class HandlerAdvice {
    public static void handlerAdvice(HttpExchange http, BaseErrorDto error, int code) throws IOException {
        OutputStream os = http.getResponseBody();
        http.sendResponseHeaders(code, 0);
        BaseResponse<Void> response = new BaseResponse<>(error);
        os.write(GsonUtil.objectToByteArray(response));
        os.close();
    }
}
