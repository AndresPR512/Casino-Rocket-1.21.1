package net.andrespr.casinorocket.games.blackjack;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.network.s2c.sender.MachineBalanceSender;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public final class BlackjackWithdrawLogic {

    private BlackjackWithdrawLogic() {}

    public static void handle(ServerPlayerEntity player, MinecraftServer server) {
        PlayerBlackjackData storage = PlayerBlackjackData.get(server);
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
        CasinoRocket.LOGGER.info("[Blackjack] User {} withdrew {}", player.getGameProfile().getName(), balance);

        MachineBalanceSender.send(player, "blackjack", 0);
    }

}