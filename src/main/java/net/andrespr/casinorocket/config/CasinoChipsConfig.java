package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;

import java.util.Locale;

@Config(name = "casino_chips_config")
public class CasinoChipsConfig implements ConfigData {

    @CollapsibleObject
    public ChipPrices chip_prices = new ChipPrices(10, 50, 100, 500, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 50_000_000);

    public static class ChipPrices implements ConfigData {
        public int basicChip;
        public int redChip;
        public int blueChip;
        public int purpleChip;
        public int copperChip;
        public int ironChip;
        public int emeraldChip;
        public int goldChip;
        public int diamondChip;
        public int netheriteChip;

        public ChipPrices() {}
        public ChipPrices(int basicChip, int redChip, int blueChip, int purpleChip, int copperChip,
                          int ironChip, int emeraldChip, int goldChip, int diamondChip, int netheriteChip) {
            this.basicChip = basicChip;
            this.redChip = redChip;
            this.blueChip = blueChip;
            this.purpleChip = purpleChip;
            this.copperChip = copperChip;
            this.ironChip = ironChip;
            this.emeraldChip = emeraldChip;
            this.goldChip = goldChip;
            this.diamondChip = diamondChip;
            this.netheriteChip = netheriteChip;
        }
    }

    // ===== HELPERS =====

    public int getChipPrice(String chipId) {
        chipId = chipId.toLowerCase(Locale.ROOT);
        return switch (chipId) {
            case "basic_chip" -> chip_prices.basicChip;
            case "red_chip" -> chip_prices.redChip;
            case "blue_chip" -> chip_prices.blueChip;
            case "purple_chip" -> chip_prices.purpleChip;
            case "copper_chip" -> chip_prices.copperChip;
            case "iron_chip" -> chip_prices.ironChip;
            case "emerald_chip" -> chip_prices.emeraldChip;
            case "gold_chip" -> chip_prices.goldChip;
            case "diamond_chip" -> chip_prices.diamondChip;
            case "netherite_chip" -> chip_prices.netheriteChip;
            default -> 100;
        };
    }

}