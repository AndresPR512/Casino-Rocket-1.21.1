package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Config(name = "gacha_machines")
public class GachaMachinesConfig implements ConfigData {

    @CollapsibleObject
    public RarityBaseWeights rarity_base_weights = new RarityBaseWeights();

    @CollapsibleObject
    public CoinPrices coin_prices = new CoinPrices();

    @CollapsibleObject
    public CoinMultipliers coin_multipliers = new CoinMultipliers();

    @CollapsibleObject
    public PityConfig pity = new PityConfig();

    @CollapsibleObject
    public PremierBonus premier_bonus = new PremierBonus();

    public static class RarityBaseWeights implements ConfigData {
        public double common = 60;
        public double uncommon = 25;
        public double rare = 10;
        public double ultrarare = 4;
        public double legendary = 1;
    }

    public static class CoinPrices implements ConfigData {
        public int copper = 1_000;
        public int iron = 10_000;
        public int gold = 100_000;
        public int diamond = 500_000;
    }

    public static class CoinMultipliers implements ConfigData {
        @CollapsibleObject public Coin copper = new Coin(1.00, 1.00, 1.00, 1.00, 0.10);
        @CollapsibleObject public Coin iron = new Coin(0.85, 1.10, 1.40, 1.60, 0.50);
        @CollapsibleObject public Coin gold = new Coin(0.55, 1.20, 2.10, 2.50, 0.95);
        @CollapsibleObject public Coin diamond = new Coin(0.32, 1.10, 3.00, 3.20, 2.30);
    }

    public static class Coin implements ConfigData {
        public double common;
        public double uncommon;
        public double rare;
        public double ultrarare;
        public double legendary;

        public Coin() {}
        public Coin(double common, double uncommon, double rare, double ultrarare, double legendary) {
            this.common = common;
            this.uncommon = uncommon;
            this.rare = rare;
            this.ultrarare = ultrarare;
            this.legendary = legendary;
        }

        public Map<String, Double> toMap() {
            Map<String, Double> map = new LinkedHashMap<>();
            map.put("common", common);
            map.put("uncommon", uncommon);
            map.put("rare", rare);
            map.put("ultrarare", ultrarare);
            map.put("legendary", legendary);
            return map;
        }
    }

    public static class PityConfig implements ConfigData {

        public boolean enable = true;
        @CollapsibleObject
        public CoinPity iron = new CoinPity(0,0.0);
        @CollapsibleObject
        public CoinPity gold = new CoinPity(80, 0.25);
        @CollapsibleObject
        public CoinPity diamond = new CoinPity(25, 0.25);

        public static class CoinPity implements ConfigData {
            public int usesToMax;
            public double maxLegendaryChance;

            public CoinPity() {}
            public CoinPity(int usesToMax, double maxLegendaryChance) {
                this.usesToMax = usesToMax;
                this.maxLegendaryChance = maxLegendaryChance;
            }
        }

    }

    public static class PremierBonus implements ConfigData {
        public boolean enable = true;
        public int coinsToBonus = 10;
    }

    // ===== HELPERS =====

    public Map<String, Double> baseWeightsMap() {
        Map<String, Double> m = new LinkedHashMap<>();
        m.put("common", rarity_base_weights.common);
        m.put("uncommon", rarity_base_weights.uncommon);
        m.put("rare", rarity_base_weights.rare);
        m.put("ultrarare", rarity_base_weights.ultrarare);
        m.put("legendary", rarity_base_weights.legendary);
        return m;
    }

    public Coin coin(String coinKey) {
        coinKey = coinKey.toLowerCase(Locale.ROOT);
        return switch (coinKey) {
            case "iron" -> coin_multipliers.iron;
            case "gold" -> coin_multipliers.gold;
            case "diamond" -> coin_multipliers.diamond;
            default -> coin_multipliers.copper;
        };
    }

    public Map<String, Double> normalizedProbabilities(String coinKey) {
        Map<String, Double> base = baseWeightsMap();
        Map<String, Double> mult = coin(coinKey).toMap();

        double sum = 0;
        Map<String, Double> weighted = new LinkedHashMap<>();
        for (var e : base.entrySet()) {
            double v = e.getValue() * mult.get(e.getKey());
            weighted.put(e.getKey(), v);
            sum += v;
        }
        if (sum <= 0) return Collections.emptyMap();
        for (var k : weighted.keySet()) weighted.put(k, weighted.get(k) / sum);
        return weighted;
    }

}