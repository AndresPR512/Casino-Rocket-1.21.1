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
    public CoinMultipliers coin_multipliers = new CoinMultipliers();

    @CollapsibleObject
    public PityConfig pity = new PityConfig();

    @CollapsibleObject
    public PremierBonus premier_bonus = new PremierBonus();

    @CollapsibleObject
    public GachaStore gacha_store = new GachaStore();

    public static class RarityBaseWeights implements ConfigData {
        public double common = 60;
        public double uncommon = 25;
        public double rare = 10;
        public double ultrarare = 4;
        public double legendary = 1;
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
        public boolean pityUpdateMessages = true;
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

    public static class GachaStore implements ConfigData {

        @CollapsibleObject
        public CoinPrices coin_prices = new CoinPrices(1_000, 10_000, 100_000, 500_000, 50_000, 50_000);
        @CollapsibleObject
        public GachaponStoreConfig gachapon_store = new GachaponStoreConfig();

        public static class CoinPrices implements ConfigData {
            public int copper;
            public int iron;
            public int gold;
            public int diamond;
            public int event;
            public int primogem;

            public CoinPrices() {}
            public CoinPrices(int copper, int iron, int gold, int diamond, int event, int primogem) {
                this.copper = copper;
                this.iron = iron;
                this.gold = gold;
                this.diamond = diamond;
                this.event = event;
                this.primogem = primogem;
            }
        }

        public static class GachaponStoreConfig implements ConfigData {
            @CollapsibleObject public boolean enableItemGachaponStore = true;
            @CollapsibleObject public Gachapon item_poke_gachapon = new Gachapon(true, 5_000);
            @CollapsibleObject public Gachapon item_great_gachapon = new Gachapon(true, 50_000);
            @CollapsibleObject public Gachapon item_ultra_gachapon = new Gachapon(true, 500_000);
            @CollapsibleObject public Gachapon item_master_gachapon = new Gachapon(false, 2_500_000);
            @CollapsibleObject public Gachapon item_cherish_gachapon = new Gachapon(false, 10_000_000);
            @CollapsibleObject public Gachapon item_premier_gachapon = new Gachapon(false, 10_000);

            @CollapsibleObject public boolean enablePokemonGachaponStore = true;
            @CollapsibleObject public Gachapon pokemon_poke_gachapon = new Gachapon(true, 5_000);
            @CollapsibleObject public Gachapon pokemon_great_gachapon = new Gachapon(true, 50_000);
            @CollapsibleObject public Gachapon pokemon_ultra_gachapon = new Gachapon(true, 500_000);
            @CollapsibleObject public Gachapon pokemon_master_gachapon = new Gachapon(false, 2_500_000);
            @CollapsibleObject public Gachapon pokemon_cherish_gachapon = new Gachapon(false, 10_000_000);
            @CollapsibleObject public Gachapon pokemon_premier_gachapon = new Gachapon(false, 10_000);
        }

    }

    public static class Gachapon implements ConfigData {
        public boolean enableToBuy;
        public int price;

        public Gachapon() {}
        public Gachapon(boolean enableToBuy, int price) {
            this.enableToBuy = enableToBuy;
            this.price = price;
        }
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

    public String getCoinPrice(String coinId) {
        coinId = coinId.toLowerCase(Locale.ROOT);
        int coinValue = switch (coinId) {
            case "casinorocket:copper_coin" -> gacha_store.coin_prices.copper;
            case "casinorocket:iron_coin" -> gacha_store.coin_prices.iron;
            case "casinorocket:gold_coin" -> gacha_store.coin_prices.gold;
            case "casinorocket:diamond_coin" -> gacha_store.coin_prices.diamond;
            case "casinorocket:event_coin" -> gacha_store.coin_prices.event;
            case "casinorocket:primogem" -> gacha_store.coin_prices.primogem;
            default -> 1_000;
        };
        return String.valueOf(coinValue);
    }

    public String getGachaponPrice(String gachaponId) {
        gachaponId = gachaponId.toLowerCase(Locale.ROOT);
        int gachaponValue = switch (gachaponId) {
            case "casinorocket:poke_gachapon" -> gacha_store.gachapon_store.item_poke_gachapon.price;
            case "casinorocket:great_gachapon" -> gacha_store.gachapon_store.item_great_gachapon.price;
            case "casinorocket:ultra_gachapon" -> gacha_store.gachapon_store.item_ultra_gachapon.price;
            case "casinorocket:master_gachapon" -> gacha_store.gachapon_store.item_master_gachapon.price;
            case "casinorocket:cherish_gachapon" -> gacha_store.gachapon_store.item_cherish_gachapon.price;
            case "casinorocket:premier_gachapon" -> gacha_store.gachapon_store.item_premier_gachapon.price;
            case "casinorocket:pokemon_poke_gachapon" -> gacha_store.gachapon_store.pokemon_poke_gachapon.price;
            case "casinorocket:pokemon_great_gachapon" -> gacha_store.gachapon_store.pokemon_great_gachapon.price;
            case "casinorocket:pokemon_ultra_gachapon" -> gacha_store.gachapon_store.pokemon_ultra_gachapon.price;
            case "casinorocket:pokemon_master_gachapon" -> gacha_store.gachapon_store.pokemon_master_gachapon.price;
            case "casinorocket:pokemon_cherish_gachapon" -> gacha_store.gachapon_store.pokemon_cherish_gachapon.price;
            case "casinorocket:pokemon_premier_gachapon" -> gacha_store.gachapon_store.pokemon_premier_gachapon.price;
            default -> 10_000;
        };
        return String.valueOf(gachaponValue);
    }

    public boolean getGachaponEnable(String gachaponId) {
        gachaponId = gachaponId.toLowerCase(Locale.ROOT);
        return switch (gachaponId) {
            case "casinorocket:poke_gachapon" -> gacha_store.gachapon_store.item_poke_gachapon.enableToBuy;
            case "casinorocket:great_gachapon" -> gacha_store.gachapon_store.item_great_gachapon.enableToBuy;
            case "casinorocket:ultra_gachapon" -> gacha_store.gachapon_store.item_ultra_gachapon.enableToBuy;
            case "casinorocket:master_gachapon" -> gacha_store.gachapon_store.item_master_gachapon.enableToBuy;
            case "casinorocket:cherish_gachapon" -> gacha_store.gachapon_store.item_cherish_gachapon.enableToBuy;
            case "casinorocket:premier_gachapon" -> gacha_store.gachapon_store.item_premier_gachapon.enableToBuy;
            case "casinorocket:pokemon_poke_gachapon" -> gacha_store.gachapon_store.pokemon_poke_gachapon.enableToBuy;
            case "casinorocket:pokemon_great_gachapon" -> gacha_store.gachapon_store.pokemon_great_gachapon.enableToBuy;
            case "casinorocket:pokemon_ultra_gachapon" -> gacha_store.gachapon_store.pokemon_ultra_gachapon.enableToBuy;
            case "casinorocket:pokemon_master_gachapon" -> gacha_store.gachapon_store.pokemon_master_gachapon.enableToBuy;
            case "casinorocket:pokemon_cherish_gachapon" -> gacha_store.gachapon_store.pokemon_cherish_gachapon.enableToBuy;
            case "casinorocket:pokemon_premier_gachapon" -> gacha_store.gachapon_store.pokemon_premier_gachapon.enableToBuy;
            default -> false;
        };
    }

}