package net.andrespr.casinorocket.games.gachapon;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.PlushiesGachaponConfig;
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

public final class PlushiesGachaponUtils {

    public record CachedEntry(Item item, int weight) {}

    private static final List<CachedEntry> ENTRIES = new ArrayList<>();
    private static int TOTAL_WEIGHT = 0;
    private static int[] CUMULATIVE = new int[0];

    private static final Set<String> WARNED_ITEMS = ConcurrentHashMap.newKeySet();

    private PlushiesGachaponUtils() {}

    public static void buildCache(List<PlushiesGachaponConfig.GachaEntry> plushies) {

        ENTRIES.clear();
        WARNED_ITEMS.clear();
        TOTAL_WEIGHT = 0;

        if (plushies == null || plushies.isEmpty()) {
            CasinoRocket.LOGGER.warn("[PlushiesGachapon] Plushies list is empty.");
            CUMULATIVE = new int[0];
            return;
        }

        for (PlushiesGachaponConfig.GachaEntry entry : plushies) {
            if (entry == null || entry.itemId == null || entry.itemId.isBlank()) continue;

            Identifier id = Identifier.of(entry.itemId);
            if (!Registries.ITEM.containsId(id)) {
                if (WARNED_ITEMS.add(entry.itemId)) {
                    CasinoRocket.LOGGER.warn("[PlushiesGachapon] Invalid item '{}'", entry.itemId);
                }
                continue;
            }

            int weight = Math.max(0, entry.weight);
            if (weight <= 0) continue;

            Item item = Registries.ITEM.get(id);
            ENTRIES.add(new CachedEntry(item, weight));
            TOTAL_WEIGHT += weight;
        }

        if (ENTRIES.isEmpty()) {
            CasinoRocket.LOGGER.warn("[PlushiesGachapon] No valid items found (all invalid or weight=0).");
            CUMULATIVE = new int[0];
            return;
        }

        CUMULATIVE = new int[ENTRIES.size()];
        int sum = 0;
        for (int i = 0; i < ENTRIES.size(); i++) {
            sum += ENTRIES.get(i).weight();
            CUMULATIVE[i] = sum;
        }

        CasinoRocket.LOGGER.info("[PlushiesGachapon] Cache built with {} entries. TotalWeight={}", ENTRIES.size(), TOTAL_WEIGHT);

    }

    public static ItemStack pickPlushie(Random random) {

        if (ENTRIES.isEmpty() || TOTAL_WEIGHT <= 0) return ItemStack.EMPTY;

        int roll = random.nextInt(TOTAL_WEIGHT);
        int idx = Arrays.binarySearch(CUMULATIVE, roll + 1);
        if (idx < 0) idx = -idx - 1;
        if (idx < 0 || idx >= ENTRIES.size()) return ItemStack.EMPTY;

        CachedEntry entry = ENTRIES.get(idx);
        return new ItemStack(entry.item());

    }

    public static Text getRates() {

        if (ENTRIES.isEmpty()) {
            return Text.literal("Plushies list has no valid items.").formatted(Formatting.RED);
        }
        if (TOTAL_WEIGHT <= 0) {
            return Text.literal("Plushies total weight is 0.").formatted(Formatting.RED);
        }

        MutableText result = Text.literal("")
                .append(Text.literal("Rates:").formatted(Formatting.UNDERLINE))
                .append("\n");

        List<CachedEntry> sorted = new ArrayList<>(ENTRIES);
        sorted.sort((a, b) -> Integer.compare(b.weight(), a.weight()));

        boolean first = true;
        for (CachedEntry e : sorted) {
            if (!first) result.append(Text.literal(", "));
            first = false;

            ItemStack stack = new ItemStack(e.item());
            String name = stack.getName().getString();

            double pct = (e.weight() * 100.0) / TOTAL_WEIGHT;
            double rounded = Math.round(pct * 100.0) / 100.0;

            Formatting color = TextUtils.percentagesColor(rounded);

            result.append(Text.literal(name + ": ").append(Text.literal(String.format("%.2f%%", rounded)).formatted(color)));
        }

        return result;
    }

    public static int getEntryCount() {
        return ENTRIES.size();
    }

    public static boolean hasRewards() {
        return getEntryCount() > 0;
    }

}