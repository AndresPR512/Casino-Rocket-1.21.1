package net.andrespr.casinorocket.item;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.ModBlocks;
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
                        // CHIP ITEMS
                        entries.add(ModItems.BASIC_CHIP);
                        entries.add(ModItems.RED_CHIP);
                        entries.add(ModItems.BLUE_CHIP);
                        entries.add(ModItems.PURPLE_CHIP);
                        entries.add(ModItems.COPPER_CHIP);
                        entries.add(ModItems.IRON_CHIP);
                        entries.add(ModItems.EMERALD_CHIP);
                        entries.add(ModItems.GOLD_CHIP);
                        entries.add(ModItems.DIAMOND_CHIP);
                        entries.add(ModItems.NETHERITE_CHIP);
                        // COINS
                        entries.add(ModItems.COPPER_COIN);
                        entries.add(ModItems.IRON_COIN);
                        entries.add(ModItems.GOLD_COIN);
                        entries.add(ModItems.DIAMOND_COIN);
                        entries.add(ModItems.EVENT_COIN);
                        entries.add(ModItems.PRIMOGEM);
                        // GACHAPON
                        entries.add(ModItems.POKE_GACHAPON);
                        entries.add(ModItems.GREAT_GACHAPON);
                        entries.add(ModItems.ULTRA_GACHAPON);
                        entries.add(ModItems.MASTER_GACHAPON);
                        entries.add(ModItems.CHERISH_GACHAPON);
                        entries.add(ModItems.PREMIER_GACHAPON);
                        entries.add(ModItems.EVENT_GACHAPON);
                        entries.add(ModItems.POKEMON_POKE_GACHAPON);
                        entries.add(ModItems.POKEMON_GREAT_GACHAPON);
                        entries.add(ModItems.POKEMON_ULTRA_GACHAPON);
                        entries.add(ModItems.POKEMON_MASTER_GACHAPON);
                        entries.add(ModItems.POKEMON_CHERISH_GACHAPON);
                        entries.add(ModItems.POKEMON_EVENT_GACHAPON);
                        // BILLS
                        entries.add(ModItems.BILL_10);
                        entries.add(ModItems.BILL_50);
                        entries.add(ModItems.BILL_100);
                        entries.add(ModItems.BILL_500);
                        entries.add(ModItems.BILL_1K);
                        entries.add(ModItems.BILL_5K);
                        entries.add(ModItems.BILL_10K);
                        entries.add(ModItems.BILL_25K);
                        entries.add(ModItems.BILL_50K);
                        entries.add(ModItems.BILL_100K);
                        entries.add(ModItems.BILL_500K);
                        entries.add(ModItems.BILL_1M);
                        entries.add(ModItems.BILL_10M);
                        entries.add(ModItems.BILL_100M);
                        // WALLET
                        entries.add(ModItems.WALLET);
                        // DISCS
                        entries.add(ModItems.FIRERED_GC_MUSIC_DISC);
                        entries.add(ModItems.HEARTGOLD_GC_MUSIC_DISC);
                        entries.add(ModItems.EMERALD_GC_MUSIC_DISC);
                        entries.add(ModItems.PLATINUM_GC_MUSIC_DISC);
                    }).build());

    public static final ItemGroup CASINO_ROCKET_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(CasinoRocket.MOD_ID, "casino_rocket_blocks"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.GOLD_BRICKS))
                    .displayName(Text.translatable("itemgroup.casinorocket.casino_rocket_blocks"))
                    .entries((displayContext, entries) -> {
                        // GOLD BLOCKS
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
                        // DIAMOND BLOCKS
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
                        // GACHA MACHINES
                        entries.add(ModBlocks.POKEMON_GACHA_MACHINE);
                        entries.add(ModBlocks.GACHA_MACHINE);
                        entries.add(ModBlocks.PLUSHIES_GACHA_MACHINE);
                        entries.add(ModBlocks.EVENT_GACHA_MACHINE);
                        // SLOT MACHINE
                        entries.add(ModBlocks.SLOT_MACHINE);
                        // BLACKJACK TABLE
                        entries.add(ModBlocks.BLACKJACK_TABLE);
                        // CHIP TABLE
                        entries.add(ModBlocks.CHIP_TABLE);
                    }).build());

    public static void registerItemGroups() {
        CasinoRocket.LOGGER.info("Registering Item Groups for " + CasinoRocket.MOD_ID);
    }

}