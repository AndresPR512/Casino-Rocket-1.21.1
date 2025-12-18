package net.andrespr.casinorocket.games.blackjack;

public final class BlackjackEngine {

    private BlackjackEngine() {}

    public record EngineResult(long extraCostToCharge, long payout, boolean roundEnded) {
        public static EngineResult ongoing() { return new EngineResult(0L, 0L, false); }
        public static EngineResult ended(long extraCost, long payout) { return new EngineResult(extraCost, payout, true); }
    }

    // === ROUND START ===
    public static EngineResult startRound(BlackjackRound round, BlackjackDeck deck, long bet) {
        if (bet <= 0) return EngineResult.ongoing();

        round.resetForNewRound(bet);

        round.getPlayerHand().add(deck.draw());
        round.getDealerHand().add(deck.draw());
        round.getPlayerHand().add(deck.draw());
        round.getDealerHand().add(deck.draw());

        return EngineResult.ongoing();
    }

    // ===== PLAYER'S ACTIONS =====

    // --- HIT ENGINE ---
    public static EngineResult playerHit(BlackjackRound round, BlackjackDeck deck) {
        if (!round.canHit()) return EngineResult.ongoing();

        round.getPlayerHand().add(deck.draw());

        if (round.getPlayerHand().isBust()) {
            round.finishWithPayout(0L);
            return EngineResult.ended(0L, 0L);
        }

        return EngineResult.ongoing();
    }

    // --- STAND ENGINE ---
    public static EngineResult playerStand(BlackjackRound round, BlackjackDeck deck) {
        if (!round.canStand()) return EngineResult.ongoing();
        round.setPhase(BlackjackPhase.DEALER_TURN);
        return dealerPlayAndResolve(round, deck);
    }

    // --- DOUBLE DOWN ENGINE ---
    public static EngineResult playerDoubleDown(BlackjackRound round, BlackjackDeck deck, long balance) {
        if (!round.canDoubleDown(balance)) return EngineResult.ongoing();

        long prevBet = round.getCurrentBet();
        long extraCost = prevBet;

        long newBet = safeMul(prevBet, 2);
        round.setCurrentBet(newBet);

        round.getPlayerHand().add(deck.draw());

        if (round.getPlayerHand().isBust()) {
            round.finishWithPayout(0L);
            return EngineResult.ended(extraCost, 0L);
        }

        round.setPhase(BlackjackPhase.DEALER_TURN);
        EngineResult resolved = dealerPlayAndResolve(round, deck);

        if (resolved.roundEnded()) {
            return EngineResult.ended(extraCost, resolved.payout());
        }

        return new EngineResult(extraCost, 0L, false);
    }

    // ===== DEALER'S TURN =====

    private static EngineResult dealerPlayAndResolve(BlackjackRound round, BlackjackDeck deck) {
        round.revealDealerHole();

        BlackjackHand dealer = round.getDealerHand();
        BlackjackHand player = round.getPlayerHand();
        long bet = round.getCurrentBet();

        if (BlackjackRules.DEALER_BLACKJACK_IS_STRONGEST && dealer.isNaturalBlackjack()) {
            long payout = player.isNaturalBlackjack()
                    ? safeMul(bet, BlackjackRules.PUSH_MULTIPLIER)
                    : safeMul(bet, BlackjackRules.LOSE_MULTIPLIER);
            round.finishWithPayout(payout);
            return EngineResult.ended(0L, payout);
        }

        while (shouldDealerHit(dealer)) {
            dealer.add(deck.draw());
            if (dealer.isBust()) break;
        }

        long payout = evaluatePayout(bet, player, dealer);
        round.finishWithPayout(payout);
        return EngineResult.ended(0L, payout);
    }

    private static boolean shouldDealerHit(BlackjackHand dealer) {
        if (dealer.isBust()) return false;

        int v = dealer.bestValue();
        if (v < 17) return true;
        if (v > 17) return false;

        if (dealer.isSoft()) {
            return !BlackjackRules.DEALER_STANDS_ON_SOFT_17;
        }
        return false;
    }

    // === EVALUATION ===
    private static long evaluatePayout(long bet, BlackjackHand player, BlackjackHand dealer) {
        if (player.isBust()) return safeMul(bet, BlackjackRules.LOSE_MULTIPLIER);
        if (dealer.isBust()) return safeMul(bet, BlackjackRules.NORMAL_WIN_MULTIPLIER);

        if (dealer.isNaturalBlackjack()) {
            return player.isNaturalBlackjack()
                    ? safeMul(bet, BlackjackRules.PUSH_MULTIPLIER)
                    : safeMul(bet, BlackjackRules.LOSE_MULTIPLIER);
        }

        if (player.isNaturalBlackjack()) {
            return safeMul(bet, BlackjackRules.BLACKJACK_WIN_MULTIPLIER);
        }

        int pv = player.bestValue();
        int dv = dealer.bestValue();

        if (pv > dv) return safeMul(bet, BlackjackRules.NORMAL_WIN_MULTIPLIER);
        if (pv == dv) return safeMul(bet, BlackjackRules.PUSH_MULTIPLIER);
        return safeMul(bet, BlackjackRules.LOSE_MULTIPLIER);
    }

    // === UTIL ===
    private static long safeMul(long a, long b) {
        if (a < 0 || b < 0) return 0L;
        if (a == 0 || b == 0) return 0L;
        if (a > Long.MAX_VALUE / b) return Long.MAX_VALUE;
        return a * b;
    }

}