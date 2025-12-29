package net.andrespr.casinorocket.data;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.slot.SlotMachineConstants;
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
import java.util.HashSet;
import java.util.Set;

public class PlayerSlotMachineData extends PersistentState {

    private static final String STORAGE_KEY = "casinorocket_slot_machine_data";

    private final Map<UUID, Long> balances = new HashMap<>();
    private final Map<UUID, Integer> betBase = new HashMap<>();
    private final Map<UUID, Integer> linesMode = new HashMap<>();

    private final Map<UUID, Long> totalDeposited = new HashMap<>();
    private final Map<UUID, Long> totalWon = new HashMap<>();
    private final Map<UUID, Long> highestWin = new HashMap<>();
    private final Map<UUID, Long> lastWin = new HashMap<>();
    private final Map<UUID, Long> totalSpent = new HashMap<>();

    private static final int DATA_VERSION_CURRENT = 2; // súbelo cuando quieras forzar reset
    private int dataVersion = DATA_VERSION_CURRENT;

    public static PlayerSlotMachineData get(MinecraftServer server) {
        PersistentStateManager manager = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager();
        PersistentState.Type<PlayerSlotMachineData> type = new PersistentState.Type<>(
                PlayerSlotMachineData::new, PlayerSlotMachineData::readNbt, null);
        return manager.getOrCreate(type, STORAGE_KEY);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {

        nbt.putInt("dataVersion", dataVersion);

        NbtCompound balTag = new NbtCompound();
        balances.forEach((uuid, val) -> balTag.putLong(uuid.toString(), val));
        nbt.put("balances", balTag);

        NbtCompound betTag = new NbtCompound();
        betBase.forEach((uuid, val) -> betTag.putInt(uuid.toString(), val));
        nbt.put("betBase", betTag);

        NbtCompound linesTag = new NbtCompound();
        linesMode.forEach((uuid, val) -> linesTag.putInt(uuid.toString(), val));
        nbt.put("linesMode", linesTag);

        NbtCompound depTag = new NbtCompound();
        totalDeposited.forEach((uuid, val) -> depTag.putLong(uuid.toString(), val));
        nbt.put("totalDeposited", depTag);

        NbtCompound wonTag = new NbtCompound();
        totalWon.forEach((uuid, val) -> wonTag.putLong(uuid.toString(), val));
        nbt.put("totalWon", wonTag);

        NbtCompound highTag = new NbtCompound();
        highestWin.forEach((uuid, val) -> highTag.putLong(uuid.toString(), val));
        nbt.put("highestWin", highTag);

        NbtCompound lastTag = new NbtCompound();
        lastWin.forEach((uuid, val) -> lastTag.putLong(uuid.toString(), val));
        nbt.put("lastWin", lastTag);

        NbtCompound spentTag = new NbtCompound();
        totalSpent.forEach((uuid, val) -> spentTag.putLong(uuid.toString(), val));
        nbt.put("totalSpent", spentTag);

        return nbt;
    }

    private static PlayerSlotMachineData readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        PlayerSlotMachineData data = new PlayerSlotMachineData();

        int version = nbt.contains("dataVersion", NbtElement.INT_TYPE) ? nbt.getInt("dataVersion") : 0;
        data.dataVersion = version;

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

        if (nbt.contains("totalDeposited", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("totalDeposited");
            t.getKeys().forEach(k -> { try { data.totalDeposited.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {}});
        }

        if (nbt.contains("totalWon", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("totalWon");
            t.getKeys().forEach(k -> { try { data.totalWon.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {}});
        }

        if (nbt.contains("highestWin", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("highestWin");
            t.getKeys().forEach(k -> { try { data.highestWin.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {}});
        }

        if (nbt.contains("lastWin", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("lastWin");
            t.getKeys().forEach(k -> { try { data.lastWin.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {}});
        }

        if (nbt.contains("totalSpent", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("totalSpent");
            t.getKeys().forEach(k -> { try { data.totalSpent.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {}});
        }

        if (version < DATA_VERSION_CURRENT) {
            data.migrate(version);
            data.dataVersion = DATA_VERSION_CURRENT;
            data.markDirty();
        }

        return data;
    }

    private void migrate(int fromVersion) {
        resetSettingsToDefault();
    }

    private void resetSettingsToDefault() {
        int defaultBet = SlotMachineConstants.defaultBetBase();
        int defaultMode = SlotMachineConstants.defaultLinesMode();

        Set<UUID> players = getAllKnownPlayers();

        for (UUID id : players) {
            betBase.put(id, defaultBet);
            linesMode.put(id, defaultMode);
        }
    }

    // === GETTERS ===
    public long getBalance(UUID id) {
        return balances.getOrDefault(id, 0L);
    }

    public int getBetBase(UUID id) {
        return betBase.getOrDefault(id, SlotMachineConstants.defaultBetBase());
    }

    public int getLinesMode(UUID id) {
        return linesMode.getOrDefault(id, SlotMachineConstants.defaultLinesMode());
    }

    public long getTotalDeposited(UUID id) {
        return totalDeposited.getOrDefault(id, 0L);
    }

    public long getTotalWon(UUID id) {
        return totalWon.getOrDefault(id, 0L);
    }

    public long getHighestWin(UUID id) {
        return highestWin.getOrDefault(id, 0L);
    }

    public long getLastWin(UUID id) {
        return lastWin.getOrDefault(id, 0L);
    }

    public long getTotalSpent(UUID id) {
        return totalSpent.getOrDefault(id, 0L);
    }

    public long getTotalLost(UUID id) {
        return getTotalWon(id) - getTotalSpent(id);
    }

    public Set<UUID> getAllKnownPlayers() {
        Set<UUID> s = new HashSet<>();
        s.addAll(balances.keySet());
        s.addAll(totalDeposited.keySet());
        s.addAll(totalWon.keySet());
        s.addAll(highestWin.keySet());
        s.addAll(lastWin.keySet());
        s.addAll(totalSpent.keySet());
        s.addAll(betBase.keySet());
        s.addAll(linesMode.keySet());
        return s;
    }

    // === SETTERS ===
    public void setBetBase(UUID id, int base) {
        betBase.put(id, base);
        markDirty();
    }

    public void setLinesMode(UUID id, int mode) {
        linesMode.put(id, mode);
        markDirty();
    }

    public void setBalance(UUID id, long value) {
        balances.put(id, value);
        markDirty();
    }

    public void setLastWin(UUID id, long amount) {
        lastWin.put(id, Math.max(0L, amount));
        markDirty();
    }

    // === MUTATORS ===
    public void addBalance(UUID id, long amount) {
        balances.merge(id, amount, Long::sum);
        markDirty();
    }

    public void addTotalDeposited(UUID id, long amount) {
        if (amount <= 0) return;
        totalDeposited.merge(id, amount, Long::sum);
        markDirty();
    }

    public void addTotalWon(UUID id, long amount) {
        if (amount <= 0) return;
        totalWon.merge(id, amount, Long::sum);
        markDirty();
    }

    public void updateHighestWin(UUID id, long win) {
        if (win <= 0) return;
        long prev = highestWin.getOrDefault(id, 0L);
        if (win > prev) {
            highestWin.put(id, win);
            markDirty();
        }
    }

    public void addTotalSpent(UUID id, long amount) {
        if (amount <= 0) return;
        totalSpent.merge(id, amount, Long::sum);
        markDirty();
    }

}