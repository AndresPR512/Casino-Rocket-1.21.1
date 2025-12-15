package net.andrespr.casinorocket.games.slot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SlotReels {

    private static final int REEL_SIZE = 256;

    private static final long SEED_REEL1 = 0xC451F0L;
    private static final long SEED_REEL2 = 0xC451F0L ^ 0x2222L;
    private static final long SEED_REEL3 = 0xC451F0L ^ 0x3333L;

    public static final SlotSymbol[] REEL1 = buildReel(SEED_REEL1);
    public static final SlotSymbol[] REEL2 = buildReel(SEED_REEL2);
    public static final SlotSymbol[] REEL3 = buildReel(SEED_REEL3);

    public static final SlotSymbol[][] STRIPS = { REEL1, REEL2, REEL3 };

    private static SlotSymbol[] buildReel(long seed) {
        List<SlotSymbol> list = new ArrayList<>(REEL_SIZE);

        add(list, SlotSymbol.SEVEN,      10);
        add(list, SlotSymbol.ROCKET,     16);
        add(list, SlotSymbol.MEW,        22);
        add(list, SlotSymbol.PIKACHU,    28);
        add(list, SlotSymbol.CHARMANDER, 35);
        add(list, SlotSymbol.SQUIRTLE,   40);
        add(list, SlotSymbol.BULBASAUR,  51);

        int remaining = REEL_SIZE - list.size();
        add(list, SlotSymbol.CHERRY, remaining);

        Random r = new Random(seed);
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(list, r);
        }

        return list.toArray(new SlotSymbol[0]);
    }

    private static void add(List<SlotSymbol> list, SlotSymbol s, int count) {
        for (int i = 0; i < count; i++) list.add(s);
    }

    public static SlotSymbol get(SlotSymbol[] reel, int index) {
        return reel[index & (REEL_SIZE - 1)];
    }
}