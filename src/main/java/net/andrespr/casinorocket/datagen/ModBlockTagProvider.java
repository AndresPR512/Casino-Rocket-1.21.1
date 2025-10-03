package net.andrespr.casinorocket.datagen;

import net.andrespr.casinorocket.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                // ADDING GOLD BLOCKS
                .add(ModBlocks.CUT_GOLD_BLOCK)
                .add(ModBlocks.CUT_GOLD_STAIRS)
                .add(ModBlocks.CUT_GOLD_SLAB)
                .add(ModBlocks.GOLD_BRICKS)
                .add(ModBlocks.GOLD_BRICK_STAIRS)
                .add(ModBlocks.GOLD_BRICK_SLAB)
                .add(ModBlocks.GOLD_BRICK_WALL)
                .add(ModBlocks.GOLD_TILES)
                .add(ModBlocks.GOLD_TILE_STAIRS)
                .add(ModBlocks.GOLD_TILE_SLAB)
                .add(ModBlocks.GOLD_TILE_WALL)
                .add(ModBlocks.CHISELED_GOLD_BLOCK)
                .add(ModBlocks.GOLD_PILLAR)
                .add(ModBlocks.HEAVY_GOLD_PILLAR)
                .add(ModBlocks.GOLD_DOOR)
                .add(ModBlocks.GOLD_TRAPDOOR)
                // ADDING DIAMOND BLOCKS
                .add(ModBlocks.CUT_DIAMOND_BLOCK)
                .add(ModBlocks.CUT_DIAMOND_STAIRS)
                .add(ModBlocks.CUT_DIAMOND_SLAB)
                .add(ModBlocks.DIAMOND_BRICKS)
                .add(ModBlocks.DIAMOND_BRICK_STAIRS)
                .add(ModBlocks.DIAMOND_BRICK_SLAB)
                .add(ModBlocks.DIAMOND_BRICK_WALL)
                .add(ModBlocks.DIAMOND_TILES)
                .add(ModBlocks.DIAMOND_TILE_STAIRS)
                .add(ModBlocks.DIAMOND_TILE_SLAB)
                .add(ModBlocks.DIAMOND_TILE_WALL)
                .add(ModBlocks.CHISELED_DIAMOND_BLOCK)
                .add(ModBlocks.DIAMOND_PILLAR)
                .add(ModBlocks.HEAVY_DIAMOND_PILLAR)
                .add(ModBlocks.DIAMOND_DOOR)
                .add(ModBlocks.DIAMOND_TRAPDOOR);

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                // ADDING GOLD BLOCKS
                .add(ModBlocks.CUT_GOLD_BLOCK)
                .add(ModBlocks.CUT_GOLD_STAIRS)
                .add(ModBlocks.CUT_GOLD_SLAB)
                .add(ModBlocks.GOLD_BRICKS)
                .add(ModBlocks.GOLD_BRICK_STAIRS)
                .add(ModBlocks.GOLD_BRICK_SLAB)
                .add(ModBlocks.GOLD_BRICK_WALL)
                .add(ModBlocks.GOLD_TILES)
                .add(ModBlocks.GOLD_TILE_STAIRS)
                .add(ModBlocks.GOLD_TILE_SLAB)
                .add(ModBlocks.GOLD_TILE_WALL)
                .add(ModBlocks.CHISELED_GOLD_BLOCK)
                .add(ModBlocks.GOLD_PILLAR)
                .add(ModBlocks.HEAVY_GOLD_PILLAR)
                .add(ModBlocks.GOLD_DOOR)
                .add(ModBlocks.GOLD_TRAPDOOR)
                // ADDING DIAMOND BLOCKS
                .add(ModBlocks.CUT_DIAMOND_BLOCK)
                .add(ModBlocks.CUT_DIAMOND_STAIRS)
                .add(ModBlocks.CUT_DIAMOND_SLAB)
                .add(ModBlocks.DIAMOND_BRICKS)
                .add(ModBlocks.DIAMOND_BRICK_STAIRS)
                .add(ModBlocks.DIAMOND_BRICK_SLAB)
                .add(ModBlocks.DIAMOND_BRICK_WALL)
                .add(ModBlocks.DIAMOND_TILES)
                .add(ModBlocks.DIAMOND_TILE_STAIRS)
                .add(ModBlocks.DIAMOND_TILE_SLAB)
                .add(ModBlocks.DIAMOND_TILE_WALL)
                .add(ModBlocks.CHISELED_DIAMOND_BLOCK)
                .add(ModBlocks.DIAMOND_PILLAR)
                .add(ModBlocks.HEAVY_DIAMOND_PILLAR)
                .add(ModBlocks.DIAMOND_DOOR)
                .add(ModBlocks.DIAMOND_TRAPDOOR);

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(ModBlocks.GOLD_BRICK_WALL)
                .add(ModBlocks.GOLD_TILE_WALL)
                .add(ModBlocks.DIAMOND_BRICK_WALL)
                .add(ModBlocks.DIAMOND_TILE_WALL);
    }

}
