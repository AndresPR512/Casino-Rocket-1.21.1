package net.andrespr.casinorocket.games.slot;

public enum SlotSymbol {

    HAUNTER(0),
    CHERRY(5),
    BULBASAUR(6),
    SQUIRTLE(8),
    CHARMANDER(10),
    PIKACHU(20),
    MEW(50),
    ROCKET(100),
    SEVEN(500);

    private final int tripleMultiplier;

    SlotSymbol(int tripleMultiplier) {
        this.tripleMultiplier = tripleMultiplier;
    }

    public int getTripleMultiplier() {
        return tripleMultiplier;
    }

}