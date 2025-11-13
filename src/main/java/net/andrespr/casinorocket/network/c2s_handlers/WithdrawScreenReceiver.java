package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.network.c2s.OpenWithdrawScreenC2SPayload;
import net.andrespr.casinorocket.screen.custom.WithdrawScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class WithdrawScreenReceiver {

    public static void openWithdrawScreen(OpenWithdrawScreenC2SPayload payload, ServerPlayNetworking.Context context) {

        ServerPlayerEntity player = context.player();

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, inv, p) -> new WithdrawScreenHandler(syncId, inv),
                Text.literal("Withdraw Menu")
        ));

    }

}