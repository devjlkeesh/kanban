package dev.jlkeesh.utils;

import com.sun.net.httpserver.HttpExchange;
import dev.jlkeesh.dto.BaseErrorDto;
import dev.jlkeesh.dto.BaseResponse;
import dev.jlkeesh.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.io.OutputStream;

public final class SecurityUtil {
    private SecurityUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String requireToken(HttpExchange http) {
        String token = http.getRequestHeaders().getFirst("Authorization");
        if (StringUtils.isBlank(token) || !AESUtil.isValidToken(token)) {
            throw new ServiceException("unauthorized", 401);
        }
        return token;
    }

    public static void initializeSecurityContext(HttpExchange http) {
        String token = requireToken(http);
        UserSession.set(AESUtil.decrypt(token));
    }
}
