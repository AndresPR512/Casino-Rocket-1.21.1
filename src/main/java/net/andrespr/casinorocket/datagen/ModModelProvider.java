package net.andrespr.casinorocket.datagen;

import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TexturedModel;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // BLOCK STATES FOR GOLD BLOCKS
        BlockStateModelGenerator.BlockTexturePool cutGoldBlockPool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.CUT_GOLD_BLOCK);
        cutGoldBlockPool.stairs(ModBlocks.CUT_GOLD_STAIRS);
        cutGoldBlockPool.slab(ModBlocks.CUT_GOLD_SLAB);
        BlockStateModelGenerator.BlockTexturePool goldBricksPool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.GOLD_BRICKS);
        goldBricksPool.stairs(ModBlocks.GOLD_BRICK_STAIRS);
        goldBricksPool.slab(ModBlocks.GOLD_BRICK_SLAB);
        goldBricksPool.wall(ModBlocks.GOLD_BRICK_WALL);
        BlockStateModelGenerator.BlockTexturePool goldTilesPool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.GOLD_TILES);
        goldTilesPool.stairs(ModBlocks.GOLD_TILE_STAIRS);
        goldTilesPool.slab(ModBlocks.GOLD_TILE_SLAB);
        goldTilesPool.wall(ModBlocks.GOLD_TILE_WALL);
        blockStateModelGenerator.registerSingleton(ModBlocks.CHISELED_GOLD_BLOCK, TexturedModel.END_FOR_TOP_CUBE_COLUMN);
        blockStateModelGenerator.registerAxisRotated(ModBlocks.GOLD_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN);
        blockStateModelGenerator.registerAxisRotated(ModBlocks.HEAVY_GOLD_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN);
        blockStateModelGenerator.registerDoor(ModBlocks.GOLD_DOOR);
        blockStateModelGenerator.registerTrapdoor(ModBlocks.GOLD_TRAPDOOR);
        // BLOCK STATES FOR DIAMOND BLOCKS
        BlockStateModelGenerator.BlockTexturePool cutDiamondBlockPool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.CUT_DIAMOND_BLOCK);
        cutDiamondBlockPool.stairs(ModBlocks.CUT_DIAMOND_STAIRS);
        cutDiamondBlockPool.slab(ModBlocks.CUT_DIAMOND_SLAB);
        BlockStateModelGenerator.BlockTexturePool diamondBricksPool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DIAMOND_BRICKS);
        diamondBricksPool.stairs(ModBlocks.DIAMOND_BRICK_STAIRS);
        diamondBricksPool.slab(ModBlocks.DIAMOND_BRICK_SLAB);
        diamondBricksPool.wall(ModBlocks.DIAMOND_BRICK_WALL);
        BlockStateModelGenerator.BlockTexturePool diamondTilesPool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.DIAMOND_TILES);
        diamondTilesPool.stairs(ModBlocks.DIAMOND_TILE_STAIRS);
        diamondTilesPool.slab(ModBlocks.DIAMOND_TILE_SLAB);
        diamondTilesPool.wall(ModBlocks.DIAMOND_TILE_WALL);
        blockStateModelGenerator.registerSingleton(ModBlocks.CHISELED_DIAMOND_BLOCK, TexturedModel.END_FOR_TOP_CUBE_COLUMN);
        blockStateModelGenerator.registerAxisRotated(ModBlocks.DIAMOND_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN);
        blockStateModelGenerator.registerAxisRotated(ModBlocks.HEAVY_DIAMOND_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN);
        blockStateModelGenerator.registerDoor(ModBlocks.DIAMOND_DOOR);
        blockStateModelGenerator.registerTrapdoor(ModBlocks.DIAMOND_TRAPDOOR);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.BASIC_CHIP, Models.GENERATED);
        itemModelGenerator.register(ModItems.COPPER_CHIP, Models.GENERATED);
        itemModelGenerator.register(ModItems.IRON_CHIP, Models.GENERATED);
        itemModelGenerator.register(ModItems.AMETHYST_CHIP, Models.GENERATED);
        itemModelGenerator.register(ModItems.GOLDEN_CHIP, Models.GENERATED);
        itemModelGenerator.register(ModItems.EMERALD_CHIP, Models.GENERATED);
        itemModelGenerator.register(ModItems.DIAMOND_CHIP, Models.GENERATED);
        itemModelGenerator.register(ModItems.NETHERITE_CHIP, Models.GENERATED);
    }

}
