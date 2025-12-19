package net.andrespr.casinorocket.games.blackjack;

public final class BlackjackRound {

    private BlackjackPhase phase = BlackjackPhase.IDLE;

    private long currentBet;
    private long winPayout;

    private final BlackjackHand playerHand = new BlackjackHand();
    private final BlackjackHand dealerHand = new BlackjackHand();

    private boolean dealerHoleHidden = true;

    private int resultSeq = 0;
    private int resultId = 0;
    private long resolvedBet = 0L;
    private long resolvedPayout = 0L;

    // === GETTERS ===
    public BlackjackPhase getPhase() { return phase; }
    public long getCurrentBet() { return currentBet; }
    public long getWinPayout() { return winPayout; }
    public BlackjackHand getPlayerHand() { return playerHand; }
    public BlackjackHand getDealerHand() { return dealerHand; }
    public boolean isDealerHoleHidden() { return dealerHoleHidden; }
    public int getResultSeq() { return resultSeq; }
    public int getResultId() { return resultId; }
    public long getResolvedBet() { return resolvedBet; }
    public long getResolvedPayout() { return resolvedPayout; }

    // === SETTERS ===
    public void setPhase(BlackjackPhase phase) { this.phase = phase; }
    public void setCurrentBet(long currentBet) { this.currentBet = Math.max(0L, currentBet); }
    public void setWinPayout(long winPayout) { this.winPayout = Math.max(0L, winPayout); }
    public void revealDealerHole() { this.dealerHoleHidden = false; }

    // === GAME CYCLE ===
    public void resetForNewRound(long bet) {
        playerHand.clear();
        dealerHand.clear();

        this.currentBet = bet;
        this.winPayout = 0L;
        this.dealerHoleHidden = true;
        this.phase = BlackjackPhase.PLAYER_TURN;
    }

    public void finishWithPayout(long payout) {
        this.winPayout = Math.max(0L, payout);
        this.resolvedBet = this.currentBet;
        this.resolvedPayout = this.winPayout;
        this.resultId++;

        if (this.winPayout == 0L) {
            this.phase = BlackjackPhase.IDLE;
        } else {
            this.phase = BlackjackPhase.RESULT_PENDING;
        }
    }

    // === VALIDATIONS ===
    public boolean canHit() { return phase == BlackjackPhase.PLAYER_TURN && !playerHand.isBust(); }

    public boolean canStand() { return phase == BlackjackPhase.PLAYER_TURN; }

    public boolean canDoubleDown(long balance) {
        if (phase != BlackjackPhase.PLAYER_TURN) return false;
        if (!BlackjackRules.DOUBLE_DOWN_ALWAYS_ALLOWED) return false;
        if (playerHand.isBust()) return false;
        return balance >= currentBet;
    }

    public boolean canFinish() { return phase == BlackjackPhase.RESULT_PENDING; }

    public boolean canDoubleOrNothing() {
        return phase == BlackjackPhase.RESULT_PENDING && winPayout > 0;
    }

}