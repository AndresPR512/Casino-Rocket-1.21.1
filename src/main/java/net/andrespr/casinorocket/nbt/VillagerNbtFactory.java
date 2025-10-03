package net.andrespr.casinorocket.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Utility para crear el "tronco" NBT común de los villager que usamos.
 * Devuelve un NbtCompound fresco por cada llamada.
 */
public final class VillagerNbtFactory {

    private VillagerNbtFactory() {}

    /**
     * Crea un NbtCompound base para un villager.
     * @param displayName full text (p. ej. "Dealer")
     * @param jobPos      posición del job site (si null no añade Brain.memories)
     * @param profession  id de la profesión (p. ej. "cobbledollars:cobble_merchant")
     * @param noAI        poner NoAI: true
     * @param persistent  poner PersistenceRequired: true
     * @return NbtCompound con el tronco listo (sin CobbleMerchantShop)
     */
    public static NbtCompound createBaseVillagerNbt(String displayName, BlockPos jobPos,
                                                    String profession, boolean noAI, boolean persistent) {
        NbtCompound root = new NbtCompound();

        // id
        root.putString("id", "minecraft:villager");

        // CustomName (JSON simple)
        String nameJson = "{\"text\":\"" + escapeJson(displayName) + "\"}";
        root.putString("CustomName", nameJson);

        // VillagerData
        NbtCompound villagerData = new NbtCompound();
        villagerData.putString("profession", profession);
        villagerData.putInt("level", 1);
        villagerData.putString("type", "minecraft:plains");
        root.put("VillagerData", villagerData);

        // Flags
        if (noAI) root.putBoolean("NoAI", true);
        if (persistent) root.putBoolean("PersistenceRequired", true);

        // Brain.memories.minecraft:job_site -> { value: { pos: [I; x,y,z], dimension: "minecraft:overworld" } }
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

        return root;
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}