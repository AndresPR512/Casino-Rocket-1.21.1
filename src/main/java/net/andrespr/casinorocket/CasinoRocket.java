package net.andrespr.casinorocket;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.block.entity.ModBlockEntities;
import net.andrespr.casinorocket.command.CasinoRocketCommands;
import net.andrespr.casinorocket.config.CasinoRocketConfig;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.ModItemsGroup;
import net.andrespr.casinorocket.network.SuitSyncPayload;
import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.screen.custom.SlotMachineScreen;
import net.andrespr.casinorocket.sound.ModSounds;
import net.andrespr.casinorocket.util.gacha.GachaponUtils;
import net.andrespr.casinorocket.util.gacha.PokemonGachaponUtils;
import net.andrespr.casinorocket.villager.ShopsRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CasinoRocket implements ModInitializer {

    public static final String MOD_ID = "casinorocket";
    public static final Logger LOGGER = LoggerFactory.getLogger("CasinoRocket");
    public static CasinoRocketConfig CONFIG;

    @Override
    public void onInitialize() {
        AutoConfig.register(CasinoRocketConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
        CONFIG = AutoConfig.getConfigHolder(CasinoRocketConfig.class).getConfig();

        ModItemsGroup.registerItemGroups();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModSounds.registerSounds();
        ModBlockEntities.registerBlockEntities();
        ModScreenHandlers.registerScreenHandlers();
        CommandRegistrationCallback.EVENT.register(CasinoRocketCommands::register);

        PayloadTypeRegistry.playS2C().register(SuitSyncPayload.ID, SuitSyncPayload.CODEC);
        ShopsRegistry.bootstrap();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            GachaponUtils.buildCache(CasinoRocket.CONFIG.itemGachapon.pools);
            PokemonGachaponUtils.buildCache(CasinoRocket.CONFIG.pokemonGachapon.pools);
        });

        LOGGER.info("Mod initialized successfully!");

    }

}