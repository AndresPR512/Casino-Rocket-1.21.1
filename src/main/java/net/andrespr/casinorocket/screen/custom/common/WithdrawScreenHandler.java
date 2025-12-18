package net.andrespr.casinorocket.screen.custom.common;

import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.s2c.sender.MachineBalanceSender;
import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.screen.opening.CommonMachineOpenData;
import net.andrespr.casinorocket.screen.widget.WithdrawSlot;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class WithdrawScreenHandler extends ScreenHandler implements IMachineBoundHandler {

    private final BlockPos pos;
    private final String machineKey;
    private final SimpleInventory inventory = new SimpleInventory(27);

    public WithdrawScreenHandler(int syncId, PlayerInventory playerInventory, CommonMachineOpenData data) {
        this(syncId, playerInventory, data.pos(), data.machineKey());
    }

    public WithdrawScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos, String machineKey) {
        super(ModScreenHandlers.WITHDRAW_SCREEN_HANDLER, syncId);
        this.machineKey = machineKey;
        this.pos = pos;

        this.inventory.addListener(this::onContentChanged);

        addChestInventory(inventory);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        PlayerEntity player = playerInventory.player;

        if (!player.getWorld().isClient && player instanceof ServerPlayerEntity serverPlayer) {
            long balance = resolveBalance(player);
            loadStacksIntoSlots(MoneyCalculator.calculateChipWithdraw(balance));

            this.inventory.markDirty();
            this.sendContentUpdates();

            MachineBalanceSender.send(serverPlayer, machineKey, balance);
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

    // === HELPER -> BALANCE PER MACHINE ===

    private long resolveBalance(PlayerEntity player) {
        if (player.getServer() == null) return 0L;
        return switch (machineKey) {
            case "slots" -> PlayerSlotMachineData.get(player.getServer()).getBalance(player.getUuid());
            case "blackjack" -> PlayerBlackjackData.get(player.getServer()).getBalance(player.getUuid());
            default -> 0L;
        };
    }

    // === WITHDRAW INVENTORY ===

    public void loadStacksIntoSlots(List<ItemStack> stacks) {
        for (int i = 0; i < inventory.size(); i++) {
            inventory.setStack(i, ItemStack.EMPTY);
        }

        int i = 0;
        for (ItemStack stack : stacks) {
            if (i >= inventory.size()) break;
            inventory.setStack(i, stack.copy());
            i++;
        }

        inventory.markDirty();
        sendContentUpdates();
    }

    public void clearWithdrawInventory() {
        for (int i = 0; i < inventory.size(); i++) {
            inventory.setStack(i, ItemStack.EMPTY);
        }
        inventory.markDirty();
        sendContentUpdates();
    }

    // === GETTERS ===

    @Override
    public BlockPos getMachinePos() {
        return pos;
    }

    @Override
    public String getMachineKey() {
        return machineKey;
    }

}