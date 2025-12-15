package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.slot.SlotsBetLogic;
import net.andrespr.casinorocket.network.c2s.DoBetC2SPayload;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class DoBetReceiver {

    public static void handle(DoBetC2SPayload payload, ServerPlayNetworking.Context context) {

        ServerPlayerEntity player = context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.currentScreenHandler instanceof IMachineBoundHandler bound)) return;
        if (!payload.pos().equals(bound.getMachinePos())) return;
        if (!payload.machineKey().equals(bound.getMachineKey())) return;

        switch (payload.machineKey()) {
            case "slots" -> SlotsBetLogic.handle(player, server);
            // case "blackjack" -> BlackjackBetLogic.handle(player, server);
            default -> CasinoRocket.LOGGER.warn("[Bet] Unknown machineKey={} from {}", payload.machineKey(), player.getGameProfile().getName());
        }
    }

}