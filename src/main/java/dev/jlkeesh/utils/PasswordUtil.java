package dev.jlkeesh.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public final class PasswordUtil {

    private PasswordUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String hash(String password) {
        Objects.requireNonNull(password, "password cannot be null");
        return BCrypt.hashpw(password, BCrypt.gensalt(8));
    }

    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        Objects.requireNonNull(rawPassword, "password cannot be null");
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
