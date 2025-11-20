package net.andrespr.casinorocket.games.slot;

public enum SlotSymbol {

    CHERRY(0, 50),
    BULBASAUR(6, 20),
    SQUIRTLE(8, 20),
    CHARMANDER(10, 15),
    PIKACHU(20, 8),
    MEW(50, 5),
    ROCKET(100, 2),
    SEVEN(500, 1);

    private final int tripleMultiplier;
    private final int weight;

    SlotSymbol(int tripleMultiplier, int weight) {
        this.tripleMultiplier = tripleMultiplier;
        this.weight = weight;
    }

    public int getTripleMultiplier() {
        return tripleMultiplier;
    }

    public int getWeight() {
        return weight;
    }

}