package net.andrespr.casinorocket.network.c2s_handlers.blackjack;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.entity.custom.BlackjackTableEntity;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.games.blackjack.BlackjackAction;
import net.andrespr.casinorocket.games.blackjack.BlackjackGameController;
import net.andrespr.casinorocket.network.c2s.blackjack.BlackjackActionC2SPayload;
import net.andrespr.casinorocket.network.s2c.sender.BlackjackStateSender;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class BlackjackActionReceiver {

    private BlackjackActionReceiver() {}

    public static void handle(BlackjackActionC2SPayload payload, ServerPlayNetworking.Context ctx) {

        ServerPlayerEntity player = ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.currentScreenHandler instanceof IMachineBoundHandler bound)) return;

        if (!payload.pos().equals(bound.getMachinePos())) return;
        if (!payload.machineKey().equals(bound.getMachineKey())) return;

        if (!(payload.machineKey()).equals("blackjack")) {
            CasinoRocket.LOGGER.warn("[BlackjackAction] Wrong machineKey={} from {}", payload.machineKey(), player.getGameProfile().getName());
            return;
        }

        World world = player.getWorld();
        BlockPos pos = payload.pos();

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        if (!world.getChunkManager().isChunkLoaded(chunkX, chunkZ)) return;

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof BlackjackTableEntity table)) return;

        if (table.isInUse() && !table.isUsedBy(player)) {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.blackjack_table_occupied", true);
            return;
        }

        if (!table.tryLock(player)) return;

        BlackjackGameController controller = table.getOrCreateController(player);
        BlackjackAction action = payload.action();
        controller.handleAction(player, action);

        PlayerBlackjackData storage = PlayerBlackjackData.get(server);
        BlackjackStateSender.send(player, table.getPos(), storage, controller);

    }

}