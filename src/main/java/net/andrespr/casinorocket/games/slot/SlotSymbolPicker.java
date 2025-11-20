package net.andrespr.casinorocket.games.slot;

import java.util.ArrayList;
import java.util.List;

public class SlotSymbolPicker {

    private static final List<SlotSymbol> TABLE;
    private static final java.util.Random RANDOM = new java.util.Random();

    static {
        List<SlotSymbol> list = new ArrayList<>();
        for (SlotSymbol symbol : SlotSymbol.values()) {
            for (int i = 0; i < symbol.getWeight(); i++) {
                list.add(symbol);
            }
        }
        TABLE = List.copyOf(list);
    }

    public static SlotSymbol random() {
        return TABLE.get(RANDOM.nextInt(TABLE.size()));
    }

}