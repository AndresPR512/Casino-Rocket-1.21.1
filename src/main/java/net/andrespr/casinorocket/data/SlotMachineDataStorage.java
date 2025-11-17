package net.andrespr.casinorocket.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SlotMachineDataStorage extends PersistentState {

    private static final String STORAGE_KEY = "casinorocket_slot_data";
    private final Map<UUID, Long> depositedMoney = new HashMap<>();

    // === INITIALIZE ===
    public SlotMachineDataStorage() {}

    public static SlotMachineDataStorage get(MinecraftServer server) {
        PersistentStateManager manager = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager();

        PersistentState.Type<SlotMachineDataStorage> type = new PersistentState.Type<>(
                SlotMachineDataStorage::new,
                SlotMachineDataStorage::readNbt,
                null
        );

        return manager.getOrCreate(type, STORAGE_KEY);
    }

    // === PUBLIC METHODS ===

    public long getBalance(UUID playerId) {
        return depositedMoney.getOrDefault(playerId, 0L);
    }

    public void addBalance(UUID playerId, long amount) {
        depositedMoney.merge(playerId, amount, Long::sum);
        markDirty();
    }

    public void setBalance(UUID playerId, long amount) {
        depositedMoney.put(playerId, amount);
        markDirty();
    }

    public void clearBalance(UUID playerId) {
        depositedMoney.remove(playerId);
        markDirty();
    }

    public Map<UUID, Long> getAllBalances() {
        return depositedMoney;
    }

    // === SAVE ===
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound tag = new NbtCompound();

        for (var entry : depositedMoney.entrySet()) {
            tag.putLong(entry.getKey().toString(), entry.getValue());
        }

        nbt.put("SlotBalances", tag);
        return nbt;
    }

    // === LOAD ===
    private static SlotMachineDataStorage readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        SlotMachineDataStorage data = new SlotMachineDataStorage();

        if (nbt.contains("SlotBalances", NbtElement.COMPOUND_TYPE)) {
            NbtCompound tag = nbt.getCompound("SlotBalances");

            for (String uuidStr : tag.getKeys()) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    long value = tag.getLong(uuidStr);
                    data.depositedMoney.put(uuid, value);
                } catch (Exception ignored) {}
            }
        }

        return data;
    }

    @Override
    public boolean isDirty() { return true; }

}