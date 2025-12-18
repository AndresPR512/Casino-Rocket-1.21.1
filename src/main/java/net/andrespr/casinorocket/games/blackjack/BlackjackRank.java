package net.andrespr.casinorocket.games.blackjack;

public enum BlackjackRank {

    ACE   (0, 1),

    TWO   (1, 2),
    THREE (2, 3),
    FOUR  (3, 4),
    FIVE  (4, 5),
    SIX   (5, 6),
    SEVEN (6, 7),
    EIGHT (7, 8),
    NINE  (8, 9),
    TEN   (9, 10),

    JACK  (10, 10),
    QUEEN (11, 10),
    KING  (12, 10);

    public final int col; // column (i) in spritesheet
    public final int value; // value of each card

    BlackjackRank(int col, int value) {
        this.col = col;
        this.value = value;
    }

    public boolean isTenValue() {
        return value == 10;
    }

}