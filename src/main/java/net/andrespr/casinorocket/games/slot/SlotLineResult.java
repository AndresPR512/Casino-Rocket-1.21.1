package net.andrespr.casinorocket.games.slot;

public record SlotLineResult(
        boolean win,
        SlotSymbol symbol,
        int count,
        int multiplier,
        long lineWin,
        int lineIndex
) {}