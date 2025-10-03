package net.andrespr.casinorocket.datagen;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {

        // GOLD BLOCKS CRAFTING TABLE RECIPES
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_BLOCK, Items.GOLD_INGOT, 1);
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICKS, ModBlocks.CUT_GOLD_BLOCK, 4);
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILES, ModBlocks.GOLD_BRICKS, 4);

        offerDeconstruct(recipeExporter, RecipeCategory.MISC, Items.GOLD_INGOT, ModBlocks.CUT_GOLD_BLOCK, 4);
        offerDeconstruct(recipeExporter, RecipeCategory.MISC, Items.GOLD_INGOT, ModBlocks.GOLD_BRICKS, 4);
        offerDeconstruct(recipeExporter, RecipeCategory.MISC, Items.GOLD_INGOT, ModBlocks.GOLD_TILES, 4);

        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.GOLD_BRICK_SLAB,1);

        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.CUT_GOLD_BLOCK, 2);
        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_PILLAR, 2);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_SLAB, ModBlocks.CUT_GOLD_BLOCK);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_STAIRS, ModBlocks.GOLD_BRICKS);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_SLAB, ModBlocks.GOLD_BRICKS);
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_WALL, ModBlocks.GOLD_BRICKS);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.GOLD_TILES);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.GOLD_TILES);
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.GOLD_TILES);

        offerDoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.GOLD_DOOR, Items.GOLD_INGOT);
        offerTrapdoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.GOLD_TRAPDOOR, Items.GOLD_INGOT);

        // STONECUTTING FROM CUT_GOLD_BLOCK
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_SLAB, ModBlocks.CUT_GOLD_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICKS, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_SLAB, ModBlocks.CUT_GOLD_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_WALL, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILES, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.CUT_GOLD_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.CUT_GOLD_BLOCK);

        // STONECUTTING FROM GOLD_BRICKS
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_SLAB, ModBlocks.GOLD_BRICKS,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_STAIRS, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_WALL, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILES, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.GOLD_BRICKS,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_BRICKS);

        // STONECUTTING FROM GOLD_TILES
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.GOLD_TILES,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.GOLD_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.GOLD_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.GOLD_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.GOLD_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_TILES);

        // STONECUTTING FROM GOLD_PILLAR
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_PILLAR);

        // DIAMOND BLOCKS CRAFTING TABLE RECIPES
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_BLOCK, Items.DIAMOND, 1);
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICKS, ModBlocks.CUT_DIAMOND_BLOCK, 4);
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILES, ModBlocks.DIAMOND_BRICKS, 4);

        offerDeconstruct(recipeExporter, RecipeCategory.MISC, Items.DIAMOND, ModBlocks.CUT_DIAMOND_BLOCK, 4);
        offerDeconstruct(recipeExporter, RecipeCategory.MISC, Items.DIAMOND, ModBlocks.DIAMOND_BRICKS, 4);
        offerDeconstruct(recipeExporter, RecipeCategory.MISC, Items.DIAMOND, ModBlocks.DIAMOND_TILES, 4);

        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.DIAMOND_BRICK_SLAB,1);

        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.CUT_DIAMOND_BLOCK, 2);
        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_PILLAR, 2);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_SLAB, ModBlocks.CUT_DIAMOND_BLOCK);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_STAIRS, ModBlocks.DIAMOND_BRICKS);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_SLAB, ModBlocks.DIAMOND_BRICKS);
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_WALL, ModBlocks.DIAMOND_BRICKS);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.DIAMOND_TILES);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.DIAMOND_TILES);
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.DIAMOND_TILES);

        offerDoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.DIAMOND_DOOR, Items.DIAMOND);
        offerTrapdoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.DIAMOND_TRAPDOOR, Items.DIAMOND);

        // STONECUTTING FROM CUT_DIAMOND_BLOCK
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_SLAB, ModBlocks.CUT_DIAMOND_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICKS, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_SLAB, ModBlocks.CUT_DIAMOND_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_WALL, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILES, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.CUT_DIAMOND_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.CUT_DIAMOND_BLOCK);

        // STONECUTTING FROM DIAMOND_BRICKS
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_SLAB, ModBlocks.DIAMOND_BRICKS,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_STAIRS, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_WALL, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILES, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.DIAMOND_BRICKS,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_BRICKS);

        // STONECUTTING FROM DIAMOND_TILES
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.DIAMOND_TILES,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.DIAMOND_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.DIAMOND_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.DIAMOND_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.DIAMOND_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_TILES);

        // STONECUTTING FROM DIAMOND_PILLAR
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_PILLAR);

    }

    public static void offerDeconstruct(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input, int count) {
        ShapelessRecipeJsonBuilder.create(category, output, count)
                .input(input)
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output) + "_from_" + getRecipeName(input)));
    }

    public static void offer2x2Recipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input, int count) {
        ShapedRecipeJsonBuilder.create(category, output, count)
                .input('#', input)
                .pattern("##")
                .pattern("##")
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output)));
    }

    public static void offer1x2Recipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input, int count) {
        ShapedRecipeJsonBuilder.create(category, output, count)
                .input('#', input)
                .pattern("#")
                .pattern("#")
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output)));
    }

    public static void offerStairsRecipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(category, output, 4)
                .input('#', input)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output)));
    }

    public static void offerDoorRecipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(category, output, 3)
                .input('#', input)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output)));
    }

    public static void offerTrapdoorRecipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(category, output, 2)
                .input('#', input)
                .pattern("###")
                .pattern("###")
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output)));
    }
}
