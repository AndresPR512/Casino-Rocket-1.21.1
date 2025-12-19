package net.andrespr.casinorocket.games.blackjack;

import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public final class BlackjackGameController {

    private final UUID playerId;
    private final PlayerBlackjackData storage;

    private final BlackjackDeck deck = new BlackjackDeck();
    private final BlackjackRound round = new BlackjackRound();

    private int resultId = 0;
    private long lastResolvedBet = 0L;
    private long lastResolvedPayout = 0L;

    public BlackjackGameController(UUID playerId, PlayerBlackjackData storage) {
        this.playerId = playerId;
        this.storage = storage;
    }

    // === PLAY ===
    public boolean play() {
        long bet = storage.getBetAmount(playerId);
        long balance = storage.getBalance(playerId);

        if (balance < bet) return false;

        storage.setBalance(playerId, balance - bet);
        storage.addTotalSpent(playerId, bet);

        BlackjackEngine.startRound(round, deck, bet);
        return true;
    }

    // === GAME ACTIONS ===
    public void hit() {
        long betBefore = round.getCurrentBet();
        BlackjackEngine.EngineResult res = BlackjackEngine.playerHit(round, deck);
        if (res.roundEnded()) {
            storage.setLastWin(playerId, res.payout());
            setResolvedResult(betBefore, res.payout());
        }
    }

    public void stand() {
        long betBefore = round.getCurrentBet();

        BlackjackEngine.EngineResult res = BlackjackEngine.playerStand(round, deck);
        if (res.roundEnded()) {
            storage.setLastWin(playerId, res.payout());
            setResolvedResult(betBefore, res.payout());
        }
    }

    public boolean doubleDown() {
        long balance = storage.getBalance(playerId);
        long betBefore = round.getCurrentBet();

        BlackjackEngine.EngineResult res =
                BlackjackEngine.playerDoubleDown(round, deck, balance);

        if (res.extraCostToCharge() > 0) {
            storage.setBalance(playerId, balance - res.extraCostToCharge());
            storage.addTotalSpent(playerId, res.extraCostToCharge());
        }

        if (res.roundEnded()) {
            storage.setLastWin(playerId, res.payout());
            setResolvedResult(betBefore + Math.max(0L, res.extraCostToCharge()), res.payout());
        }

        return res.extraCostToCharge() > 0;
    }

    // === FINISH ===
    public void finish() {
        if (!round.canFinish()) return;

        long win = round.getWinPayout();
        long balance = storage.getBalance(playerId);

        storage.setBalance(playerId, balance + win);

        storage.addTotalWon(playerId, win);
        storage.setLastWin(playerId, win);
        storage.updateHighestWin(playerId, win);

        round.setPhase(BlackjackPhase.IDLE);
    }

    // === DOUBLE OR NOTHING ===
    public boolean doubleOrNothing() {
        if (!round.canDoubleOrNothing()) return false;

        long newBet = round.getWinPayout();
        if (newBet <= 0) return false;

        BlackjackEngine.startRound(round, deck, newBet);
        return true;
    }

    // === DECIDE ACTION ===
    public void handleAction(ServerPlayerEntity player, BlackjackAction action) {
        switch (action) {
            case PLAY -> play();
            case HIT -> hit();
            case STAND -> stand();
            case DOUBLE_DOWN -> doubleDown();
            case FINISH -> finish();
            case DOUBLE_OR_NOTHING -> doubleOrNothing();
        }
    }

    // === GETTERS ===
    public BlackjackRound getRound() { return round; }
    public int getResultId() { return resultId; }
    public long getLastResolvedBet() { return lastResolvedBet; }
    public long getLastResolvedPayout() { return lastResolvedPayout; }

    // === SETTERS ===
    private void setResolvedResult(long bet, long payout) {
        if (bet <= 0) return;
        this.resultId++;
        this.lastResolvedBet = Math.max(0L, bet);
        this.lastResolvedPayout = Math.max(0L, payout);
    }

}