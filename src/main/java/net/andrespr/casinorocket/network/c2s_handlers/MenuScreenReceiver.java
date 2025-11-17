package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.network.c2s.OpenMenuScreenC2SPayload;
import net.andrespr.casinorocket.screen.custom.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class MenuScreenReceiver {

    public static void openMenuScreen(OpenMenuScreenC2SPayload payload, ServerPlayNetworking.Context context) {

        ServerPlayerEntity player = context.player();

        if (player.currentScreenHandler instanceof SlotMachineScreenHandler) {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, p) -> new SlotMachineMenuScreenHandler(syncId, inv),
                    Text.literal("Slot Machine Menu")
            ));
        }

    }

}