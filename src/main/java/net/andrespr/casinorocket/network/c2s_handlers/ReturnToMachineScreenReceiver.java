package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.network.c2s.ReturnToMachineScreenC2SPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReturnToMachineScreenReceiver {

    public static void handle(ReturnToMachineScreenC2SPayload payload, ServerPlayNetworking.Context ctx) {

        ServerPlayerEntity player = ctx.player();
        World world = player.getWorld();
        BlockPos pos = payload.pos();

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        if (!world.getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
            return;
        }

        BlockEntity be = world.getBlockEntity(pos);
        if (be == null) return;

        if (be instanceof ExtendedScreenHandlerFactory<?> factory) {
            player.openHandledScreen(factory);
        }

    }

}