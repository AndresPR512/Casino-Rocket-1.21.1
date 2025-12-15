package net.andrespr.casinorocket.games.gachapon;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.PokemonGachaponConfig;
import net.andrespr.casinorocket.util.CobblemonUtils;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.Random;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PokemonGachaponUtils {

    public record CachedEntry(String pokemonId, int level, int ivs, String shiny, int weight) {}
    public record CachedPool(List<CachedEntry> entries, int totalWeight, int[] cumulativeWeights) {}

    private static final Map<String, CachedPool> CACHE = new ConcurrentHashMap<>();
    private static final Set<String> WARNED_POKEMON = ConcurrentHashMap.newKeySet();

    public static void buildCache(Map<String, List<PokemonGachaponConfig.GachaEntry>> pools) {
        CACHE.clear();
        WARNED_POKEMON.clear();

        for (Map.Entry<String, List<PokemonGachaponConfig.GachaEntry>> poolEntry : pools.entrySet()) {
            String poolKey = poolEntry.getKey();
            List<PokemonGachaponConfig.GachaEntry> entries = poolEntry.getValue();
            List<CachedEntry> valid = new ArrayList<>();
            int totalWeight = 0;

            for (PokemonGachaponConfig.GachaEntry entry : entries) {
                if (CobblemonUtils.tryParse(entry.pokemonId) == null) {
                    if (WARNED_POKEMON.add(entry.pokemonId)) {
                        CasinoRocket.LOGGER.warn("[Pokémon Gachapon] Invalid Pokémon in pool '{}': '{}'", poolKey, entry.pokemonId);
                    }
                    continue;
                }

                entry.validate();

                valid.add(new CachedEntry(entry.pokemonId, entry.level, entry.ivs, entry.shiny, Math.max(0, entry.weight)));
                totalWeight += Math.max(0, entry.weight);
            }

            if (valid.isEmpty()) {
                CasinoRocket.LOGGER.warn("[Pokémon Gachapon] Pool '{}' is empty or contains no valid Pokémon", poolKey);
            }

            int[] cumulative = new int[valid.size()];
            int cumulativeSum = 0;
            for (int i = 0; i < valid.size(); i++) {
                cumulativeSum += valid.get(i).weight();
                cumulative[i] = cumulativeSum;
            }

            CACHE.put(poolKey, new CachedPool(Collections.unmodifiableList(valid), totalWeight, cumulative));
        }

        CasinoRocket.LOGGER.info("[Pokémon Gachapon] Cache built with '{}' pools.", CACHE.size());
    }

    public static CachedEntry pickPokemonReward(Random random, String poolKey) {
        CachedPool pool = CACHE.get(poolKey);
        if (pool == null || pool.entries().isEmpty()) return null;

        int totalWeight = pool.totalWeight();
        if (totalWeight <= 0) return null;

        int roll = random.nextInt(totalWeight);

        int index = Arrays.binarySearch(pool.cumulativeWeights(), roll + 1);
        if (index < 0) index = -index - 1;
        if (index >= pool.entries().size()) return null;

        return pool.entries().get(index);
    }

    public static Text getPoolPercentages(String poolKey) {
        CachedPool pool = CACHE.get(poolKey);
        if (pool == null || pool.entries().isEmpty())
            return Text.literal("Pool '" + poolKey + "' has no valid Pokémon.").formatted(Formatting.RED);

        int totalWeight = pool.totalWeight();
        if (totalWeight <= 0)
            return Text.literal("Pool '" + poolKey + "' has total weight 0.").formatted(Formatting.RED);

        MutableText result = Text.literal("")
                .append(Text.literal("Rates:").formatted(Formatting.UNDERLINE)).append("\n");

        List<CachedEntry> sorted = new ArrayList<>(pool.entries());
        sorted.sort((a, b) -> Integer.compare(b.weight(), a.weight()));

        boolean first = true;

        for (CachedEntry entry : sorted) {
            if (!first) result.append(Text.literal(", "));
            first = false;

            String cleanId = CobblemonUtils.getRawId(entry.pokemonId());
            String name = TextUtils.capitalize(cleanId.replace("_", " "));

            double percentage = (entry.weight() * 100.0) / totalWeight;
            double rounded = Math.round(percentage * 100.0) / 100.0;

            Formatting color = TextUtils.percentagesColor(rounded);

            result.append(Text.literal(name + ": ").append(Text.literal(String.format("%.2f%%", rounded)).formatted(color)));
        }

        return result;
    }

    public static Set<String> getPools() {
        return CACHE.keySet();
    }

}