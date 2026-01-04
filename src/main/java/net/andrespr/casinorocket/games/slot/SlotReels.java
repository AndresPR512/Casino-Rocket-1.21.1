package net.andrespr.casinorocket.games.slot;

import net.andrespr.casinorocket.config.SlotMachineConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SlotReels {

    private static int REEL_SIZE = 256;

    private static SlotSymbol[] REEL1;
    private static SlotSymbol[] REEL2;
    private static SlotSymbol[] REEL3;

    public static SlotSymbol[][] STRIPS;

    public static int reelSize() {
        return REEL_SIZE;
    }

    public static void reloadFromConfig(SlotMachineConfig cfg) {
        REEL_SIZE = Math.max(16, Math.min(cfg.reels.reelSize, 4096));

        REEL1 = buildReel(cfg.reels.reel1, REEL_SIZE, 0xC451F0L);
        REEL2 = buildReel(cfg.reels.reel2, REEL_SIZE, 0xC451F0L ^ 0x2222L);
        REEL3 = buildReel(cfg.reels.reel3, REEL_SIZE, 0xC451F0L ^ 0x3333L);

        STRIPS = new SlotSymbol[][] { REEL1, REEL2, REEL3 };
    }

    public static SlotSymbol get(SlotSymbol[] reel, int index) {
        int i = Math.floorMod(index, REEL_SIZE);
        return reel[i];
    }

    private static SlotSymbol[] buildReel(SlotMachineConfig.ReelStrip strip, int reelSize, long seed) {
        List<SlotSymbol> list = new ArrayList<>(reelSize);

        for (var e : strip.counts.entrySet()) {
            int count = Math.max(0, e.getValue());
            for (int i = 0; i < count; i++) list.add(e.getKey());
        }

        if (list.size() > reelSize) {
            list = list.subList(0, reelSize);
        } else if (list.size() < reelSize) {
            int remaining = reelSize - list.size();
            for (int i = 0; i < remaining; i++) list.add(strip.fillSymbol);
        }

        Random r = new Random(seed);
        for (int i = 0; i < 5; i++) Collections.shuffle(list, r);

        return list.toArray(new SlotSymbol[0]);
    }

    public static void applySyncedStrips(int reelSize, SlotSymbol[] r1, SlotSymbol[] r2, SlotSymbol[] r3) {
        REEL_SIZE = Math.max(16, Math.min(reelSize, 4096));
        REEL1 = r1;
        REEL2 = r2;
        REEL3 = r3;
        STRIPS = new SlotSymbol[][]{ REEL1, REEL2, REEL3 };
    }

}