package net.andrespr.casinorocket.screen.custom;

import net.andrespr.casinorocket.data.SlotMachineDataStorage;
import net.andrespr.casinorocket.network.s2c.SendSlotBalanceS2CPayload;
import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.screen.widget.WithdrawSlot;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Objects;

public class WithdrawScreenHandler extends ScreenHandler {

    private final Inventory inventory = new SimpleInventory(27);

    public WithdrawScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.WITHDRAW_SCREEN_HANDLER, syncId);

        addChestInventory(inventory);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

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

    // ==== HELPER -> INVENTORIES =====

    private void addChestInventory(Inventory inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new WithdrawSlot(inventory, l + i * 9, 7 + l * 18, 19 + i * 18));
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

    // === WITHDRAW INVENTORY ===

    public void loadStacksIntoSlots(List<ItemStack> stacks) {
        int i = 0;

        for (ItemStack stack : stacks) {
            if (i >= inventory.size()) break;
            inventory.setStack(i, stack.copy());
            this.slots.get(i).setStack(stack.copy());
            i++;
        }
    }

    public void clearWithdrawInventory() {
        for (int i = 0; i < inventory.size(); i++) {
            inventory.setStack(i, ItemStack.EMPTY);
            this.slots.get(i).setStack(ItemStack.EMPTY);
        }
    }

    // === GETTERS ===

    public Inventory getInventory() {
        return this.inventory;
    }

}