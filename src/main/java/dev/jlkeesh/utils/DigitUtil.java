package dev.jlkeesh.utils;

import java.util.concurrent.ThreadLocalRandom;

public final class DigitUtil {
    private DigitUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Long generateNumber(double digitCount) {
        double v = ThreadLocalRandom.current().nextDouble(Math.pow(10, digitCount - 1), Math.pow(10, digitCount));
        return Math.round(v);
    }
}
