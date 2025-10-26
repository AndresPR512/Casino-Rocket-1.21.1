package net.andrespr.casinorocket;

import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.network.SuitSync;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class CasinoRocketClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLD_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLD_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GACHA_MACHINE, RenderLayer.getCutout());

        ModItems.BILL_LIST.forEach(bill -> {
            ModelPredicateProviderRegistry.register(bill, Identifier.of("stacked"),
                    (stack, world, entity, seed) -> stack.getCount() >= 3 ? 1.0F : 0.0F
            );
        });

        SuitSync.registerClientReceiver();

    }

}