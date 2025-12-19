package net.andrespr.casinorocket.block;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.custom.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    // GOLD BLOCKS

    public static final Block CUT_GOLD_BLOCK = registerBlock("cut_gold_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .instrument(NoteBlockInstrument.BELL)
                    .requiresTool()
                    .strength(2.5F, 5.5F)
                    .sounds(BlockSoundGroup.METAL)
            )
    );

    public static final Block CUT_GOLD_STAIRS = registerBlock("cut_gold_stairs",
            new StairsBlock(ModBlocks.CUT_GOLD_BLOCK.getDefaultState(), AbstractBlock.Settings.copy(CUT_GOLD_BLOCK)));

    public static final Block CUT_GOLD_SLAB = registerBlock("cut_gold_slab",
            new SlabBlock(AbstractBlock.Settings.copy(CUT_GOLD_BLOCK)));

    public static final Block GOLD_BRICKS = registerBlock("gold_bricks",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .instrument(NoteBlockInstrument.BELL)
                    .requiresTool()
                    .strength(2.5F, 5.5F)
                    .sounds(BlockSoundGroup.METAL)
            )
    );

    public static final Block GOLD_BRICK_STAIRS = registerBlock("gold_brick_stairs",
            new StairsBlock(ModBlocks.GOLD_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(GOLD_BRICKS)));

    public static final Block GOLD_BRICK_SLAB = registerBlock("gold_brick_slab",
            new SlabBlock(AbstractBlock.Settings.copy(GOLD_BRICKS)));

    public static final Block GOLD_BRICK_WALL = registerBlock("gold_brick_wall",
            new WallBlock(AbstractBlock.Settings.copy(GOLD_BRICKS)));

    public static final Block GOLD_TILES = registerBlock("gold_tiles",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .instrument(NoteBlockInstrument.BELL)
                    .requiresTool()
                    .strength(2.5F, 5.5F)
                    .sounds(BlockSoundGroup.METAL)
            )
    );

    public static final Block GOLD_TILE_STAIRS = registerBlock("gold_tile_stairs",
            new StairsBlock(ModBlocks.GOLD_TILES.getDefaultState(), AbstractBlock.Settings.copy(GOLD_TILES)));

    public static final Block GOLD_TILE_SLAB = registerBlock("gold_tile_slab",
            new SlabBlock(AbstractBlock.Settings.copy(GOLD_TILES)));

    public static final Block GOLD_TILE_WALL = registerBlock("gold_tile_wall",
            new WallBlock(AbstractBlock.Settings.copy(GOLD_TILES)));

    public static final Block CHISELED_GOLD_BLOCK = registerBlock("chiseled_gold_block",
            new PillarBlock(AbstractBlock.Settings.copy(CUT_GOLD_BLOCK)));

    public static final Block GOLD_PILLAR = registerBlock("gold_pillar",
            new PillarBlock(AbstractBlock.Settings.copy(CUT_GOLD_BLOCK)));

    public static final Block HEAVY_GOLD_PILLAR = registerBlock("heavy_gold_pillar",
            new PillarBlock(AbstractBlock.Settings.copy(CUT_GOLD_BLOCK)));

    public static final Block GOLD_DOOR = registerBlock("gold_door",
            new DoorBlock(BlockSetType.GOLD, AbstractBlock.Settings.copy(CUT_GOLD_BLOCK).nonOpaque()));

    public static final Block GOLD_TRAPDOOR = registerBlock("gold_trapdoor",
            new TrapdoorBlock(BlockSetType.GOLD, AbstractBlock.Settings.copy(CUT_GOLD_BLOCK).nonOpaque()));

    // DIAMOND BLOCKS

    public static final Block CUT_DIAMOND_BLOCK = registerBlock("cut_diamond_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .requiresTool()
                    .strength(4.5F, 5.5F)
                    .sounds(BlockSoundGroup.METAL)
            )
    );

    public static final Block CUT_DIAMOND_STAIRS = registerBlock("cut_diamond_stairs",
            new StairsBlock(ModBlocks.CUT_DIAMOND_BLOCK.getDefaultState(), AbstractBlock.Settings.copy(CUT_DIAMOND_BLOCK)));

    public static final Block CUT_DIAMOND_SLAB = registerBlock("cut_diamond_slab",
            new SlabBlock(AbstractBlock.Settings.copy(CUT_DIAMOND_BLOCK)));

    public static final Block DIAMOND_BRICKS = registerBlock("diamond_bricks",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .requiresTool()
                    .strength(4.5F, 5.5F)
                    .sounds(BlockSoundGroup.METAL)
            )
    );

    public static final Block DIAMOND_BRICK_STAIRS = registerBlock("diamond_brick_stairs",
            new StairsBlock(ModBlocks.DIAMOND_BRICKS.getDefaultState(), AbstractBlock.Settings.copy(DIAMOND_BRICKS)));

    public static final Block DIAMOND_BRICK_SLAB = registerBlock("diamond_brick_slab",
            new SlabBlock(AbstractBlock.Settings.copy(DIAMOND_BRICKS)));

    public static final Block DIAMOND_BRICK_WALL = registerBlock("diamond_brick_wall",
            new WallBlock(AbstractBlock.Settings.copy(DIAMOND_BRICKS)));

    public static final Block DIAMOND_TILES = registerBlock("diamond_tiles",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .requiresTool()
                    .strength(4.5F, 5.5F)
                    .sounds(BlockSoundGroup.METAL)
            )
    );

    public static final Block DIAMOND_TILE_STAIRS = registerBlock("diamond_tile_stairs",
            new StairsBlock(ModBlocks.DIAMOND_TILES.getDefaultState(), AbstractBlock.Settings.copy(DIAMOND_TILES)));

    public static final Block DIAMOND_TILE_SLAB = registerBlock("diamond_tile_slab",
            new SlabBlock(AbstractBlock.Settings.copy(DIAMOND_TILES)));

    public static final Block DIAMOND_TILE_WALL = registerBlock("diamond_tile_wall",
            new WallBlock(AbstractBlock.Settings.copy(DIAMOND_TILES)));

    public static final Block CHISELED_DIAMOND_BLOCK = registerBlock("chiseled_diamond_block",
            new PillarBlock(AbstractBlock.Settings.copy(CUT_DIAMOND_BLOCK)));

    public static final Block DIAMOND_PILLAR = registerBlock("diamond_pillar",
            new PillarBlock(AbstractBlock.Settings.copy(CUT_DIAMOND_BLOCK)));

    public static final Block HEAVY_DIAMOND_PILLAR = registerBlock("heavy_diamond_pillar",
            new PillarBlock(AbstractBlock.Settings.copy(CUT_DIAMOND_BLOCK)));

    public static final Block DIAMOND_DOOR = registerBlock("diamond_door",
            new DoorBlock(BlockSetType.GOLD, AbstractBlock.Settings.copy(CUT_DIAMOND_BLOCK).nonOpaque()));

    public static final Block DIAMOND_TRAPDOOR = registerBlock("diamond_trapdoor",
            new TrapdoorBlock(BlockSetType.GOLD, AbstractBlock.Settings.copy(CUT_DIAMOND_BLOCK).nonOpaque()));

    // ENTITIES

    public static final Block GACHA_MACHINE = registerBlock("gacha_machine",
            new GachaMachineBlock(AbstractBlock.Settings.create()
                    .strength(-1.0F, 3600000.0F)
                    .dropsNothing()
                    .allowsSpawning(Blocks::never)
                    .nonOpaque()
            )
    );

    public static final Block POKEMON_GACHA_MACHINE = registerBlock("pokemon_gacha_machine",
            new PokemonGachaMachineBlock(AbstractBlock.Settings.create()
                    .strength(-1.0F, 3600000.0F)
                    .dropsNothing()
                    .allowsSpawning(Blocks::never)
                    .nonOpaque()
            )
    );

    public static final Block PLUSHIES_GACHA_MACHINE = registerBlock("plushies_gacha_machine",
            new PlushiesGachaMachineBlock(AbstractBlock.Settings.create()
                    .strength(-1.0F, 3600000.0F)
                    .dropsNothing()
                    .allowsSpawning(Blocks::never)
                    .nonOpaque()
            )
    );

    public static final Block EVENT_GACHA_MACHINE = registerBlock("event_gacha_machine",
            new EventGachaMachineBlock(AbstractBlock.Settings.create()
                    .strength(-1.0F, 3600000.0F)
                    .dropsNothing()
                    .allowsSpawning(Blocks::never)
                    .nonOpaque()
            )
    );

    public static final Block SLOT_MACHINE = registerBlock("slot_machine",
            new SlotMachineBlock(AbstractBlock.Settings.create()
                    .strength(-1.0F, 3600000.0F)
                    .dropsNothing()
                    .allowsSpawning(Blocks::never)
                    .luminance(state -> 14)
                    .nonOpaque()
            )
    );

    public static final Block BLACKJACK_TABLE = registerBlock("blackjack_table",
            new BlackjackTableBlock(AbstractBlock.Settings.create()
                    .strength(-1.0F, 3600000.0F)
                    .dropsNothing()
                    .allowsSpawning(Blocks::never)
                    .nonOpaque()
            )
    );

    public static final Block CHIP_TABLE = registerBlock("chip_table",
            new ChipTableBlock(AbstractBlock.Settings.create()
                    .strength(-1.0F, 3600000.0F)
                    .dropsNothing()
                    .allowsSpawning(Blocks::never)
                    .nonOpaque()
            )
    );

    // METHODS

    private static Block registerBlock(String name, Block block)  {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(CasinoRocket.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(CasinoRocket.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        CasinoRocket.LOGGER.info("Registering Mod Blocks for " + CasinoRocket.MOD_ID);
    }

}