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

public class PlayerSlotMachineData extends PersistentState {

    private static final String STORAGE_KEY = "casinorocket_slot_machine_data";

    private final Map<UUID, Long> balances = new HashMap<>();
    private final Map<UUID, Integer> betBase = new HashMap<>();
    private final Map<UUID, Integer> linesMode = new HashMap<>();

    public static PlayerSlotMachineData get(MinecraftServer server) {
        PersistentStateManager manager = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager();
        PersistentState.Type<PlayerSlotMachineData> type = new PersistentState.Type<>(
                PlayerSlotMachineData::new,
                PlayerSlotMachineData::readNbt,
                null
        );
        return manager.getOrCreate(type, STORAGE_KEY);
    }

    // Defaults
    private int defaultBetBase = 10;
    private int defaultLines = 1;

    public long getBalance(UUID id) {
        return balances.getOrDefault(id, 0L);
    }

    public int getBetBase(UUID id) {
        return betBase.getOrDefault(id, defaultBetBase);
    }

    public int getLinesMode(UUID id) {
        return linesMode.getOrDefault(id, defaultLines);
    }

    public void setBetBase(UUID id, int base) {
        betBase.put(id, base);
        markDirty();
    }

    public void setLinesMode(UUID id, int mode) {
        linesMode.put(id, mode);
        markDirty();
    }

    public void addBalance(UUID id, long amount) {
        balances.merge(id, amount, Long::sum);
        markDirty();
    }

    public void setBalance(UUID id, long value) {
        balances.put(id, value);
        markDirty();
    }

    public void clearBalance(UUID id) {
        balances.remove(id);
        markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound balTag = new NbtCompound();
        balances.forEach((uuid, val) -> balTag.putLong(uuid.toString(), val));
        nbt.put("balances", balTag);

        NbtCompound betTag = new NbtCompound();
        betBase.forEach((uuid, val) -> betTag.putInt(uuid.toString(), val));
        nbt.put("betBase", betTag);

        NbtCompound linesTag = new NbtCompound();
        linesMode.forEach((uuid, val) -> linesTag.putInt(uuid.toString(), val));
        nbt.put("linesMode", linesTag);

        return nbt;
    }

    private static PlayerSlotMachineData readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        PlayerSlotMachineData data = new PlayerSlotMachineData();

        if (nbt.contains("balances", NbtElement.COMPOUND_TYPE)) {
            NbtCompound bal = nbt.getCompound("balances");
            bal.getKeys().forEach(key -> {
                try { data.balances.put(UUID.fromString(key), bal.getLong(key)); } catch (Exception ignored) {}
            });
        }

        if (nbt.contains("betBase", NbtElement.COMPOUND_TYPE)) {
            NbtCompound bb = nbt.getCompound("betBase");
            bb.getKeys().forEach(key -> {
                try { data.betBase.put(UUID.fromString(key), bb.getInt(key)); } catch (Exception ignored) {}
            });
        }

        if (nbt.contains("linesMode", NbtElement.COMPOUND_TYPE)) {
            NbtCompound lm = nbt.getCompound("linesMode");
            lm.getKeys().forEach(key -> {
                try { data.linesMode.put(UUID.fromString(key), lm.getInt(key)); } catch (Exception ignored) {}
            });
        }

        return data;
    }

    @Override
    public boolean isDirty() { return true; }

}