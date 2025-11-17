package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.data.SlotMachineDataStorage;
import net.andrespr.casinorocket.network.c2s.DoWithdrawC2SPayload;
import net.andrespr.casinorocket.network.s2c.SendSlotBalanceS2CPayload;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class DoWithdrawReceiver {

    public static void handle(DoWithdrawC2SPayload packet, ServerPlayNetworking.Context ctx) {

        ServerPlayerEntity player = ctx.player();
        var server = player.getServer();
        if (server == null) return;

        var storage = SlotMachineDataStorage.get(server);
        var uuid = player.getUuid();

        long balance = storage.getBalance(uuid);
        if (balance <= 0) return;

        var stacks = MoneyCalculator.calculateChipWithdraw(balance);

        for (ItemStack stack : stacks) {
            if (!player.getInventory().insertStack(stack)) {
                player.dropItem(stack, false);
            }
        }

        storage.setBalance(uuid, 0);

        ServerPlayNetworking.send(player, new SendSlotBalanceS2CPayload(0));

    }

}