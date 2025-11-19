package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.c2s.ChangeLinesModeC2SPayload;
import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class ChangeLinesModeReceiver {

    public static void handle(ChangeLinesModeC2SPayload packet, ServerPlayNetworking.Context ctx) {

        ServerPlayerEntity player = ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUuid();

        int mode = packet.mode();
        if (mode < 1 || mode > 3) return;

        storage.setLinesMode(uuid, mode);

        long balance = storage.getBalance(uuid);
        int base = storage.getBetBase(uuid);

        ServerPlayNetworking.send(player, new SendMenuSettingsS2CPayload(balance, base, mode));

    }

}