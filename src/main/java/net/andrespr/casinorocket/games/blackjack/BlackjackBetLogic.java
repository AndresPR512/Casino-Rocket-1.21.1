package net.andrespr.casinorocket.games.blackjack;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.network.s2c.sender.MachineBalanceSender;
import net.andrespr.casinorocket.screen.custom.common.BetScreenHandler;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public final class BlackjackBetLogic {

    private BlackjackBetLogic() {}

    public static void handle(ServerPlayerEntity player, MinecraftServer server) {
        if (!(player.currentScreenHandler instanceof BetScreenHandler handler)) return;

        long amount = handler.getTotalMoney();
        if (amount <= 0) return;

        SimpleInventory inventory = handler.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            inventory.setStack(i, ItemStack.EMPTY);
        }

        PlayerBlackjackData data = PlayerBlackjackData.get(Objects.requireNonNull(server));
        data.addBalance(player.getUuid(), amount);
        data.addTotalDeposited(player.getUuid(), amount);

        CasinoRocket.LOGGER.info("[Blackjack] User {} deposited {}", player.getGameProfile().getName(), amount);

        MachineBalanceSender.send(player, "blackjack", data.getBalance(player.getUuid()));

        handler.onContentChanged(inventory);
    }

}