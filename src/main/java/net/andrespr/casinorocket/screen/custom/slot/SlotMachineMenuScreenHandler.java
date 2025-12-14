package net.andrespr.casinorocket.screen.custom.slot;

import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.screen.opening.SlotMachineOpenData;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class SlotMachineMenuScreenHandler extends ScreenHandler implements IMachineBoundHandler {

    private final BlockPos pos;
    private final long initialBalance;
    private final int initialBetBase;
    private final int initialLinesMode;

    public SlotMachineMenuScreenHandler(int syncId, PlayerInventory inv, SlotMachineOpenData data) {
        super(ModScreenHandlers.SLOT_MACHINE_MENU_SCREEN_HANDLER, syncId);
        this.pos = data.pos();
        this.initialBalance = data.balance();
        this.initialBetBase = data.betBase();
        this.initialLinesMode = data.linesMode();
    }

    public SlotMachineMenuScreenHandler(int syncId, PlayerInventory inv, BlockPos pos, long balance, int betBase, int linesMode) {
        super(ModScreenHandlers.SLOT_MACHINE_MENU_SCREEN_HANDLER, syncId);
        this.pos = pos;
        this.initialBalance = balance;
        this.initialBetBase = betBase;
        this.initialLinesMode = linesMode;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    // === GETTERS ===
    public BlockPos getPos() { return pos; }
    public long getInitialBalance() { return initialBalance; }
    public int getInitialBetBase() { return initialBetBase; }
    public int getInitialLinesMode() { return initialLinesMode; }

}