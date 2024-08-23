package dev.jlkeesh.utils;

import lombok.NonNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtil {
    private ExceptionUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getStackTrace(@NonNull Throwable throwable) {
        StringWriter out = new StringWriter();
        throwable.printStackTrace(new PrintWriter(out, true));
        return out.toString();
    }
}
