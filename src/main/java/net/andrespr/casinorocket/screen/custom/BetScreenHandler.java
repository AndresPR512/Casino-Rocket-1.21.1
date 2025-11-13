package net.andrespr.casinorocket.screen.custom;

import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class BetScreenHandler extends ScreenHandler {

    public BetScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.BET_SCREEN_HANDLER, syncId);
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