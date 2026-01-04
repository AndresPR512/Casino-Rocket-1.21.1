package net.andrespr.casinorocket.games.slot;

import java.util.ArrayList;
import java.util.List;

public class SlotLines {

    // === LINE DEFINITIONS ===

    // ONLY THE PRINCIPAL LINE
    public static final int[][] MODE1 = {
            {1,0}, {1,1}, {1,2}
    };

    // ALL HORIZONTAL
    public static final int[][][] MODE2 = {
            { {0,0}, {0,1}, {0,2} },
            { {1,0}, {1,1}, {1,2} },
            { {2,0}, {2,1}, {2,2} }
    };

    // ALL HORIZONTAL + DIAGONAL
    public static final int[][][] MODE3 = {
            // HORIZONTAL
            { {0,0}, {0,1}, {0,2} },
            { {1,0}, {1,1}, {1,2} },
            { {2,0}, {2,1}, {2,2} },
            // DIAGONAL
            { {0,0}, {1,1}, {2,2} },
            { {2,0}, {1,1}, {0,2} }
    };

    // Return the lines depending on mode
    private static int[][][] getLines(int mode) {
        return switch (mode) {
            case 2 -> MODE2;
            case 3 -> MODE3;
            default -> new int[][][]{ MODE1 };
        };
    }

    // === EVALUATE A SINGLE LINE ===
    private static SlotLineResult evaluateLine(SlotSymbol[][] matrix, int[][] coords, int baseBet, int lineIndex) {

        SlotSymbol a = matrix[ coords[0][0] ][ coords[0][1] ];
        SlotSymbol b = matrix[ coords[1][0] ][ coords[1][1] ];
        SlotSymbol c = matrix[ coords[2][0] ][ coords[2][1] ];

        SlotSymbol target = a;
        boolean allSame = (a == b && b == c);

        int multiplier;
        int cherryCount;

        if (target == SlotSymbol.CHERRY) {
            if (a == SlotSymbol.CHERRY && b != SlotSymbol.CHERRY) {
                multiplier = 2;
                cherryCount = 1;
            } else if (a == SlotSymbol.CHERRY && b == SlotSymbol.CHERRY && c != SlotSymbol.CHERRY) {
                multiplier = 3;
                cherryCount = 2;
            } else if (a == SlotSymbol.CHERRY && b == SlotSymbol.CHERRY && c == SlotSymbol.CHERRY) {
                multiplier = 5;
                cherryCount = 3;
            } else {
                multiplier = 0;
                cherryCount = 0;
            }
            return new SlotLineResult(multiplier > 0, SlotSymbol.CHERRY, cherryCount,
                    multiplier, (long) baseBet * multiplier, lineIndex);
        }

        if (target == SlotSymbol.HAUNTER) {
            return new SlotLineResult(false, null, 0, 0, 0, lineIndex);
        }

        if (allSame) {
            multiplier = target.getTripleMultiplier();
            return new SlotLineResult(true, target, 3, multiplier, (long) baseBet * multiplier, lineIndex);
        }

        return new SlotLineResult(false, null, 0, 0, 0, lineIndex);
    }

    // === EVALUATE THE FULL SPIN ===
    public static SlotSpinResult evaluateSpin(SlotSymbol[][] matrix, int baseBet, int mode) {
        int[][][] lines = getLines(mode);

        List<SlotLineResult> results = new ArrayList<>();
        long total = 0;

        for (int i = 0; i < lines.length; i++) {
            int[][] line = lines[i];
            SlotLineResult result = evaluateLine(matrix, line, baseBet, i);
            results.add(result);
            total += result.lineWin();
        }

        return new SlotSpinResult(matrix, total, results);
    }

}