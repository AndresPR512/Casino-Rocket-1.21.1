package net.andrespr.casinorocket.games.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BlackjackHand {

    private final List<BlackjackCard> cards = new ArrayList<>();

    public BlackjackHand() {}

    public void clear() {
        cards.clear();
    }

    public void add(BlackjackCard card) {
        cards.add(card);
    }

    public int size() {
        return cards.size();
    }

    public List<BlackjackCard> cards() {
        return Collections.unmodifiableList(cards);
    }

    public int hardValue() {
        int sum = 0;
        for (BlackjackCard c : cards) {
            sum += c.baseValue();
        }
        return sum;
    }

    public int bestValue() {
        int hard = hardValue();

        int aces = 0;
        for (BlackjackCard c : cards) {
            if (c.isAce()) aces++;
        }

        if (aces == 0) return hard;

        int softCandidate = hard + 10;
        if (softCandidate <= 21) return softCandidate;

        return hard;
    }

    public boolean isSoft() {
        if (isBust()) return false;

        boolean hasAce = false;
        for (BlackjackCard c : cards) {
            if (c.isAce()) { hasAce = true; break; }
        }
        if (!hasAce) return false;

        return bestValue() == hardValue() + 10;
    }

    public boolean isBust() {
        return hardValue() > 21;
    }

    public boolean isNaturalBlackjack() {
        if (cards.size() != 2) return false;

        if (BlackjackRules.BLACKJACK_ONLY_ACE_PLUS_TEN) {
            boolean hasAce = false;
            boolean hasTen = false;
            for (BlackjackCard c : cards) {
                if (c.isAce()) hasAce = true;
                if (c.isTenValue()) hasTen = true;
            }
            return hasAce && hasTen;
        }

        return bestValue() == 21;
    }

    public String getValueText() {
        if (isNaturalBlackjack()) return "Blackjack";
        if (isBust()) return "Bust";

        int v = bestValue();
        if (isSoft()) return "Soft " + v;

        return Integer.toString(v);
    }

}