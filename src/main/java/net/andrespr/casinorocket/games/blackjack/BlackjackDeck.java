package net.andrespr.casinorocket.games.blackjack;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public final class BlackjackDeck {

    private final Deque<BlackjackCard> cards = new ArrayDeque<>();

    public BlackjackDeck() {
        resetAndShuffle();
    }

    public void resetAndShuffle() {
        cards.clear();

        List<BlackjackCard> temp = new ArrayList<>(52);

        for (BlackjackSuit suit : BlackjackSuit.values()) {
            for (BlackjackRank rank : BlackjackRank.values()) {
                temp.add(new BlackjackCard(rank, suit));
            }
        }

        Collections.shuffle(temp);

        for (BlackjackCard card : temp) {
            cards.addLast(card);
        }
    }

    public BlackjackCard draw() {
        if (cards.isEmpty()) {
            resetAndShuffle();
        }
        return cards.removeFirst();
    }

    public int remaining() {
        return cards.size();
    }

}