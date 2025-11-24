package net.andrespr.casinorocket.games.slot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SlotReels {

    private static final int REEL_SIZE = 256;
    private static final Random RANDOM = new Random();

    public static final SlotSymbol[] REEL1;
    public static final SlotSymbol[] REEL2;
    public static final SlotSymbol[] REEL3;

    static {
        REEL1 = buildReel();
        REEL2 = buildReel();
        REEL3 = buildReel();
    }

    private static SlotSymbol[] buildReel() {

        List<SlotSymbol> list = new ArrayList<>(REEL_SIZE);

        add(list, SlotSymbol.SEVEN,      10); // ~1/16384
        add(list, SlotSymbol.ROCKET,     16); // ~1/4096
        add(list, SlotSymbol.MEW,        22); // ~1/1536
        add(list, SlotSymbol.PIKACHU,    28); // ~1/768
        add(list, SlotSymbol.CHARMANDER, 35); // ~1/384
        add(list, SlotSymbol.SQUIRTLE,   40); // ~1/256
        add(list, SlotSymbol.BULBASAUR,  51); // ~1/128

        int current = list.size(); // 202
        int remaining = REEL_SIZE - current; // 54

        add(list, SlotSymbol.CHERRY, remaining); // 54 cherries

        for(int i = 0; i < 5; i++) {
            Collections.shuffle(list, RANDOM);
        }

        return list.toArray(new SlotSymbol[0]);
    }

    private static void add(List<SlotSymbol> list, SlotSymbol s, int count) {
        for (int i = 0; i < count; i++) list.add(s);
    }

    public static SlotSymbol get(SlotSymbol[] reel, int index) {
        return reel[index & (REEL_SIZE - 1)];
    }

    public static final SlotSymbol[][] STRIPS = { REEL1, REEL2, REEL3 };

}