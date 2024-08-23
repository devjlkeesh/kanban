package dev.jlkeesh.utils;

public final class StringUtil {
    private StringUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }
}
