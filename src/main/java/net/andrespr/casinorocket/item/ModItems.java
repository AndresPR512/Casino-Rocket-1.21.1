package net.andrespr.casinorocket.item;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.custom.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final List<Item> ALL_CHIP_ITEMS = new ArrayList<>();
    public static final ChipItem BASIC_CHIP = registerChipItem("basic_chip");
    public static final ChipItem COPPER_CHIP = registerChipItem("copper_chip");
    public static final ChipItem IRON_CHIP = registerChipItem("iron_chip");
    public static final ChipItem AMETHYST_CHIP = registerChipItem("amethyst_chip");
    public static final ChipItem GOLD_CHIP = registerChipItem("gold_chip");
    public static final ChipItem EMERALD_CHIP = registerChipItem("emerald_chip");
    public static final ChipItem DIAMOND_CHIP = registerChipItem("diamond_chip");
    public static final ChipItem NETHERITE_CHIP = registerChipItem("netherite_chip");

    public static final Item COPPER_COIN = registerItem("copper_coin", new Item(new Item.Settings()));
    public static final Item IRON_COIN = registerItem("iron_coin", new Item(new Item.Settings()));
    public static final Item GOLD_COIN = registerItem("gold_coin", new Item(new Item.Settings()));
    public static final Item DIAMOND_COIN = registerItem("diamond_coin", new Item(new Item.Settings()));

    public static final List<Item> ALL_GACHAPON_ITEMS = new ArrayList<>();
    public static final GachaponItem POKE_GACHAPON = registerCustomItem("poke_gachapon",
            new GachaponItem(new Item.Settings(), "common"));
    public static final GachaponItem GREAT_GACHAPON = registerCustomItem("great_gachapon",
            new GachaponItem(new Item.Settings(), "uncommon"));
    public static final GachaponItem ULTRA_GACHAPON = registerCustomItem("ultra_gachapon",
            new GachaponItem(new Item.Settings(), "rare"));
    public static final GachaponItem MASTER_GACHAPON = registerCustomItem("master_gachapon",
            new GachaponItem(new Item.Settings(), "ultrarare"));
    public static final GachaponItem CHERISH_GACHAPON = registerCustomItem("cherish_gachapon",
            new GachaponItem(new Item.Settings(), "legendary"));
    public static final GachaponItem PREMIER_GACHAPON = registerCustomItem("premier_gachapon",
            new GachaponItem(new Item.Settings(), "bonus"));
    public static final PokemonGachaponItem POKEMON_POKE_GACHAPON = registerCustomItem("pokemon_poke_gachapon",
            new PokemonGachaponItem(new Item.Settings(), "common"));
    public static final PokemonGachaponItem POKEMON_GREAT_GACHAPON = registerCustomItem("pokemon_great_gachapon",
            new PokemonGachaponItem(new Item.Settings(), "uncommon"));
    public static final PokemonGachaponItem POKEMON_ULTRA_GACHAPON = registerCustomItem("pokemon_ultra_gachapon",
            new PokemonGachaponItem(new Item.Settings(), "rare"));
    public static final PokemonGachaponItem POKEMON_MASTER_GACHAPON = registerCustomItem("pokemon_master_gachapon",
            new PokemonGachaponItem(new Item.Settings(), "ultrarare"));
    public static final PokemonGachaponItem POKEMON_CHERISH_GACHAPON = registerCustomItem("pokemon_cherish_gachapon",
            new PokemonGachaponItem(new Item.Settings(), "legendary"));
    public static final PokemonGachaponItem POKEMON_PREMIER_GACHAPON = registerCustomItem("pokemon_premier_gachapon",
            new PokemonGachaponItem(new Item.Settings(), "bonus"));

    public static final PokemonPinItem LITWICK_PIN = registerCustomItem("litwick_pin",
            new PokemonPinItem(new Item.Settings(), 5, 15, false));
    public static final PokemonPinItem STARYU_PIN = registerCustomItem("staryu_pin",
            new PokemonPinItem(new Item.Settings(), 5, 15, false));
    public static final PokemonPinItem BELLSPROUT_PIN = registerCustomItem("bellsprout_pin",
            new PokemonPinItem(new Item.Settings(), 5, 15, false));
    public static final PokemonPinItem TYROGUE_PIN = registerCustomItem("tyrogue_pin",
            new PokemonPinItem(new Item.Settings(), 10, 15, false));
    public static final PokemonPinItem SCYTHER_PIN = registerCustomItem("scyther_pin",
            new PokemonPinItem(new Item.Settings(), 10, 15, false));
    public static final PokemonPinItem EEVEE_PIN = registerCustomItem("eevee_pin",
            new PokemonPinItem(new Item.Settings(), 10, 21, false));
    public static final PokemonPinItem DRATINI_PIN = registerCustomItem("dratini_pin",
            new PokemonPinItem(new Item.Settings(), 10, 15, false));
    public static final PokemonPinItem ROTOM_PIN = registerCustomItem("rotom_pin",
            new PokemonPinItem(new Item.Settings(), 10, 21, false));
    public static final PokemonPinItem DITTO_PIN = registerCustomItem("ditto_pin",
            new PokemonPinItem(new Item.Settings(), 5, 31, false));
    public static final PokemonPinItem PORYGON_PIN = registerCustomItem("porygon_pin",
            new PokemonPinItem(new Item.Settings(), 10, 15, false));

    public static List<Item> ALL_BILL_ITEMS = new ArrayList<>();
    public static final BillItem BILL_100 = registerCustomItem("bill_100", new BillItem(new Item.Settings(), 100));
    public static final BillItem BILL_500 = registerCustomItem("bill_500", new BillItem(new Item.Settings(), 500));
    public static final BillItem BILL_1K = registerCustomItem("bill_1k", new BillItem(new Item.Settings(), 1_000));
    public static final BillItem BILL_5K = registerCustomItem("bill_5k", new BillItem(new Item.Settings(), 5_000));
    public static final BillItem BILL_10K = registerCustomItem("bill_10k", new BillItem(new Item.Settings(), 10_000));
    public static final BillItem BILL_25K = registerCustomItem("bill_25k", new BillItem(new Item.Settings(), 25_000));
    public static final BillItem BILL_50K = registerCustomItem("bill_50k", new BillItem(new Item.Settings(), 50_000));
    public static final BillItem BILL_100K = registerCustomItem("bill_100k", new BillItem(new Item.Settings(), 100_000));
    public static final BillItem BILL_500K = registerCustomItem("bill_500k", new BillItem(new Item.Settings(), 500_000));
    public static final BillItem BILL_1M = registerCustomItem("bill_1m", new BillItem(new Item.Settings(), 1_000_000));
    public static final BillItem BILL_10M = registerCustomItem("bill_10m", new BillItem(new Item.Settings(), 10_000_000));
    public static final BillItem BILL_100M = registerCustomItem("bill_100m", new BillItem(new Item.Settings(), 100_000_000));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(CasinoRocket.MOD_ID, name), item);
    }

    private static <T extends Item> T registerCustomItem(String name, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(CasinoRocket.MOD_ID, name), item);
    }

    private static ChipItem registerChipItem(String name) {
        long value = CasinoRocket.CONFIG.casinoChips.getChipPrice(name);
        ChipItem newChipitem = new ChipItem(new Item.Settings(), value);
        Registry.register(Registries.ITEM, Identifier.of(CasinoRocket.MOD_ID, name), newChipitem);
        return newChipitem;
    }

    public static void registerModItems() {
        CasinoRocket.LOGGER.info("Registering Mod Items for " + CasinoRocket.MOD_ID);
    }

}