package net.andrespr.casinorocket.util;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;

public class LookPlayerData {

    private static final String KEY = "casinorocket.LookPlayer";

    public static int getLookPlayer(VillagerEntity villager) {
        NbtCompound nbt = ((IEntityDataSaver) villager).getPersistentData();
        return nbt.contains(KEY) ? nbt.getInt(KEY) : 0;
    }

    public static void setLookPlayer(VillagerEntity villager, int value) {
        NbtCompound nbt = ((IEntityDataSaver) villager).getPersistentData();
        nbt.putInt(KEY, value);
    }

}