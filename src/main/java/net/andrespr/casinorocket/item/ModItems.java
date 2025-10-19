package net.andrespr.casinorocket.item;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.andrespr.casinorocket.item.custom.PokemonPinItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final ChipItem BASIC_CHIP = registerCustomItem("basic_chip", new ChipItem(new Item.Settings(),100));
    public static final ChipItem COPPER_CHIP = registerCustomItem("copper_chip", new ChipItem(new Item.Settings(),1000));
    public static final ChipItem IRON_CHIP = registerCustomItem("iron_chip", new ChipItem(new Item.Settings(),5000));
    public static final ChipItem AMETHYST_CHIP = registerCustomItem("amethyst_chip", new ChipItem(new Item.Settings(),25000));
    public static final ChipItem GOLDEN_CHIP = registerCustomItem("golden_chip", new ChipItem(new Item.Settings(),100000));
    public static final ChipItem EMERALD_CHIP = registerCustomItem("emerald_chip", new ChipItem(new Item.Settings(),1000000));
    public static final ChipItem DIAMOND_CHIP = registerCustomItem("diamond_chip", new ChipItem(new Item.Settings(),10000000));
    public static final ChipItem NETHERITE_CHIP =registerCustomItem("netherite_chip", new ChipItem(new Item.Settings(),100000000));

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

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(CasinoRocket.MOD_ID, name), item);
    }

    private static <T extends Item> T registerCustomItem(String name, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(CasinoRocket.MOD_ID, name), item);
    }

    public static void registerModItems() {
        CasinoRocket.LOGGER.info("Registering Mod Items for " + CasinoRocket.MOD_ID);
    }

}