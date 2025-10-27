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

    public static final ChipItem BASIC_CHIP = registerCustomItem("basic_chip", new ChipItem(new Item.Settings(),100));
    public static final ChipItem COPPER_CHIP = registerCustomItem("copper_chip", new ChipItem(new Item.Settings(),1000));
    public static final ChipItem IRON_CHIP = registerCustomItem("iron_chip", new ChipItem(new Item.Settings(),5000));
    public static final ChipItem AMETHYST_CHIP = registerCustomItem("amethyst_chip", new ChipItem(new Item.Settings(),25000));
    public static final ChipItem GOLDEN_CHIP = registerCustomItem("golden_chip", new ChipItem(new Item.Settings(),100000));
    public static final ChipItem EMERALD_CHIP = registerCustomItem("emerald_chip", new ChipItem(new Item.Settings(),1000000));
    public static final ChipItem DIAMOND_CHIP = registerCustomItem("diamond_chip", new ChipItem(new Item.Settings(),10000000));
    public static final ChipItem NETHERITE_CHIP = registerCustomItem("netherite_chip", new ChipItem(new Item.Settings(),100000000));

    public static final Item COPPER_COIN = registerItem("copper_coin", new Item(new Item.Settings()));
    public static final Item IRON_COIN = registerItem("iron_coin", new Item(new Item.Settings()));
    public static final Item GOLD_COIN = registerItem("gold_coin", new Item(new Item.Settings()));
    public static final Item DIAMOND_COIN = registerItem("diamond_coin", new Item(new Item.Settings()));

    public static final ItemGachaponItem ITEM_POKE_GACHAPON = registerCustomItem("item_poke_gachapon", new ItemGachaponItem(new Item.Settings(), "pokeball"));
    public static final ItemGachaponItem ITEM_SUPER_GACHAPON = registerCustomItem("item_super_gachapon", new ItemGachaponItem(new Item.Settings(), "superball"));
    public static final ItemGachaponItem ITEM_ULTRA_GACHAPON = registerCustomItem("item_ultra_gachapon", new ItemGachaponItem(new Item.Settings(), "ultraball"));
    public static final ItemGachaponItem ITEM_MASTER_GACHAPON = registerCustomItem("item_master_gachapon", new ItemGachaponItem(new Item.Settings(), "masterball"));

    public static final PokemonGachaponItem POKEMON_POKE_GACHAPON = registerCustomItem("pokemon_poke_gachapon", new PokemonGachaponItem(new Item.Settings(), "pokeball"));
    public static final PokemonGachaponItem POKEMON_SUPER_GACHAPON = registerCustomItem("pokemon_super_gachapon", new PokemonGachaponItem(new Item.Settings(), "superball"));
    public static final PokemonGachaponItem POKEMON_ULTRA_GACHAPON = registerCustomItem("pokemon_ultra_gachapon", new PokemonGachaponItem(new Item.Settings(), "ultraball"));
    public static final PokemonGachaponItem POKEMON_MASTER_GACHAPON = registerCustomItem("pokemon_master_gachapon", new PokemonGachaponItem(new Item.Settings(), "masterball"));

    public static final PokemonPinItem LITWICK_PIN = registerCustomItem("litwick_pin",
            new PokemonPinItem(new Item.Settings(), 15));
    public static final PokemonPinItem STARYU_PIN = registerCustomItem("staryu_pin",
            new PokemonPinItem(new Item.Settings(), 15));
    public static final PokemonPinItem BELLSPROUT_PIN = registerCustomItem("bellsprout_pin",
            new PokemonPinItem(new Item.Settings(), 15));
    public static final PokemonPinItem TYROGUE_PIN = registerCustomItem("tyrogue_pin",
            new PokemonPinItem(new Item.Settings(), 15));
    public static final PokemonPinItem SCYTHER_PIN = registerCustomItem("scyther_pin",
            new PokemonPinItem(new Item.Settings(), 15));
    public static final PokemonPinItem EEVEE_PIN = registerCustomItem("eevee_pin",
            new PokemonPinItem(new Item.Settings(), 21));
    public static final PokemonPinItem DRATINI_PIN = registerCustomItem("dratini_pin",
            new PokemonPinItem(new Item.Settings(), 15));
    public static final PokemonPinItem ROTOM_PIN = registerCustomItem("rotom_pin",
            new PokemonPinItem(new Item.Settings(), 21));
    public static final PokemonPinItem DITTO_PIN = registerCustomItem("ditto_pin",
            new PokemonPinItem(new Item.Settings(), 31));
    public static final PokemonPinItem PORYGON_PIN = registerCustomItem("porygon_pin",
            new PokemonPinItem(new Item.Settings(), 15));

    public static List<BillItem> BILL_LIST = new ArrayList<>();
    public static final BillItem BILL_100 = registerBillItem("bill_100", new BillItem(new Item.Settings(), 100));
    public static final BillItem BILL_500 = registerBillItem("bill_500", new BillItem(new Item.Settings(), 500));
    public static final BillItem BILL_1K = registerBillItem("bill_1k", new BillItem(new Item.Settings(), 1_000));
    public static final BillItem BILL_5K = registerBillItem("bill_5k", new BillItem(new Item.Settings(), 5_000));
    public static final BillItem BILL_10K = registerBillItem("bill_10k", new BillItem(new Item.Settings(), 10_000));
    public static final BillItem BILL_25K = registerBillItem("bill_25k", new BillItem(new Item.Settings(), 25_000));
    public static final BillItem BILL_50K = registerBillItem("bill_50k", new BillItem(new Item.Settings(), 50_000));
    public static final BillItem BILL_100K = registerBillItem("bill_100k", new BillItem(new Item.Settings(), 100_000));
    public static final BillItem BILL_500K = registerBillItem("bill_500k", new BillItem(new Item.Settings(), 500_000));
    public static final BillItem BILL_1M = registerBillItem("bill_1m", new BillItem(new Item.Settings(), 1_000_000));
    public static final BillItem BILL_10M = registerBillItem("bill_10m", new BillItem(new Item.Settings(), 10_000_000));
    public static final BillItem BILL_100M = registerBillItem("bill_100m", new BillItem(new Item.Settings(), 100_000_000));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(CasinoRocket.MOD_ID, name), item);
    }

    private static <T extends Item> T registerCustomItem(String name, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(CasinoRocket.MOD_ID, name), item);
    }

    private static BillItem registerBillItem(String name, BillItem item) {
        BillItem newBillItem = Registry.register(Registries.ITEM, Identifier.of(CasinoRocket.MOD_ID, name), item);
        BILL_LIST.add(newBillItem);
        return newBillItem;
    }

    public static void registerModItems() {
        CasinoRocket.LOGGER.info("Registering Mod Items for " + CasinoRocket.MOD_ID);
    }

}