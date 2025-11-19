package net.andrespr.casinorocket.screen.custom;

import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.network.s2c.SendSlotBalanceS2CPayload;
import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class SlotMachineScreenHandler extends ScreenHandler {

    public SlotMachineScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(pos));
    }

    public SlotMachineScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.SLOT_MACHINE_SCREEN_HANDLER, syncId);

        PlayerEntity player = playerInventory.player;

        if (!player.getWorld().isClient) {
            var server = Objects.requireNonNull(player.getServer());
            var storage = PlayerSlotMachineData.get(server);
            var uuid = player.getUuid();

            long balance = storage.getBalance(uuid);
            int betBase = storage.getBetBase(uuid);
            int lines = storage.getLinesMode(uuid);

            // Send balance
            ServerPlayNetworking.send((ServerPlayerEntity) player, new SendSlotBalanceS2CPayload(balance));

            // Send betAmount
            ServerPlayNetworking.send((ServerPlayerEntity) player, new SendMenuSettingsS2CPayload(balance, betBase, lines));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

}