package net.andrespr.casinorocket.villager;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public final class VillagerNbtFactory {

    private VillagerNbtFactory() {}

    public static NbtCompound createBaseVillagerNbt(String displayName, BlockPos jobPos, String profession) {
        return createBaseVillagerNbt(displayName, jobPos, profession, 0);
    }

    public static NbtCompound createBaseVillagerNbt(String displayName, BlockPos jobPos, String profession, int suitId) {

        NbtCompound root = new NbtCompound();

        // Villager ID
        root.putString("id", "minecraft:villager");

        // Custom Name
        String nameJson = "{\"text\":\"" + escapeJson(displayName) + "\"}";
        root.putString("CustomName", nameJson);

        // VillagerData
        NbtCompound villagerData = new NbtCompound();
        villagerData.putString("profession", profession);
        villagerData.putInt("level", 5);
        villagerData.putString("type", "minecraft:plains");
        root.put("VillagerData", villagerData);

        // Flags
        root.putBoolean("NoAI", true);
        root.putBoolean("PersistenceRequired", true);

        // JobPos Memories
        if (jobPos != null) {
            NbtCompound brain = new NbtCompound();
            NbtCompound memories = new NbtCompound();
            NbtCompound jobSiteEntry = new NbtCompound();
            NbtCompound jobSiteValue = new NbtCompound();

            int[] posArray = new int[]{ jobPos.getX(), jobPos.getY(), jobPos.getZ() };
            jobSiteValue.putIntArray("pos", posArray);
            jobSiteValue.putString("dimension", "minecraft:overworld");

            jobSiteEntry.put("value", jobSiteValue);
            memories.put("minecraft:job_site", jobSiteEntry);
            brain.put("memories", memories);
            root.put("Brain", brain);
        }

        if (suitId > 0) {
            root.putInt("casinorocket.CustomSuit", suitId);
        }

        return root;

    }

    private static @NotNull String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}