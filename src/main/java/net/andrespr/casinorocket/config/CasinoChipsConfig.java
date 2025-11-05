package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;

import java.util.Locale;

@Config(name = "casino_chips_config")
public class CasinoChipsConfig implements ConfigData {

    @CollapsibleObject
    public ChipPrices chip_prices = new ChipPrices(100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000, 1_000_000_000);

    public static class ChipPrices implements ConfigData {
        public int basicChip;
        public int copperChip;
        public int ironChip;
        public int amethystChip;
        public int goldChip;
        public int emeraldChip;
        public int diamondChip;
        public int netheriteChip;

        public ChipPrices() {}
        public ChipPrices(int basicChip, int copperChip, int ironChip, int amethystChip, int goldChip, int emeraldChip, int diamondChip, int netheriteChip) {
            this.basicChip = basicChip;
            this.copperChip = copperChip;
            this.ironChip = ironChip;
            this.amethystChip = amethystChip;
            this.goldChip = goldChip;
            this.emeraldChip = emeraldChip;
            this.diamondChip = diamondChip;
            this.netheriteChip = netheriteChip;
        }
    }

    // ===== HELPERS =====

    public int getChipPrice(String chipId) {
        chipId = chipId.toLowerCase(Locale.ROOT);
        return switch (chipId) {
            case "basic_chip" -> chip_prices.basicChip;
            case "copper_chip" -> chip_prices.copperChip;
            case "iron_chip" -> chip_prices.ironChip;
            case "amethyst_chip" -> chip_prices.amethystChip;
            case "gold_chip" -> chip_prices.goldChip;
            case "emerald_chip" -> chip_prices.emeraldChip;
            case "diamond_chip" -> chip_prices.diamondChip;
            case "netherite_chip" -> chip_prices.netheriteChip;
            default -> 500;
        };
    }

}