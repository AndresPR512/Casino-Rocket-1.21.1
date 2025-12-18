package net.andrespr.casinorocket.network.s2c_handlers;

import net.andrespr.casinorocket.network.s2c.SendBlackjackStateS2CPayload;
import net.andrespr.casinorocket.screen.custom.blackjack.BlackjackTableScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public final class BlackjackStateReceiver {

    private BlackjackStateReceiver() {}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                SendBlackjackStateS2CPayload.ID,
                (payload, ctx) -> MinecraftClient.getInstance().execute(() -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.currentScreen instanceof BlackjackTableScreen screen) {
                        screen.applyState(payload);
                    }
                })
        );
    }

}