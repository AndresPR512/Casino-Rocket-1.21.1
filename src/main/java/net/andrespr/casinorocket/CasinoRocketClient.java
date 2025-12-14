package net.andrespr.casinorocket;

import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.andrespr.casinorocket.network.SuitSync;
import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.screen.custom.common.BetScreen;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineMenuScreen;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreen;
import net.andrespr.casinorocket.screen.custom.common.WithdrawScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class CasinoRocketClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLD_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLD_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GACHA_MACHINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POKEMON_GACHA_MACHINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SLOT_MACHINE, RenderLayer.getCutout());

        HandledScreens.register(ModScreenHandlers.SLOT_MACHINE_SCREEN_HANDLER, SlotMachineScreen::new);
        HandledScreens.register(ModScreenHandlers.SLOT_MACHINE_MENU_SCREEN_HANDLER, SlotMachineMenuScreen::new);
        HandledScreens.register(ModScreenHandlers.BET_SCREEN_HANDLER, BetScreen::new);
        HandledScreens.register(ModScreenHandlers.WITHDRAW_SCREEN_HANDLER, WithdrawScreen::new);

        ModItems.ALL_BILL_ITEMS.forEach(bill -> {
            ModelPredicateProviderRegistry.register(bill, Identifier.of("stacked"),
                    (stack, world, entity, seed) -> stack.getCount() >= 3 ? 1.0F : 0.0F
            );
        });

        SuitSync.registerClientReceiver();
        CasinoRocketPackets.registerClient();

    }

}