package net.andrespr.casinorocket.games.blackjack;

public enum BlackjackSuit {

    HEARTS(0),
    CLUBS(1),
    DIAMONDS(2),
    SPADES(3);

    public final int row; // row (j) in spritesheet

    BlackjackSuit(int row) {
        this.row = row;
    }

}