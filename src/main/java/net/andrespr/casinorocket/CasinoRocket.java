package net.andrespr.casinorocket;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.block.entity.ModBlockEntities;
import net.andrespr.casinorocket.command.CasinoRocketCommands;
import net.andrespr.casinorocket.config.CasinoRocketConfig;
import net.andrespr.casinorocket.games.gachapon.PlushiesGachaponUtils;
import net.andrespr.casinorocket.games.slot.SlotReels;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.ModItemsGroup;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.andrespr.casinorocket.network.SuitSyncPayload;
import net.andrespr.casinorocket.network.s2c.SlotConfigSyncS2CPayload;
import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.sound.ModSounds;
import net.andrespr.casinorocket.games.gachapon.GachaponUtils;
import net.andrespr.casinorocket.games.gachapon.PokemonGachaponUtils;
import net.andrespr.casinorocket.villager.ModVillagers;
import net.andrespr.casinorocket.villager.ShopsRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
        SlotReels.reloadFromConfig(CasinoRocket.CONFIG.slotMachine);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            SlotConfigSyncS2CPayload payload = SlotConfigSyncS2CPayload.fromServer();
            ServerPlayNetworking.send(handler.player, payload);
        });

        ModItemsGroup.registerItemGroups();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModSounds.registerSounds();
        ModBlockEntities.registerBlockEntities();
        ModScreenHandlers.registerScreenHandlers();
        ModVillagers.registerVillagers();
        CasinoRocketPackets.registerServer();
        CommandRegistrationCallback.EVENT.register(CasinoRocketCommands::register);

        PayloadTypeRegistry.playS2C().register(SuitSyncPayload.ID, SuitSyncPayload.CODEC);
        ShopsRegistry.bootstrap();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            GachaponUtils.buildCache(CasinoRocket.CONFIG.itemGachapon.pools);
            PokemonGachaponUtils.buildCache(CasinoRocket.CONFIG.pokemonGachapon.pools);
            PlushiesGachaponUtils.buildCache(CasinoRocket.CONFIG.plushiesGachapon.plushies);
        });

        LOGGER.info("Mod initialized successfully!");

    }

}