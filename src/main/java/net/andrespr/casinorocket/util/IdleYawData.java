package net.andrespr.casinorocket.util;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;

public class IdleYawData {

    private static final String KEY = "casinorocket.IdleYaw";

    public static float get(VillagerEntity v) {
        NbtCompound nbt = ((IEntityDataSaver) v).getPersistentData();
        return nbt.contains(KEY) ? nbt.getFloat(KEY) : v.getYaw();
    }

    public static void set(VillagerEntity v, float yaw) {
        ((IEntityDataSaver) v).getPersistentData().putFloat(KEY, yaw);
    }

    public static boolean has(VillagerEntity v) {
        return ((IEntityDataSaver) v).getPersistentData().contains(KEY);
    }

}