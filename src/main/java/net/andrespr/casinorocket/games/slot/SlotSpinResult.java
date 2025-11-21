package net.andrespr.casinorocket.games.slot;

import java.util.List;

public record SlotSpinResult(
        SlotSymbol[][] matrix,
        int totalWin,
        List<SlotLineResult> lines
) { }