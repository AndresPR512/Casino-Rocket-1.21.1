package net.andrespr.casinorocket.games.blackjack;

public record BlackjackCard(BlackjackRank rank, BlackjackSuit suit) {

    // === LOGIC ===

    public boolean isAce() {
        return rank == BlackjackRank.ACE;
    }

    public boolean isTenValue() {
        return rank.isTenValue();
    }

    public int baseValue() {
        return rank.value;
    }

    // === RENDER / TEXTURE ===

    public int i() {
        return rank.col;
    }

    public int j() {
        return suit.row;
    }

    public int u0() {
        return i() * 24;
    }

    public int v0() {
        return j() * 32;
    }

    public int u1() {
        return u0() + 24;
    }

    public int v1() {
        return v0() + 32;
    }

}