package net.andrespr.casinorocket.network.c2s_handlers.blackjack;

import net.andrespr.casinorocket.block.entity.custom.BlackjackTableEntity;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.games.blackjack.BlackjackGameController;
import net.andrespr.casinorocket.games.blackjack.BlackjackPhase;
import net.andrespr.casinorocket.network.c2s.blackjack.ChangeBlackjackBetIndexC2SPayload;
import net.andrespr.casinorocket.network.s2c.sender.BlackjackStateSender;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class ChangeBlackjackBetIndexReceiver {

    private ChangeBlackjackBetIndexReceiver() {}

    public static void handle(ChangeBlackjackBetIndexC2SPayload payload, ServerPlayNetworking.Context ctx) {

        ServerPlayerEntity player = ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.currentScreenHandler instanceof IMachineBoundHandler bound)) return;
        if (!"blackjack".equals(bound.getMachineKey())) return;

        World world = player.getWorld();
        BlockPos pos = bound.getMachinePos();

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof BlackjackTableEntity table)) return;

        BlackjackGameController controller = table.getOrCreateController(player);

        if (controller.getRound().getPhase() != BlackjackPhase.IDLE) return;

        PlayerBlackjackData storage = PlayerBlackjackData.get(server);

        if (payload.increase()) {
            storage.incrementBetIndex(player.getUuid());
        } else {
            storage.decrementBetIndex(player.getUuid());
        }

        BlackjackStateSender.send(player, pos, storage, controller);

    }

}