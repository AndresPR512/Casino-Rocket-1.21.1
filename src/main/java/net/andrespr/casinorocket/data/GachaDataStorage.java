package net.andrespr.casinorocket.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GachaDataStorage extends PersistentState {

    public Map<UUID, Map<String, Integer>> pityTracker = new HashMap<>();
    public Map<UUID, GachaStats> playerStats = new HashMap<>();

    private static final String STORAGE_KEY = "casinorocket_gacha_data";

    // === MAIN LOADER ===
    public static GachaDataStorage get(MinecraftServer server) {
        PersistentStateManager manager = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager();

        PersistentState.Type<GachaDataStorage> type = new PersistentState.Type<>(
                GachaDataStorage::new,
                GachaDataStorage::readNbt,
                null
        );

        return manager.getOrCreate(type, STORAGE_KEY);
    }

    // === SAVE ===
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, net.minecraft.registry.RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound pityTag = new NbtCompound();
        for (var entry : pityTracker.entrySet()) {
            UUID uuid = entry.getKey();
            NbtCompound coinMap = new NbtCompound();
            for (var coinEntry : entry.getValue().entrySet()) {
                coinMap.putInt(coinEntry.getKey(), coinEntry.getValue());
            }
            pityTag.put(uuid.toString(), coinMap);
        }
        nbt.put("PityTracker", pityTag);

        NbtCompound statsTag = new NbtCompound();
        for (var entry : playerStats.entrySet()) {
            statsTag.put(entry.getKey().toString(), entry.getValue().toNbt());
        }
        nbt.put("PlayerStats", statsTag);

        return nbt;
    }

    // === LOAD ===
    private static GachaDataStorage readNbt(NbtCompound nbt, net.minecraft.registry.RegistryWrapper.WrapperLookup registryLookup) {
        GachaDataStorage data = new GachaDataStorage();

        if (nbt.contains("PityTracker", NbtElement.COMPOUND_TYPE)) {
            NbtCompound pityTag = nbt.getCompound("PityTracker");
            for (String uuidStr : pityTag.getKeys()) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    NbtCompound coinTag = pityTag.getCompound(uuidStr);
                    Map<String, Integer> coinMap = new HashMap<>();
                    for (String coin : coinTag.getKeys()) {
                        coinMap.put(coin, coinTag.getInt(coin));
                    }
                    data.pityTracker.put(uuid, coinMap);
                } catch (Exception ignored) {}
            }
        }

        if (nbt.contains("PlayerStats", NbtElement.COMPOUND_TYPE)) {
            NbtCompound statsTag = nbt.getCompound("PlayerStats");
            for (String uuidStr : statsTag.getKeys()) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    data.playerStats.put(uuid, GachaStats.fromNbt(statsTag.getCompound(uuidStr)));
                } catch (Exception ignored) {}
            }
        }

        return data;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

}