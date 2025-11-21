package net.andrespr.casinorocket.games.slot;

public class SlotSpinEngine {

    public static SlotSpinResult spin(int baseBet, int linesMode) {
        SlotSymbol[][] matrix = generateMatrix();
        return SlotLines.evaluateSpin(matrix, baseBet, linesMode);
    }

    private static SlotSymbol[][] generateMatrix() {
        SlotSymbol[][] matrix = new SlotSymbol[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                matrix[row][col] = SlotSymbolPicker.random();
            }
        }

        return matrix;
    }

}