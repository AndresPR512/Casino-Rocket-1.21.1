package net.andrespr.casinorocket.screen.custom;

import net.andrespr.casinorocket.item.custom.BillItem;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.andrespr.casinorocket.network.s2c.sender.SlotBalanceSender;
import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.screen.widget.BetSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

public class BetScreenHandler extends ScreenHandler {

    private final SimpleInventory inventory = new SimpleInventory(27);
    private final PlayerEntity player;
    private long totalMoney = 0L;

    public BetScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.BET_SCREEN_HANDLER, syncId);
        this.inventory.addListener(this::onContentChanged);
        this.player = playerInventory.player;

        addChestInventory(inventory);
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
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        if (!player.getWorld().isClient) {
            this.dropInventory(player, this.inventory);
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                inventory.setStack(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);

        long total = 0;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();

                if (item instanceof ChipItem chip) {
                    total += chip.getValue() * stack.getCount();
                } else if (item instanceof BillItem bill) {
                    total += bill.getValue() * stack.getCount();
                }
            }
        }
        this.totalMoney = total;

        if (player instanceof ServerPlayerEntity serverPlayer) {
            SlotBalanceSender.send(serverPlayer, total);
        }
    }

    // ==== HELPER -> INVENTORIES =====

    private void addChestInventory(Inventory inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new BetSlot(inventory, l + i * 9, 7 + l * 18, 19 + i * 18));
            }
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 7 + l * 18, 85 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 7 + i * 18, 143));
        }
    }

    // === GETTERS ===

    public SimpleInventory getInventory() {
        return this.inventory;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

}