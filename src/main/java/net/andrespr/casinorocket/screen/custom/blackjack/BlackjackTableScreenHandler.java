package net.andrespr.casinorocket.screen.custom.blackjack;

import net.andrespr.casinorocket.block.entity.custom.BlackjackTableEntity;
import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.screen.opening.BlackjackTableOpenData;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class BlackjackTableScreenHandler extends ScreenHandler implements IMachineBoundHandler {

    private final BlockPos pos;
    private final String machineKey;
    private final long initialBalance;
    private final int initialBetIndex;

    public BlackjackTableScreenHandler(int syncId, PlayerInventory inv, BlackjackTableOpenData data) {
        super(ModScreenHandlers.BLACKJACK_TABLE_SCREEN_HANDLER, syncId);
        this.pos = data.pos();
        this.machineKey = data.machineKey();
        this.initialBalance = data.balance();
        this.initialBetIndex = data.betIndex();
    }

    public BlackjackTableScreenHandler(int syncId, PlayerInventory inv, BlockPos pos, String machineKey, long balance, int betIndex) {
        super(ModScreenHandlers.BLACKJACK_TABLE_SCREEN_HANDLER, syncId);
        this.pos = pos;
        this.machineKey = machineKey;
        this.initialBalance = balance;
        this.initialBetIndex = betIndex;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (player.getWorld().isClient) return;
        if (!(player instanceof ServerPlayerEntity sp)) return;

        BlockPos machinePos = this.pos;
        MinecraftServer server = sp.getServer();
        if (server == null) return;

        server.execute(() -> {
            if (sp.currentScreenHandler instanceof IMachineBoundHandler bound
                    && bound.getMachinePos().equals(machinePos)) {
                return;
            }

            BlockEntity be = sp.getWorld().getBlockEntity(machinePos);
            if (be instanceof BlackjackTableEntity table) {
                table.unlock(sp);
            }
        });
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
    public BlockPos getMachinePos() { return pos; }
    public long getInitialBalance() { return initialBalance; }
    public int getInitialBetIndex() { return initialBetIndex; }

    @Override
    public String getMachineKey() {
        return machineKey;
    }

}