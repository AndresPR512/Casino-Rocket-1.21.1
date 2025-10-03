package net.andrespr.casinorocket.villager;

import net.andrespr.casinorocket.CasinoRocket;
import com.google.common.collect.ImmutableSet;
import net.andrespr.casinorocket.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModVillagers {

    public static final RegistryKey<PointOfInterestType> CASINO_WORKER_POI_KEY = registerPoiKey("casino_worker_poi");
    public static final PointOfInterestType CASINO_WORKER_POI = registerPOI("casino_worker_poi", ModBlocks.CHISELED_DIAMOND_BLOCK);

    public static final VillagerProfession CASINO_WORKER = registerProfession("casino_worker", CASINO_WORKER_POI_KEY);

    private static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type) {
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(CasinoRocket.MOD_ID, name),
                new VillagerProfession(name, entry -> entry.matchesKey(type), entry -> entry.matchesKey(type),
                        ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN));
    }

    private static PointOfInterestType registerPOI(String name, Block block) {
        return PointOfInterestHelper.register(Identifier.of(CasinoRocket.MOD_ID, name),
                1, 1, block);
    }

    private static RegistryKey<PointOfInterestType> registerPoiKey(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(CasinoRocket.MOD_ID, name));
    }

    public static void registerVillagers() {
        CasinoRocket.LOGGER.info("Registering Villagers for " + CasinoRocket.MOD_ID);
    }
}
