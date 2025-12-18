package net.andrespr.casinorocket.network.c2s_handlers.common;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.entity.custom.BlackjackTableEntity;
import net.andrespr.casinorocket.block.entity.custom.SlotMachineEntity;
import net.andrespr.casinorocket.network.c2s.common.ReturnToMachineScreenC2SPayload;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReturnToMachineScreenReceiver {

    public static void handle(ReturnToMachineScreenC2SPayload payload, ServerPlayNetworking.Context ctx) {

        ServerPlayerEntity player = ctx.player();
        World world = player.getWorld();

        if (!(player.currentScreenHandler instanceof IMachineBoundHandler bound)) return;
        if (!payload.pos().equals(bound.getMachinePos())) return;
        if (!payload.machineKey().equals(bound.getMachineKey())) return;

        BlockPos pos = payload.pos();

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        if (!world.getChunkManager().isChunkLoaded(chunkX, chunkZ)) return;

        BlockEntity be = world.getBlockEntity(pos);
        if (be == null) return;

        switch (payload.machineKey()) {

            case "slots" -> {
                if (!(be instanceof SlotMachineEntity slot)) return;

                if (slot.isInUse() && !slot.isUsedBy(player)) {
                    CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.slot_machine_occupied", true);
                    return;
                }
                if (!slot.tryLock(player)) return;

                player.openHandledScreen(slot);
            }

            case "blackjack" -> {
                if (!(be instanceof BlackjackTableEntity table)) return;

                if (table.isInUse() && !table.isUsedBy(player)) {
                    CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.blackjack_table_occupied", true);
                    return;
                }
                if (!table.tryLock(player)) return;

                player.openHandledScreen(table);
            }

            default -> CasinoRocket.LOGGER.warn("[ReturnToMachine] Unknown machineKey={} from {}",
                    payload.machineKey(), player.getGameProfile().getName());

        }

    }

}