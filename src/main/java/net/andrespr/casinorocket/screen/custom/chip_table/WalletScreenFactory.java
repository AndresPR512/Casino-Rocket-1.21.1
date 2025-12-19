package net.andrespr.casinorocket.screen.custom.chip_table;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class WalletScreenFactory implements ExtendedScreenHandlerFactory<BlockPos> {

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return BlockPos.ORIGIN;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("gui.casinorocket.wallet");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new ChipTableScreenHandler(syncId, inventory, BlockPos.ORIGIN);
    }

}