package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.c2s.OpenMenuScreenC2SPayload;
import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.screen.custom.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

public class MenuScreenReceiver {

    public static void openMenuScreen(OpenMenuScreenC2SPayload payload, ServerPlayNetworking.Context context) {

        ServerPlayerEntity player = context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (player.currentScreenHandler instanceof SlotMachineScreenHandler) {

            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, playerEntity) -> new SlotMachineMenuScreenHandler(syncId, inv),
                    Text.literal("Slot Machine Menu")
            ));

            PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
            UUID uuid = player.getUuid();

            long balance = storage.getBalance(uuid);
            int betBase = storage.getBetBase(uuid);
            int lines = storage.getLinesMode(uuid);

            ServerPlayNetworking.send(player, new SendMenuSettingsS2CPayload(balance, betBase, lines));
        }
    }

}
