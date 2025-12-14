package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.c2s.DoWithdrawC2SPayload;
import net.andrespr.casinorocket.network.s2c.sender.SlotBalanceSender;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class DoWithdrawReceiver {

    public static void handle(DoWithdrawC2SPayload packet, ServerPlayNetworking.Context ctx) {

        ServerPlayerEntity player = ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUuid();

        long balance = storage.getBalance(uuid);
        if (balance <= 0) return;

        var stacks = MoneyCalculator.calculateChipWithdraw(balance);

        for (ItemStack stack : stacks) {
            if (!player.getInventory().insertStack(stack)) {
                player.dropItem(stack, false);
            }
        }

        storage.setBalance(uuid, 0);
        CasinoRocket.LOGGER.info("[SlotMachine] User {} withdrew {}", player.getGameProfile().getName(), balance);

        SlotBalanceSender.send(player, 0);
    }
}