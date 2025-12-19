package net.andrespr.casinorocket.block.entity.custom;

import net.andrespr.casinorocket.block.entity.ModBlockEntities;
import net.andrespr.casinorocket.screen.custom.chip_table.ChipTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ChipTableEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos> {

    public ChipTableEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHIP_TABLE_BE, pos, state);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("gui.casinorocket.chip_table");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ChipTableScreenHandler(syncId, playerInventory, this.pos);
    }

}