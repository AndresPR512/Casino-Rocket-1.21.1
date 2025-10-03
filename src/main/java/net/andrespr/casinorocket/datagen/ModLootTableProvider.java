package net.andrespr.casinorocket.datagen;

import net.andrespr.casinorocket.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {

    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        // ADDING DROPS FOR GOLD BLOCKS
        addDrop(ModBlocks.CUT_GOLD_BLOCK);
        addDrop(ModBlocks.CUT_GOLD_STAIRS);
        addDrop(ModBlocks.CUT_GOLD_SLAB, slabDrops(ModBlocks.CUT_GOLD_SLAB));
        addDrop(ModBlocks.GOLD_BRICKS);
        addDrop(ModBlocks.GOLD_BRICK_STAIRS);
        addDrop(ModBlocks.GOLD_BRICK_SLAB, slabDrops(ModBlocks.GOLD_BRICK_SLAB));
        addDrop(ModBlocks.GOLD_BRICK_WALL);
        addDrop(ModBlocks.GOLD_TILES);
        addDrop(ModBlocks.GOLD_TILE_STAIRS);
        addDrop(ModBlocks.GOLD_TILE_SLAB, slabDrops(ModBlocks.GOLD_TILE_SLAB));
        addDrop(ModBlocks.GOLD_TILE_WALL);
        addDrop(ModBlocks.CHISELED_GOLD_BLOCK);
        addDrop(ModBlocks.GOLD_PILLAR);
        addDrop(ModBlocks.HEAVY_GOLD_PILLAR);
        addDrop(ModBlocks.GOLD_DOOR, doorDrops(ModBlocks.GOLD_DOOR));
        addDrop(ModBlocks.GOLD_TRAPDOOR);
        // ADDING DROPS FOR DIAMOND BLOCKS
        addDrop(ModBlocks.CUT_DIAMOND_BLOCK);
        addDrop(ModBlocks.CUT_DIAMOND_STAIRS);
        addDrop(ModBlocks.CUT_DIAMOND_SLAB, slabDrops(ModBlocks.CUT_DIAMOND_SLAB));
        addDrop(ModBlocks.DIAMOND_BRICKS);
        addDrop(ModBlocks.DIAMOND_BRICK_STAIRS);
        addDrop(ModBlocks.DIAMOND_BRICK_SLAB, slabDrops(ModBlocks.DIAMOND_BRICK_SLAB));
        addDrop(ModBlocks.DIAMOND_BRICK_WALL);
        addDrop(ModBlocks.DIAMOND_TILES);
        addDrop(ModBlocks.DIAMOND_TILE_STAIRS);
        addDrop(ModBlocks.DIAMOND_TILE_SLAB, slabDrops(ModBlocks.DIAMOND_TILE_SLAB));
        addDrop(ModBlocks.DIAMOND_TILE_WALL);
        addDrop(ModBlocks.CHISELED_DIAMOND_BLOCK);
        addDrop(ModBlocks.DIAMOND_PILLAR);
        addDrop(ModBlocks.HEAVY_DIAMOND_PILLAR);
        addDrop(ModBlocks.DIAMOND_DOOR, doorDrops(ModBlocks.DIAMOND_DOOR));
        addDrop(ModBlocks.DIAMOND_TRAPDOOR);
    }

}
