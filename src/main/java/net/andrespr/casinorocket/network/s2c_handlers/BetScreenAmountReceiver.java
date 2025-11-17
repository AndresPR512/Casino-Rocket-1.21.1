package net.andrespr.casinorocket.network.s2c_handlers;

import net.andrespr.casinorocket.network.s2c.SendBetAmountS2CPayload;
import net.andrespr.casinorocket.screen.custom.BetScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class BetScreenAmountReceiver {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                SendBetAmountS2CPayload.ID,
                (payload, context) -> {
                    long amount = payload.amount();

                    MinecraftClient.getInstance().execute(() -> {
                        if (MinecraftClient.getInstance().currentScreen instanceof BetScreen screen) {
                            screen.updateTotalAmount(amount);
                        }
                    });
                }
        );
    }

}

