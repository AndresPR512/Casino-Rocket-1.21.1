package net.andrespr.casinorocket.data;

import net.andrespr.casinorocket.games.blackjack.BlackjackRules;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PlayerBlackjackData extends PersistentState {

    private static final String STORAGE_KEY = "casinorocket_blackjack_data";

    // === GAME DATA ===
    private final Map<UUID, Long> balances = new HashMap<>();
    private final Map<UUID, Integer> betIndex = new HashMap<>();

    // === LEADERBOARD STATS ===
    private final Map<UUID, Long> totalDeposited = new HashMap<>();
    private final Map<UUID, Long> totalWon = new HashMap<>();
    private final Map<UUID, Long> highestWin = new HashMap<>();
    private final Map<UUID, Long> lastWin = new HashMap<>();
    private final Map<UUID, Long> totalSpent = new HashMap<>();

    public static PlayerBlackjackData get(MinecraftServer server) {
        PersistentStateManager manager = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager();
        PersistentState.Type<PlayerBlackjackData> type = new PersistentState.Type<>(
                PlayerBlackjackData::new, PlayerBlackjackData::readNbt, null
        );
        return manager.getOrCreate(type, STORAGE_KEY);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound balTag = new NbtCompound();
        balances.forEach((uuid, val) -> balTag.putLong(uuid.toString(), val));
        nbt.put("balances", balTag);

        NbtCompound betTag = new NbtCompound();
        betIndex.forEach((uuid, val) -> betTag.putInt(uuid.toString(), val));
        nbt.put("betIndex", betTag);

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

    private static PlayerBlackjackData readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        PlayerBlackjackData data = new PlayerBlackjackData();

        if (nbt.contains("balances", NbtElement.COMPOUND_TYPE)) {
            NbtCompound bal = nbt.getCompound("balances");
            bal.getKeys().forEach(key -> {
                try { data.balances.put(UUID.fromString(key), bal.getLong(key)); } catch (Exception ignored) {}
            });
        }

        if (nbt.contains("betIndex", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("betIndex");
            t.getKeys().forEach(k -> {
                try { data.betIndex.put(UUID.fromString(k), t.getInt(k)); } catch (Exception ignored) {}
            });
        }

        if (nbt.contains("totalDeposited", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("totalDeposited");
            t.getKeys().forEach(k -> { try { data.totalDeposited.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {} });
        }

        if (nbt.contains("totalWon", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("totalWon");
            t.getKeys().forEach(k -> { try { data.totalWon.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {} });
        }

        if (nbt.contains("highestWin", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("highestWin");
            t.getKeys().forEach(k -> { try { data.highestWin.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {} });
        }

        if (nbt.contains("lastWin", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("lastWin");
            t.getKeys().forEach(k -> { try { data.lastWin.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {} });
        }

        if (nbt.contains("totalSpent", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = nbt.getCompound("totalSpent");
            t.getKeys().forEach(k -> { try { data.totalSpent.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {} });
        }

        return data;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    // === GETTERS ===
    public long getBalance(UUID id) {
        return balances.getOrDefault(id, 0L);
    }

    public int getBetIndex(UUID id) {
        return betIndex.getOrDefault(id, BlackjackRules.DEFAULT_BET_INDEX);
    }

    public long getBetAmount(UUID id) {
        int index = getBetIndex(id);
        return BlackjackRules.BET_VALUES.get(index);
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
        s.addAll(betIndex.keySet());
        s.addAll(totalDeposited.keySet());
        s.addAll(totalWon.keySet());
        s.addAll(highestWin.keySet());
        s.addAll(lastWin.keySet());
        s.addAll(totalSpent.keySet());
        return s;
    }

    // === SETTERS ===
    public void setBalance(UUID id, long value) {
        balances.put(id, Math.max(0L, value));
        markDirty();
    }

    public void setBetIndex(UUID id, int index) {
        int max = BlackjackRules.BET_VALUES.size() - 1;
        if (index < 0 || index > max) return;

        betIndex.put(id, index);
        markDirty();
    }

    public void setLastWin(UUID id, long amount) {
        lastWin.put(id, Math.max(0L, amount));
        markDirty();
    }

    // === MUTATORS ===
    public void addBalance(UUID id, long amount) {
        balances.merge(id, amount, Long::sum);
        if (balances.getOrDefault(id, 0L) < 0L) balances.put(id, 0L);
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

    public void incrementBetIndex(UUID id) {
        int idx = getBetIndex(id);
        if (idx < BlackjackRules.BET_VALUES.size() - 1) {
            betIndex.put(id, idx + 1);
            markDirty();
        }
    }

    public void decrementBetIndex(UUID id) {
        int idx = getBetIndex(id);
        if (idx > 0) {
            betIndex.put(id, idx - 1);
            markDirty();
        }
    }

}