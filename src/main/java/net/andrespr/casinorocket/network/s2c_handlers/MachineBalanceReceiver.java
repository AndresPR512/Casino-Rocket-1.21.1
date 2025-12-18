package net.andrespr.casinorocket.network.s2c_handlers;

import net.andrespr.casinorocket.network.s2c.SendMachineBalanceS2CPayload;
import net.andrespr.casinorocket.screen.custom.common.BetScreen;
import net.andrespr.casinorocket.screen.custom.common.WithdrawScreen;
import net.andrespr.casinorocket.screen.custom.common.WithdrawScreenHandler;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreen;
// import net.andrespr.casinorocket.screen.custom.blackjack.BlackjackTableScreen;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.List;

public final class MachineBalanceReceiver {

    private MachineBalanceReceiver() {}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                SendMachineBalanceS2CPayload.ID,
                (payload, context) -> {

                    String machineKey = payload.machineKey();
                    long amount = payload.amount();

                    MinecraftClient.getInstance().execute(() -> {

                        MinecraftClient client = MinecraftClient.getInstance();
                        if (client == null) return;

                        // === BET SCREEN ===
                        if (client.currentScreen instanceof BetScreen betScreen) {
                            betScreen.updateTotalAmount(amount);
                            return;
                        }

                        // === WITHDRAW SCREEN ===
                        if (client.currentScreen instanceof WithdrawScreen withdrawScreen) {
                            withdrawScreen.updateBalance(amount);
                            WithdrawScreenHandler handler = withdrawScreen.getScreenHandler();

                            if (amount == 0) {
                                handler.clearWithdrawInventory();
                                return;
                            }

                            List<ItemStack> stacks = MoneyCalculator.calculateChipWithdraw(amount);
                            handler.loadStacksIntoSlots(stacks);
                            return;
                        }

                        // === SPECIFIC SCREENS ===
                        switch (machineKey) {
                            case "slots" -> {
                                if (client.currentScreen instanceof SlotMachineScreen slotScreen) {
                                    if (!slotScreen.isSpinning()) {
                                        slotScreen.updateBalance(amount);
                                    }
                                }
                            }
                            /* case "blackjack" -> {
                                if (client.currentScreen instanceof BlackjackTableScreen bjScreen) {
                                    bjScreen.updateBalance(amount);
                                }
                            } */
                        }

                    });
                }
        );
    }

}