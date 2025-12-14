package net.andrespr.casinorocket.util;

import net.minecraft.util.Formatting;

import java.util.Locale;

public class TextUtils {

    public static String formatCompact(long number) {
        double value;
        String suffix;

        if (number >= 1_000_000_000_000_000_000L) {
            value = number / 1_000_000_000_000_000_000.0;
            suffix = "Qi";
        } else if (number >= 1_000_000_000_000_000L) {
            value = number / 1_000_000_000_000_000.0;
            suffix = "Q";
        } else if (number >= 1_000_000_000_000L) {
            value = number / 1_000_000_000_000.0;
            suffix = "T";
        } else if (number >= 1_000_000_000) {
            value = number / 1_000_000_000.0;
            suffix = "B";
        } else if (number >= 1_000_000) {
            value = number / 1_000_000.0;
            suffix = "M";
        } else if (number >= 1_000) {
            value = number / 1_000.0;
            suffix = "K";
        } else {
            return String.valueOf(number);
        }

        value = Math.floor(value * 10.0) / 10.0;

        String formatted = String.format("%.1f%s", value, suffix);
        return formatted.replace(".0", "");
    }


    public static String formatLarge(long number) {

        if (number < 1_000L) {
            return String.format("%,d", number);
        }

        if (number < 1_000_000L) {
            String promoted = tryPromote(number, 1_000L, "K");
            if (promoted != null) return promoted;

            return String.format("%,d", number);
        }

        if (number < 1_000_000_000L) {
            String promoted = tryPromote(number, 1_000_000L, "M");
            if (promoted != null) return promoted;

            long valueK = number / 1_000L;
            return String.format("%,dK", valueK);
        }

        if (number < 1_000_000_000_000L) {
            String promoted = tryPromote(number, 1_000_000_000L, "B");
            if (promoted != null) return promoted;

            long valueM = number / 1_000_000L;
            return String.format("%,dM", valueM);
        }

        if (number < 1_000_000_000_000_000L) {
            String promoted = tryPromote(number, 1_000_000_000_000L, "T");
            if (promoted != null) return promoted;

            long valueB = number / 1_000_000_000L;
            return String.format("%,dB", valueB);
        }

        long valueT = number / 1_000_000_000_000L;
        return String.format("%,dT", valueT);
    }

    private static String tryPromote(long number, long unit, String suffix) {
        if (number < unit) return null;

        if (number % unit == 0) {
            double value = number / (double) unit;
            return String.format(Locale.US, "%.1f%s", value, suffix);
        }

        long tenth = unit / 10L;
        if (number % tenth == 0) {
            double value = number / (double) unit;
            return String.format(Locale.US, "%.1f%s", value, suffix);
        }

        return null;
    }


    public static String formatCompactNoDecimal(long number) {
        if (number >= 1_000_000_000_000_000_000L) return number / 1_000_000_000_000_000_000L + "Qi";
        if (number >= 1_000_000_000_000_000L) return number / 1_000_000_000_000_000L + "Q";
        if (number >= 1_000_000_000_000L) return number / 1_000_000_000_000L + "T";
        if (number >= 1_000_000_000) return number / 1_000_000_000 + "B";
        if (number >= 1_000_000) return number / 1_000_000 + "M";
        if (number >= 1_000) return number / 1_000 + "K";
        return String.valueOf(number);
    }

    public static Formatting percentagesColor(double percentage) {
        if (percentage >= 5.01) {
            return Formatting.GREEN;
        } else if (percentage >= 1.01) {
            return Formatting.YELLOW;
        } else if (percentage >= 0.10) {
            return Formatting.RED;
        } else {
            return Formatting.DARK_RED;
        }
    }

    public static Formatting rarityColor(String rarity) {
        return switch (rarity.toLowerCase(Locale.ROOT)) {
            case "common", "bonus" -> Formatting.WHITE;
            case "uncommon" -> Formatting.BLUE;
            case "rare" -> Formatting.GOLD;
            case "ultrarare" -> Formatting.LIGHT_PURPLE;
            case "legendary" -> Formatting.RED;
            default -> Formatting.GRAY;
        };
    }

    public static Formatting coinColor(String rarity) {
        return switch (rarity.toLowerCase(Locale.ROOT)) {
            case "copper" -> Formatting.RED;
            case "iron" -> Formatting.WHITE;
            case "gold" -> Formatting.GOLD;
            case "diamond" -> Formatting.AQUA;
            default -> Formatting.GRAY;
        };
    }

    public static Formatting rankColors(int rank) {
        return switch (rank) {
            case 1 -> Formatting.GOLD;
            case 2 -> Formatting.AQUA;
            case 3 -> Formatting.GREEN;
            default -> Formatting.WHITE;
        };
    }

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

}