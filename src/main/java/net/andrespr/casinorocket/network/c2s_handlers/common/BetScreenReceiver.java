package net.andrespr.casinorocket.network.c2s_handlers.common;

import net.andrespr.casinorocket.network.c2s.common.OpenBetScreenC2SPayload;
import net.andrespr.casinorocket.screen.custom.common.BetScreenHandler;
import net.andrespr.casinorocket.screen.opening.CommonMachineOpenData;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class BetScreenReceiver {

    public static void openBetScreen(OpenBetScreenC2SPayload payload, ServerPlayNetworking.@NotNull Context context) {

        ServerPlayerEntity player = context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.currentScreenHandler instanceof IMachineBoundHandler bound)) return;

        String machineKey = bound.getMachineKey();
        BlockPos pos = bound.getMachinePos();

        CommonMachineOpenData data = new CommonMachineOpenData(pos, machineKey);

        player.openHandledScreen(new ExtendedScreenHandlerFactory<CommonMachineOpenData>() {

            @Override
            public CommonMachineOpenData getScreenOpeningData(ServerPlayerEntity p) {
                return data;
            }

            @Override
            public Text getDisplayName() {
                return Text.literal("Bet Menu");
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity p) {
                return new BetScreenHandler(syncId, inv, data);
            }

        });

    }

}