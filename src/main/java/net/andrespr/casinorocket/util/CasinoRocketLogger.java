package net.andrespr.casinorocket.util;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CasinoRocketLogger {

    public enum LogLevel {
        INFO,
        WARN,
        ERROR
    }

    // DIRECT MESSAGE TO A PLAYER
    public static void toPlayer(PlayerEntity player, String message, boolean overlay) {
        player.sendMessage(Text.literal(message), overlay);
    }

    // DIRECT TRANSLATED MESSAGE TO A PLAYER
    public static void toPlayerTranslated(PlayerEntity player, String translationKey, boolean overlay, Object... args) {
        player.sendMessage(Text.translatable(translationKey, args), overlay);
    }

    // INFO LOG
    public static void info(String message, Object... args) {
        CasinoRocket.LOGGER.info(message, args);
    }

    // WARN LOG
    public static void warn(String message, Object... args) {
        CasinoRocket.LOGGER.warn(message, args);
    }

    // ERROR LOG
    public static void error(String message, Object... args) {
        CasinoRocket.LOGGER.error(message, args);
    }

    // DEBUG MESSAGE
    public static void debug(String message) {
        if (CasinoRocket.LOGGER.isDebugEnabled()) {
            CasinoRocket.LOGGER.debug(message);
        }
    }

    // TO OPERATORS AND LOG
    public static void toOps(MinecraftServer server, LogLevel level, String message) {
        String formatted = "[CasinoRocket] " + message;
        server.getPlayerManager().getPlayerList().forEach(p -> {
            if (server.getPlayerManager().isOperator(p.getGameProfile())) {
                p.sendMessage(Text.literal(formatted), false);
            }
        });
        switch (level) {
            case INFO: info(message); break;
            case WARN: warn(message); break;
            case ERROR: error(message); break;
        }
    }

    // TO EVERYONE
    public static void broadcast(MinecraftServer server, String level, String message) {
        String formatted = "[CasinoRocket] " + message;
        server.getPlayerManager().broadcast(Text.literal(formatted), false);
        info("[Broadcast/" + level.toUpperCase() + "] " + message);
    }

}