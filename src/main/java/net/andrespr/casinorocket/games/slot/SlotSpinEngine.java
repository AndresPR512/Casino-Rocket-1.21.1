package net.andrespr.casinorocket.games.slot;

import java.util.Random;

public class SlotSpinEngine {

    private static final Random RNG = new Random();

    public record SpinStop(int index1, int index2, int index3, SlotSymbol[][] finalMatrix) {}

    public static SpinStop spin() {

        int size = SlotReels.reelSize();

        int stop1 = RNG.nextInt(size);
        int stop2 = RNG.nextInt(size);
        int stop3 = RNG.nextInt(size);

        SlotSymbol[][] matrix = new SlotSymbol[3][3];

        SlotSymbol[] r1 = SlotReels.STRIPS[0];
        SlotSymbol[] r2 = SlotReels.STRIPS[1];
        SlotSymbol[] r3 = SlotReels.STRIPS[2];

        matrix[0][0] = SlotReels.get(r1, stop1 - 1);
        matrix[1][0] = SlotReels.get(r1, stop1);
        matrix[2][0] = SlotReels.get(r1, stop1 + 1);

        matrix[0][1] = SlotReels.get(r2, stop2 - 1);
        matrix[1][1] = SlotReels.get(r2, stop2);
        matrix[2][1] = SlotReels.get(r2, stop2 + 1);

        matrix[0][2] = SlotReels.get(r3, stop3 - 1);
        matrix[1][2] = SlotReels.get(r3, stop3);
        matrix[2][2] = SlotReels.get(r3, stop3 + 1);

        return new SpinStop(stop1, stop2, stop3, matrix);
    }

    public static SlotSpinResult spinAndEvaluate(int baseBet, int linesMode) {
        SpinStop stop = spin();
        SlotSymbol[][] matrix = stop.finalMatrix();
        return SlotLines.evaluateSpin(matrix, baseBet, linesMode);
    }

}