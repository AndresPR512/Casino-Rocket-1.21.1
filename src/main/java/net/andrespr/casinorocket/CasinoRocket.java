package net.andrespr.casinorocket;

import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.command.CasinoRocketCommands;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.ModItemsGroup;
import net.andrespr.casinorocket.network.SuitSyncPayload;
import net.andrespr.casinorocket.util.shops.ShopsRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
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
        CommandRegistrationCallback.EVENT.register(CasinoRocketCommands::register);
        PayloadTypeRegistry.playS2C().register(SuitSyncPayload.ID, SuitSyncPayload.CODEC);
        ShopsRegistry.bootstrap();
    }

}