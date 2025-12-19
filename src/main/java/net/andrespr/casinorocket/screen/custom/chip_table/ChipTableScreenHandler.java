package net.andrespr.casinorocket.screen.custom.chip_table;

import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.screen.widget.BetSlot;
import net.andrespr.casinorocket.util.IChipBankHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class ChipTableScreenHandler extends ScreenHandler {

    private final Inventory bankInventory;

    public ChipTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        super(ModScreenHandlers.CHIP_TABLE_SCREEN_HANDLER, syncId);

        if (playerInventory.player.getWorld().isClient) {
            this.bankInventory = new SimpleInventory(27);
        } else {
            this.bankInventory = ((IChipBankHolder) playerInventory.player).casinorocket$getChipBankInventory();
        }
        checkSize(this.bankInventory, 27);
        this.bankInventory.onOpen(playerInventory.player);

        addChestInventory(this.bankInventory);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (invSlot < this.bankInventory.size()) {
                if (!this.insertItem(originalStack, this.bankInventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.bankInventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.bankInventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.bankInventory.onClose(player);
    }

    // ==== HELPER -> INVENTORIES =====

    private void addChestInventory(Inventory inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new BetSlot(inventory, l + i * 9, 7 + l * 18, 15 + i * 18));
            }
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 7 + l * 18, 90 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 7 + i * 18, 148));
        }
    }

}