package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.c2s.OpenWithdrawScreenC2SPayload;
import net.andrespr.casinorocket.screen.custom.common.WithdrawScreenHandler;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreenHandler;
import net.andrespr.casinorocket.screen.opening.SlotMachineOpenData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class WithdrawScreenReceiver {

    public static void openWithdrawScreen(OpenWithdrawScreenC2SPayload payload, ServerPlayNetworking.Context context) {

        ServerPlayerEntity player = context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (player.currentScreenHandler instanceof SlotMachineScreenHandler slotHandler) {
            BlockPos pos = slotHandler.getPos();

            PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
            UUID uuid = player.getUuid();

            long balance = storage.getBalance(uuid);
            int betBase = storage.getBetBase(uuid);
            int lines = storage.getLinesMode(uuid);

            SlotMachineOpenData data = new SlotMachineOpenData(pos, balance, betBase, lines);

            player.openHandledScreen(new ExtendedScreenHandlerFactory<SlotMachineOpenData>() {
                @Override
                public SlotMachineOpenData getScreenOpeningData(ServerPlayerEntity p) { return data; }

                @Override
                public Text getDisplayName() { return Text.literal("Withdraw"); }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity p) {
                    return new WithdrawScreenHandler(syncId, inv, pos);
                }
            });
        }

    }

}