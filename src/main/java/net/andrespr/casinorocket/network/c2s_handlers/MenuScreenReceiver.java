package net.andrespr.casinorocket.network.c2s_handlers;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.c2s.OpenMenuScreenC2SPayload;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineMenuScreenHandler;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreenHandler;
import net.andrespr.casinorocket.screen.opening.SlotMachineOpenData;
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

import java.util.UUID;

public class MenuScreenReceiver {

    public static void openMenuScreen(OpenMenuScreenC2SPayload payload, ServerPlayNetworking.Context context) {

        ServerPlayerEntity player = context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.currentScreenHandler instanceof IMachineBoundHandler bound)) return;

        String key = bound.getMachineKey();
        BlockPos pos = bound.getMachinePos();

        switch (key) {
            case "slots" -> {
                PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
                UUID uuid = player.getUuid();

                long balance = storage.getBalance(uuid);
                int betBase = storage.getBetBase(uuid);
                int lines = storage.getLinesMode(uuid);

                SlotMachineOpenData data = new SlotMachineOpenData(pos, key, balance, betBase, lines);

                player.openHandledScreen(new ExtendedScreenHandlerFactory<SlotMachineOpenData>() {
                    @Override public SlotMachineOpenData getScreenOpeningData(ServerPlayerEntity p) { return data; }
                    @Override public Text getDisplayName() { return Text.literal("Slot Machine Menu"); }

                    @Override
                    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity p) {
                        return new SlotMachineMenuScreenHandler(syncId, inv, data); // <-- pásale data
                    }
                });
            }

            default -> CasinoRocket.LOGGER.warn("[MenuOpen] Unknown machineKey={} from {}", key, player.getGameProfile().getName());
        }

    }

}