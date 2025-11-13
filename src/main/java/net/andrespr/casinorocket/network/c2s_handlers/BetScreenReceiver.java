package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.network.c2s.OpenBetScreenC2SPayload;
import net.andrespr.casinorocket.screen.custom.BetScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class BetScreenReceiver {

    public static void openBetScreen(OpenBetScreenC2SPayload payload, ServerPlayNetworking.Context context) {

        ServerPlayerEntity player = context.player();

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, inv, p) -> new BetScreenHandler(syncId, inv),
                Text.literal("Bet Menu")
        ));

    }

}