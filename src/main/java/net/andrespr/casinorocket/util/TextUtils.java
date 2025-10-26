package net.andrespr.casinorocket.util;

public class TextUtils {

    public static String formatCompact(long number) {
        String formatted;
        if (number >= 1_000_000_000) {
            formatted = String.format("%.1fB", number / 1_000_000_000.0);
        } else if (number >= 1_000_000) {
            formatted = String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000) {
            formatted = String.format("%.1fK", number / 1_000.0);
        } else {
            formatted = String.valueOf(number);
        }
        return formatted.replaceAll("\\.0([KMB])", "$1");
    }

}
