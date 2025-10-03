package net.andrespr.casinorocket;

import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.command.SpawnCasinoWorkers;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.ModItemsGroup;
import net.andrespr.casinorocket.villager.ModVillagers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CasinoRocket implements ModInitializer {
    public static final String MOD_ID = "casinorocket";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItemsGroup.registerItemGroups();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModVillagers.registerVillagers();

        SpawnCasinoWorkers.registerCommands();

    }
}