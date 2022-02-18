package com.theCherno.rain.util;

public class MathUtils {

    private MathUtils() {
    }

    public static int min (int min, int value) {
        return Math.max(value, min);
    }

    public static int max(int max, int value) {
        return Math.min(value, max);
    }
    public static int clamp(int value, int min, int max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }
}
