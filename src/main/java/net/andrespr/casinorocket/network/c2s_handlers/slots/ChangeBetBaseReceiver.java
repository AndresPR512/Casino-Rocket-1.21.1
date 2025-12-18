package net.andrespr.casinorocket.network.c2s_handlers.slots;

import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.c2s.slots.ChangeBetBaseC2SPayload;
import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.games.slot.SlotMachineConstants;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class ChangeBetBaseReceiver {

    public static void handle(ChangeBetBaseC2SPayload payload, ServerPlayNetworking.Context ctx) {

        ServerPlayerEntity player = ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUuid();

        int current = storage.getBetBase(uuid);

        int index = SlotMachineConstants.BET_VALUES.indexOf(current);
        if (index == -1) index = 0;

        if (payload.increase()) {
            if (index < SlotMachineConstants.BET_VALUES.size() - 1) index++;
        } else {
            if (index > 0) index--;
        }

        int newBase = SlotMachineConstants.BET_VALUES.get(index);
        storage.setBetBase(uuid, newBase);

        long balance = storage.getBalance(uuid);
        int lines = storage.getLinesMode(uuid);

        ServerPlayNetworking.send(player, new SendMenuSettingsS2CPayload(balance, newBase, lines));
    }

}