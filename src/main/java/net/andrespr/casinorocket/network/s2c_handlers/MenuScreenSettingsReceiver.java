package net.andrespr.casinorocket.network.s2c_handlers;

import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineMenuScreen;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class MenuScreenSettingsReceiver {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                SendMenuSettingsS2CPayload.ID,
                (payload, context) -> {
                    long balance = payload.balance();
                    int betBase = payload.betBase();
                    int linesMode = payload.linesMode();

                    MinecraftClient.getInstance().execute(() -> {
                        if (MinecraftClient.getInstance().currentScreen instanceof SlotMachineMenuScreen screen) {
                            screen.updateSettings(balance, betBase, linesMode);
                        } else if (MinecraftClient.getInstance().currentScreen instanceof SlotMachineScreen screen) {
                            screen.updateDisplay(balance, betBase, linesMode);
                        }
                    });
                }
        );
    }

}