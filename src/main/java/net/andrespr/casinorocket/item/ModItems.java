package net.andrespr.casinorocket.item;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.custom.*;
import net.andrespr.casinorocket.sound.ModSounds;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final List<Item> ALL_CHIP_ITEMS = new ArrayList<>();
    public static final ChipItem BASIC_CHIP = registerChipItem("basic_chip");
    public static final ChipItem RED_CHIP = registerChipItem("red_chip");
    public static final ChipItem BLUE_CHIP = registerChipItem("blue_chip");
    public static final ChipItem PURPLE_CHIP = registerChipItem("purple_chip");
    public static final ChipItem COPPER_CHIP = registerChipItem("copper_chip");
    public static final ChipItem IRON_CHIP = registerChipItem("iron_chip");
    public static final ChipItem EMERALD_CHIP = registerChipItem("emerald_chip");
    public static final ChipItem GOLD_CHIP = registerChipItem("gold_chip");
    public static final ChipItem DIAMOND_CHIP = registerChipItem("diamond_chip");
    public static final ChipItem NETHERITE_CHIP = registerChipItem("netherite_chip");

    public static final CoinItem COPPER_COIN = registerCustomItem("copper_coin", new CoinItem(new Item.Settings()));
    public static final CoinItem IRON_COIN = registerCustomItem("iron_coin", new CoinItem(new Item.Settings()));
    public static final CoinItem GOLD_COIN = registerCustomItem("gold_coin", new CoinItem(new Item.Settings()));
    public static final CoinItem DIAMOND_COIN = registerCustomItem("diamond_coin", new CoinItem(new Item.Settings()));
    public static final CoinItem EVENT_COIN = registerCustomItem("event_coin", new CoinItem(new Item.Settings()));
    public static final CoinItem PRIMOGEM = registerCustomItem("primogem", new CoinItem(new Item.Settings()));

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
    public static final GachaponItem EVENT_GACHAPON = registerCustomItem("event_gachapon",
            new GachaponItem(new Item.Settings(), "event"));
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
    public static final PokemonGachaponItem POKEMON_EVENT_GACHAPON = registerCustomItem("pokemon_event_gachapon",
            new PokemonGachaponItem(new Item.Settings(), "event"));

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
    public static final BillItem BILL_10 = registerCustomItem("bill_10", new BillItem(new Item.Settings(), 10));
    public static final BillItem BILL_50 = registerCustomItem("bill_50", new BillItem(new Item.Settings(), 50));
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

    public static final WalletItem WALLET = registerCustomItem("wallet", new WalletItem(new Item.Settings()));

    public static final Item FIRERED_GC_MUSIC_DISC = registerItem("firered_gc_music_disc",
            new Item(new Item.Settings().jukeboxPlayable(ModSounds.FIRERED_GC_KEY).maxCount(1)));
    public static final Item HEARTGOLD_GC_MUSIC_DISC = registerItem("heartgold_gc_music_disc",
            new Item(new Item.Settings().jukeboxPlayable(ModSounds.HEARTGOLD_GC_KEY).maxCount(1)));
    public static final Item EMERALD_GC_MUSIC_DISC = registerItem("emerald_gc_music_disc",
            new Item(new Item.Settings().jukeboxPlayable(ModSounds.EMERALD_GC_KEY).maxCount(1)));
    public static final Item PLATINUM_GC_MUSIC_DISC = registerItem("platinum_gc_music_disc",
            new Item(new Item.Settings().jukeboxPlayable(ModSounds.PLATINUM_GC_KEY).maxCount(1)));

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