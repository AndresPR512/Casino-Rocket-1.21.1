package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.games.slot.SlotSpinEngine;
import net.andrespr.casinorocket.games.slot.SlotSpinResult;
import net.andrespr.casinorocket.network.c2s.DoSpinC2SPayload;
import net.andrespr.casinorocket.network.s2c.SendSpinResultS2CPayload;
import net.andrespr.casinorocket.screen.custom.SlotMachineScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class DoSpinReceiver {

    public static void handle(DoSpinC2SPayload payload, ServerPlayNetworking.Context context) {

        ServerPlayerEntity player = context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.currentScreenHandler instanceof SlotMachineScreenHandler)) {
            return;
        }

        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUuid();

        long balance = storage.getBalance(uuid);
        int betBase = storage.getBetBase(uuid);
        int linesMode = storage.getLinesMode(uuid);

        if (betBase <= 0 || linesMode <= 0) return;

        long cost = (long) betBase * linesMode;
        if (balance < cost) return;

        long afterCost = balance - cost;

        SlotSpinResult spinResult = SlotSpinEngine.spinAndEvaluate(betBase, linesMode);

        long newBalance = afterCost + spinResult.totalWin();
        storage.setBalance(uuid, newBalance);

        ServerPlayNetworking.send(player, SendSpinResultS2CPayload.from(newBalance, spinResult));
    }

}