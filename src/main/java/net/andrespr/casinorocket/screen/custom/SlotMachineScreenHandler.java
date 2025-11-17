package net.andrespr.casinorocket.screen.custom;

import net.andrespr.casinorocket.data.SlotMachineDataStorage;
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
            long balance = SlotMachineDataStorage.get(Objects.requireNonNull(player.getServer())).getBalance(player.getUuid());
            ServerPlayNetworking.send((ServerPlayerEntity) player, new SendSlotBalanceS2CPayload(balance));
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