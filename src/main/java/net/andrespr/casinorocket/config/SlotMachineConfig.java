package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.andrespr.casinorocket.games.slot.SlotSymbol;

@Config(name = "slot_machine")
public class SlotMachineConfig implements ConfigData {

    public boolean debug = false;

    public List<Integer> betValues = new ArrayList<>(List.of(
            10, 25, 50, 100, 250, 500, 1_000, 5_000, 10_000, 25_000, 50_000, 100_000, 250_000, 500_000, 1_000_000, 5_000_000
    ));

    @CollapsibleObject
    public BetMultipliers betMultipliers = new BetMultipliers(1, 3, 5);

    @CollapsibleObject
    public Reels reels = Reels.defaultReels();

    public static class BetMultipliers implements ConfigData {
        public int mode1;
        public int mode2;
        public int mode3;

        public BetMultipliers() {}
        public BetMultipliers(int mode1, int mode2, int mode3) {
            this.mode1 = mode1;
            this.mode2 = mode2;
            this.mode3 = mode3;
        }
    }

    public static class Reels implements ConfigData {
        public int reelSize = 256;

        @CollapsibleObject public ReelStrip reel1 = ReelStrip.defaultReel1();
        @CollapsibleObject public ReelStrip reel2 = ReelStrip.defaultReel2();
        @CollapsibleObject public ReelStrip reel3 = ReelStrip.defaultReel3();

        public Reels() {}

        public static Reels defaultReels() {
            return new Reels();
        }
    }

    public static class ReelStrip implements ConfigData {
        public Map<SlotSymbol, Integer> counts = new EnumMap<>(SlotSymbol.class);
        public SlotSymbol fillSymbol = SlotSymbol.HAUNTER;

        public ReelStrip() {}

        public static ReelStrip defaultCommonBase() {
            ReelStrip r = new ReelStrip();
            r.counts.put(SlotSymbol.SEVEN, 6);
            r.counts.put(SlotSymbol.ROCKET, 10);
            r.counts.put(SlotSymbol.MEW, 16);
            r.counts.put(SlotSymbol.PIKACHU, 22);
            r.counts.put(SlotSymbol.CHARMANDER, 32);
            r.counts.put(SlotSymbol.SQUIRTLE, 38);
            r.counts.put(SlotSymbol.BULBASAUR, 46);
            return r;
        }

        public static ReelStrip defaultReel1() {
            ReelStrip r = defaultCommonBase();
            r.counts.put(SlotSymbol.CHERRY, 64);
            return r;
        }

        public static ReelStrip defaultReel2() {
            ReelStrip r = defaultCommonBase();
            r.counts.put(SlotSymbol.CHERRY, 76);
            return r;
        }

        public static ReelStrip defaultReel3() {
            ReelStrip r = defaultCommonBase();
            r.counts.put(SlotSymbol.CHERRY, 76);
            return r;
        }
    }

    // ===== HELPERS =====
    public int defaultBetBase() {
        return betValues.isEmpty() ? 10 : betValues.getFirst();
    }

    public int getBetMultiplierForMode(int mode) {
        return switch (mode) {
            case 2 -> betMultipliers.mode2;
            case 3 -> betMultipliers.mode3;
            default -> betMultipliers.mode1;
        };
    }

    @Override
    public void validatePostLoad() {
        if (betValues == null) betValues = new ArrayList<>();
        betValues = betValues.stream()
                .filter(v -> v != null && v > 0)
                .distinct()
                .sorted()
                .toList()
                .stream()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        if (betValues.isEmpty()) {
            betValues.add(10);
        }
        if (betMultipliers == null) betMultipliers = new BetMultipliers(1, 3, 5);
        betMultipliers.mode1 = Math.max(1, betMultipliers.mode1);
        betMultipliers.mode2 = Math.max(1, betMultipliers.mode2);
        betMultipliers.mode3 = Math.max(1, betMultipliers.mode3);

        if (reels == null) reels = Reels.defaultReels();
        reels.reelSize = clamp(reels.reelSize, 16, 4096);

        sanitizeStrip(reels.reel1);
        sanitizeStrip(reels.reel2);
        sanitizeStrip(reels.reel3);
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    private static void sanitizeStrip(ReelStrip strip) {
        if (strip == null) return;

        if (strip.counts == null) strip.counts = new EnumMap<>(SlotSymbol.class);
        if (strip.fillSymbol == null) strip.fillSymbol = SlotSymbol.HAUNTER;

        strip.counts.replaceAll((k, val) -> val == null ? 0 : Math.max(0, val));
    }

}