package net.andrespr.casinorocket.item;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item BASIC_CHIP = registerItem("basic_chip", new ChipItem(new Item.Settings(),"tooltip.casinorocket.basic_chip"));
    public static final Item COPPER_CHIP = registerItem("copper_chip", new ChipItem(new Item.Settings(),"tooltip.casinorocket.copper_chip"));
    public static final Item IRON_CHIP = registerItem("iron_chip", new ChipItem(new Item.Settings(),"tooltip.casinorocket.iron_chip"));
    public static final Item AMETHYST_CHIP = registerItem("amethyst_chip", new ChipItem(new Item.Settings(),"tooltip.casinorocket.amethyst_chip"));
    public static final Item GOLDEN_CHIP = registerItem("golden_chip", new ChipItem(new Item.Settings(),"tooltip.casinorocket.golden_chip"));
    public static final Item EMERALD_CHIP = registerItem("emerald_chip", new ChipItem(new Item.Settings(),"tooltip.casinorocket.emerald_chip"));
    public static final Item DIAMOND_CHIP = registerItem("diamond_chip", new ChipItem(new Item.Settings(),"tooltip.casinorocket.diamond_chip"));
    public static final Item NETHERITE_CHIP = registerItem("netherite_chip", new ChipItem(new Item.Settings(),"tooltip.casinorocket.netherite_chip"));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(CasinoRocket.MOD_ID, name), item);
    }

    public static void registerModItems() {
        CasinoRocket.LOGGER.info("Registering Mod Items for " + CasinoRocket.MOD_ID);
    }

}