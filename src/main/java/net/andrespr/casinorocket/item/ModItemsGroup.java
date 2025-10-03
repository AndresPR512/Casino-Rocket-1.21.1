package net.andrespr.casinorocket.item;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.datagen.ModRecipeProvider;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemsGroup {

    public static final ItemGroup CASINO_ROCKET_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(CasinoRocket.MOD_ID, "casino_rocket_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.DIAMOND_CHIP))
                    .displayName(Text.translatable("itemgroup.casinorocket.casino_rocket_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.BASIC_CHIP);
                        entries.add(ModItems.COPPER_CHIP);
                        entries.add(ModItems.IRON_CHIP);
                        entries.add(ModItems.AMETHYST_CHIP);
                        entries.add(ModItems.GOLDEN_CHIP);
                        entries.add(ModItems.EMERALD_CHIP);
                        entries.add(ModItems.DIAMOND_CHIP);
                        entries.add(ModItems.NETHERITE_CHIP);
                    }).build());

    public static final ItemGroup CASINO_ROCKET_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(CasinoRocket.MOD_ID, "casino_rocket_blocks"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.GOLD_BRICKS))
                    .displayName(Text.translatable("itemgroup.casinorocket.casino_rocket_blocks"))
                    .entries((displayContext, entries) -> {
                        // ADDING GOLD BLOCKS
                        entries.add(ModBlocks.CUT_GOLD_BLOCK);
                        entries.add(ModBlocks.CUT_GOLD_STAIRS);
                        entries.add(ModBlocks.CUT_GOLD_SLAB);
                        entries.add(ModBlocks.GOLD_BRICKS);
                        entries.add(ModBlocks.GOLD_BRICK_STAIRS);
                        entries.add(ModBlocks.GOLD_BRICK_SLAB);
                        entries.add(ModBlocks.GOLD_BRICK_WALL);
                        entries.add(ModBlocks.GOLD_TILES);
                        entries.add(ModBlocks.GOLD_TILE_STAIRS);
                        entries.add(ModBlocks.GOLD_TILE_SLAB);
                        entries.add(ModBlocks.GOLD_TILE_WALL);
                        entries.add(ModBlocks.CHISELED_GOLD_BLOCK);
                        entries.add(ModBlocks.GOLD_PILLAR);
                        entries.add(ModBlocks.HEAVY_GOLD_PILLAR);
                        entries.add(ModBlocks.GOLD_DOOR);
                        entries.add(ModBlocks.GOLD_TRAPDOOR);
                        // ADDING DIAMOND BLOCKS
                        entries.add(ModBlocks.CUT_DIAMOND_BLOCK);
                        entries.add(ModBlocks.CUT_DIAMOND_STAIRS);
                        entries.add(ModBlocks.CUT_DIAMOND_SLAB);
                        entries.add(ModBlocks.DIAMOND_BRICKS);
                        entries.add(ModBlocks.DIAMOND_BRICK_STAIRS);
                        entries.add(ModBlocks.DIAMOND_BRICK_SLAB);
                        entries.add(ModBlocks.DIAMOND_BRICK_WALL);
                        entries.add(ModBlocks.DIAMOND_TILES);
                        entries.add(ModBlocks.DIAMOND_TILE_STAIRS);
                        entries.add(ModBlocks.DIAMOND_TILE_SLAB);
                        entries.add(ModBlocks.DIAMOND_TILE_WALL);
                        entries.add(ModBlocks.CHISELED_DIAMOND_BLOCK);
                        entries.add(ModBlocks.DIAMOND_PILLAR);
                        entries.add(ModBlocks.HEAVY_DIAMOND_PILLAR);
                        entries.add(ModBlocks.DIAMOND_DOOR);
                        entries.add(ModBlocks.DIAMOND_TRAPDOOR);
                    }).build());

    public static void registerItemGroups() {
        CasinoRocket.LOGGER.info("Registering Item Groups for " + CasinoRocket.MOD_ID);
    }
}