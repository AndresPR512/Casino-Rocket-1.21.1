package net.andrespr.casinorocket.block.entity.custom;

import net.andrespr.casinorocket.block.entity.ModBlockEntities;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreenHandler;
import net.andrespr.casinorocket.screen.opening.SlotMachineOpenData;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.UUID;

public class SlotMachineEntity extends BlockEntity implements ExtendedScreenHandlerFactory<SlotMachineOpenData> {

    private UUID currentUser;

    public SlotMachineEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SLOT_MACHINE_BE, pos, state);
    }

    // ===== LOCK API =====
    public boolean isInUse() {
        return currentUser != null;
    }

    public boolean isUsedBy(PlayerEntity player) {
        return currentUser != null && currentUser.equals(player.getUuid());
    }

    public boolean tryLock(PlayerEntity player) {
        UUID id = player.getUuid();
        if (currentUser == null) {
            currentUser = id;
            markDirty();
            return true;
        }
        return currentUser.equals(id);
    }

    public void unlock(PlayerEntity player) {
        if (currentUser != null && currentUser.equals(player.getUuid())) {
            currentUser = null;
            markDirty();
        }
    }

    public void forceUnlock() {
        if (currentUser != null) {
            currentUser = null;
            markDirty();
        }
    }

    // === DISPLAY NAME ===
    @Override
    public Text getDisplayName() {
        return Text.translatable("gui.casinorocket.slot_machine");
    }

    // === OPENING DATA ===
    @Override
    public SlotMachineOpenData getScreenOpeningData(ServerPlayerEntity player) {
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUuid();

        long balance = storage.getBalance(uuid);
        int betBase = storage.getBetBase(uuid);
        int lines = storage.getLinesMode(uuid);

        return new SlotMachineOpenData(this.pos, "slots", balance, betBase, lines);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity sp) {
            MinecraftServer server = Objects.requireNonNull(sp.getServer());
            PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
            UUID uuid = sp.getUuid();

            long balance = storage.getBalance(uuid);
            int betBase = storage.getBetBase(uuid);
            int lines = storage.getLinesMode(uuid);

            return new SlotMachineScreenHandler(syncId, inv, this.getPos(), "slots", balance, betBase, lines);
        }

        return new SlotMachineScreenHandler(syncId, inv, this.getPos(), "slots", 0L, 10, 1);
    }

}