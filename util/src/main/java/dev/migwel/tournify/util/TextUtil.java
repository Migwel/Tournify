package dev.migwel.tournify.util;

public final class TextUtil {

    private TextUtil() {
        //util
    }

    public static boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean hasText(String str) {
        return !isEmptyOrNull(str);
    }
}
