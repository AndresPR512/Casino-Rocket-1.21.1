package net.andrespr.casinorocket.util;

import net.minecraft.util.Formatting;

import java.util.Locale;

public class TextUtils {

    public static String formatCompact(long number) {
        String formatted;
        if (number >= 1_000_000_000_000_000_000L) formatted = String.format("%.1fQi", number / 1_000_000_000_000_000_000.0);
        else if (number >= 1_000_000_000_000_000L) formatted = String.format("%.1fQ", number / 1_000_000_000_000_000.0);
        else if (number >= 1_000_000_000_000L) formatted = String.format("%.1fT", number / 1_000_000_000_000.0);
        else if (number >= 1_000_000_000) formatted = String.format("%.1fB", number / 1_000_000_000.0);
        else if (number >= 1_000_000) formatted = String.format("%.1fM", number / 1_000_000.0);
        else if (number >= 1_000) formatted = String.format("%.1fK", number / 1_000.0);
        else formatted = String.valueOf(number);
        return formatted.replaceAll("\\.0(?=[A-Za-z]+)", "");
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

}