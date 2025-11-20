package net.andrespr.casinorocket.games.gachapon;

import net.andrespr.casinorocket.config.ItemGachaponConfig;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GachaponUtils {

    public record CachedEntry(Item item, int count, int weight) {}
    public record CachedPool(List<CachedEntry> entries, int totalWeight, int[] cumulativeWeights) {}

    private static final Map<String, CachedPool> CACHE = new ConcurrentHashMap<>();
    private static final Set<String> WARNED_ITEMS = ConcurrentHashMap.newKeySet();

    public static void buildCache(Map<String, List<ItemGachaponConfig.GachaEntry>> pools) {
        CACHE.clear();
        WARNED_ITEMS.clear();

        for (Map.Entry<String, List<ItemGachaponConfig.GachaEntry>> poolEntry : pools.entrySet()) {
            String poolKey = poolEntry.getKey();
            List<ItemGachaponConfig.GachaEntry> entries = poolEntry.getValue();
            List<CachedEntry> valid = new ArrayList<>();
            int totalWeight = 0;

            for (ItemGachaponConfig.GachaEntry entry : entries) {
                Identifier id = Identifier.of(entry.itemId);
                if (!Registries.ITEM.containsId(id)) {
                    if (WARNED_ITEMS.add(entry.itemId)) {
                        CasinoRocketLogger.warn("[Gachapon] Invalid item in pool '{}': '{}'", poolKey, entry.itemId);
                    }
                    continue;
                }

                Item item = Registries.ITEM.get(id);
                int count = Math.max(1, entry.count);
                int weight = Math.max(0, entry.weight);

                valid.add(new CachedEntry(item, count, weight));
                totalWeight += weight;
            }

            if (valid.isEmpty()) {
                CasinoRocketLogger.warn("[Gachapon] Pool '{}' is empty or contains no valid items", poolKey);
            }

            int[] cumulative = new int[valid.size()];
            int cumulativeSum = 0;
            for (int i = 0; i < valid.size(); i++) {
                cumulativeSum += valid.get(i).weight();
                cumulative[i] = cumulativeSum;
            }

            CACHE.put(poolKey, new CachedPool(Collections.unmodifiableList(valid), totalWeight, cumulative));
        }

        CasinoRocketLogger.info("[Gachapon] Cache built with '{}' pools.", CACHE.size());
    }

    public static ItemStack pickItemReward(Random random, String poolKey) {
        CachedPool pool = CACHE.get(poolKey);
        if (pool == null || pool.entries().isEmpty()) return ItemStack.EMPTY;

        int totalWeight = pool.totalWeight();
        if (totalWeight <= 0) return ItemStack.EMPTY;

        int roll = random.nextInt(totalWeight);

        int index = Arrays.binarySearch(pool.cumulativeWeights(), roll + 1);
        if (index < 0) { index = -index - 1; }
        if (index >= pool.entries().size()) return ItemStack.EMPTY;

        CachedEntry entry = pool.entries().get(index);
        return new ItemStack(entry.item(), entry.count());
    }

    public static Text getPoolPercentages(String poolKey) {
        CachedPool pool = CACHE.get(poolKey);
        if (pool == null || pool.entries().isEmpty()) {
            return Text.literal("Pool '" + poolKey + "' has no valid items.").formatted(Formatting.RED);
        }

        int totalWeight = pool.totalWeight();
        if (totalWeight <= 0) {
            return Text.literal("Pool '" + poolKey + "' has total weight 0.").formatted(Formatting.RED);
        }

        MutableText result = Text.literal("")
                .append(Text.literal("Rates:").formatted(Formatting.UNDERLINE)).append("\n");

        List<CachedEntry> sorted = new ArrayList<>(pool.entries());
        sorted.sort((a, b) -> Integer.compare(b.weight(), a.weight()));

        boolean first = true;

        for (CachedEntry entry : sorted) {
            if (!first) result.append(Text.literal(", "));
            first = false;

            ItemStack stack = new ItemStack(entry.item());
            String name = stack.getName().getString();

            double percentage = (entry.weight() * 100.0) / totalWeight;
            double rounded = Math.round(percentage * 100.0) / 100.0;

            Formatting color = TextUtils.percentagesColor(rounded);

            result.append(Text.literal(name + ": ")
                    .append(Text.literal(String.format("%.2f%%", rounded)).formatted(color)));
        }

        return result;
    }

    public static Set<String> getPools() {
        return CACHE.keySet();
    }

}