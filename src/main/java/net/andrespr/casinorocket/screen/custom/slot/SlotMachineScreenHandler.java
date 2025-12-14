package net.andrespr.casinorocket.screen.custom.slot;

import net.andrespr.casinorocket.block.entity.custom.SlotMachineEntity;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.network.s2c.SendSlotBalanceS2CPayload;
import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.screen.opening.SlotMachineOpenData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.UUID;

public class SlotMachineScreenHandler extends ScreenHandler {

    private final BlockPos pos;
    private final long initialBalance;
    private final int initialBetBase;
    private final int initialLinesMode;

    public SlotMachineScreenHandler(int syncId, PlayerInventory inv, SlotMachineOpenData data) {
        super(ModScreenHandlers.SLOT_MACHINE_SCREEN_HANDLER, syncId);
        this.pos = data.pos();
        this.initialBalance = data.balance();
        this.initialBetBase = data.betBase();
        this.initialLinesMode = data.linesMode();
    }

    public SlotMachineScreenHandler(int syncId, PlayerInventory inv, BlockPos pos, long balance, int betBase, int linesMode) {
        super(ModScreenHandlers.SLOT_MACHINE_SCREEN_HANDLER, syncId);
        this.pos = pos;
        this.initialBalance = balance;
        this.initialBetBase = betBase;
        this.initialLinesMode = linesMode;
    }

    public BlockPos getPos() { return pos; }
    public long getInitialBalance() { return initialBalance; }
    public int getInitialBetBase() { return initialBetBase; }
    public int getInitialLinesMode() { return initialLinesMode; }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        if (player.getWorld().isClient) return;

        BlockEntity be = player.getWorld().getBlockEntity(this.pos);
        if (be instanceof SlotMachineEntity slotBe) {
            slotBe.unlock(player);
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