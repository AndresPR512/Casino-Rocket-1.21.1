package net.andrespr.casinorocket.block.entity.custom;

import net.andrespr.casinorocket.block.entity.ModBlockEntities;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.games.blackjack.BlackjackGameController;
import net.andrespr.casinorocket.network.s2c.sender.BlackjackStateSender;
import net.andrespr.casinorocket.screen.custom.blackjack.BlackjackTableScreenHandler;
import net.andrespr.casinorocket.screen.opening.BlackjackTableOpenData;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BlackjackTableEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlackjackTableOpenData> {

    private UUID currentUser;

    public BlackjackTableEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLACKJACK_TABLE_BE, pos, state);
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
            controllers.remove(player.getUuid());
            currentUser = null;
            markDirty();
        }
    }

    public void forceUnlock() {
        if (currentUser != null) {
            controllers.remove(currentUser);
            currentUser = null;
            markDirty();
        }
    }

    // === DISPLAY NAME ===
    @Override
    public Text getDisplayName() {
        return Text.translatable("gui.casinorocket.blackjack_table");
    }

    // === OPENING DATA ===
    @Override
    public BlackjackTableOpenData getScreenOpeningData(ServerPlayerEntity player) {
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        PlayerBlackjackData storage = PlayerBlackjackData.get(server);
        UUID uuid = player.getUuid();

        long balance = storage.getBalance(uuid);
        int index = storage.getBetIndex(uuid);

        return new BlackjackTableOpenData(this.pos, "blackjack", balance, index);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        long balance = 0;
        int index = 0;

        if (player instanceof ServerPlayerEntity sp) {
            MinecraftServer server = Objects.requireNonNull(sp.getServer());
            PlayerBlackjackData storage = PlayerBlackjackData.get(server);
            UUID uuid = sp.getUuid();

            balance = storage.getBalance(uuid);
            index = storage.getBetIndex(uuid);

            sendState(sp);
        }

        return new BlackjackTableScreenHandler(syncId, inv, this.getPos(), "blackjack", balance, index);
    }

    // === CONTROLLER ===
    private final Map<UUID, BlackjackGameController> controllers = new HashMap<>();

    public BlackjackGameController getOrCreateController(ServerPlayerEntity player) {
        return controllers.computeIfAbsent(player.getUuid(), id ->
                new BlackjackGameController(id, PlayerBlackjackData.get(Objects.requireNonNull(player.getServer())))
        );
    }

    // === SEND STATE ===
    public void sendState(ServerPlayerEntity player) {
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        PlayerBlackjackData storage = PlayerBlackjackData.get(server);
        BlackjackGameController controller = getOrCreateController(player);

        BlackjackStateSender.send(player, this.getPos(), storage, controller);
    }

}