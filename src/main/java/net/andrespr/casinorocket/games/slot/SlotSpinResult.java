package net.andrespr.casinorocket.games.slot;

import java.util.List;

public record SlotSpinResult(
        SlotSymbol[][] matrix,
        long totalWin,
        List<SlotLineResult> lines
) { }