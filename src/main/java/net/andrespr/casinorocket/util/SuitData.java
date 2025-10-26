package net.andrespr.casinorocket.util;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;

public class SuitData {

    private static final String KEY = "casinorocket.CustomSuit";

    public static void setSuitServer(VillagerEntity villager, int suit) {
        NbtCompound nbt = ((IEntityDataSaver) villager).getPersistentData();
        nbt.putInt(KEY, suit);
    }

    public static int getSuit(VillagerEntity villager) {
        NbtCompound nbt = ((IEntityDataSaver) villager).getPersistentData();
        return nbt.contains(KEY) ? nbt.getInt(KEY) : 0;
    }

    public static void setSuitClient(VillagerEntity villager, int suit) {
        NbtCompound nbt = ((IEntityDataSaver) villager).getPersistentData();
        nbt.putInt(KEY, suit);
    }

}
