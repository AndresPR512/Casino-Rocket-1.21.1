package net.andrespr.casinorocket.network.s2c_handlers;

import net.andrespr.casinorocket.network.s2c.SendSlotBalanceS2CPayload;
import net.andrespr.casinorocket.screen.custom.SlotMachineScreen;
import net.andrespr.casinorocket.screen.custom.WithdrawScreen;
import net.andrespr.casinorocket.screen.custom.WithdrawScreenHandler;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SlotBalanceReceiver {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                SendSlotBalanceS2CPayload.ID,
                (payload, context) -> {
                    long amount = payload.amount();

                    MinecraftClient.getInstance().execute(() -> {
                        if (MinecraftClient.getInstance().currentScreen instanceof SlotMachineScreen screen) {
                            screen.updateBalance(amount);
                        } else if (MinecraftClient.getInstance().currentScreen instanceof WithdrawScreen screen) {
                            screen.updateBalance(amount);
                            WithdrawScreenHandler handler = screen.getScreenHandler();

                            if (amount == 0) {
                                handler.clearWithdrawInventory();
                                return;
                            }

                            List<ItemStack> stacks = MoneyCalculator.calculateChipWithdraw(amount);
                            handler.loadStacksIntoSlots(stacks);
                        }
                    });
                }
        );
    }

}