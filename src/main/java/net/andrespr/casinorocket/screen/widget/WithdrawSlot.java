package net.andrespr.casinorocket.screen.widget;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class WithdrawSlot extends Slot {

    public WithdrawSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity player) {
        return false;
    }

    @Override
    public int getMaxItemCount() {
        return 64;
    }

}
