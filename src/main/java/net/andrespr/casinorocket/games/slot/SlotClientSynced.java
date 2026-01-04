package net.andrespr.casinorocket.games.slot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SlotClientSynced {

    private SlotClientSynced() {}

    public static volatile boolean DEBUG = false;
    public static volatile List<Integer> BET_VALUES = null;
    public static volatile int MODE1 = 1;
    public static volatile int MODE2 = 3;
    public static volatile int MODE3 = 5;

    public static boolean has() {
        return BET_VALUES != null && !BET_VALUES.isEmpty();
    }

    public static void apply(boolean debug, int[] betValues, int mode1, int mode2, int mode3) {
        DEBUG = debug;
        List<Integer> list = new ArrayList<>(betValues.length);
        for (int v : betValues) if (v > 0) list.add(v);
        Collections.sort(list);

        BET_VALUES = Collections.unmodifiableList(list);
        MODE1 = Math.max(1, mode1);
        MODE2 = Math.max(1, mode2);
        MODE3 = Math.max(1, mode3);
    }

}