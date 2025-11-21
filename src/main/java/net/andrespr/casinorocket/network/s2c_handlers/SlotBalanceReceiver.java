package net.andrespr.casinorocket.network.s2c_handlers;

import net.andrespr.casinorocket.network.s2c.SendSlotBalanceS2CPayload;
import net.andrespr.casinorocket.screen.custom.BetScreen;
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
                        var client = MinecraftClient.getInstance();

                        if (client.currentScreen instanceof BetScreen betScreen) {
                            betScreen.updateTotalAmount(amount);

                        } else if (client.currentScreen instanceof SlotMachineScreen slotScreen) {
                            if (!slotScreen.isSpinning()) {
                                slotScreen.updateBalance(amount);
                            }

                        } else if (client.currentScreen instanceof WithdrawScreen withdrawScreen) {
                            withdrawScreen.updateBalance(amount);
                            WithdrawScreenHandler handler = withdrawScreen.getScreenHandler();

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