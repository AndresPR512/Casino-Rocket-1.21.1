package net.andrespr.casinorocket.games.blackjack;

import java.util.List;

public final class BlackjackRules {

    private BlackjackRules() {}

    public static final boolean BLACKJACK_ONLY_ACE_PLUS_TEN = true;
    public static final boolean DEALER_STANDS_ON_SOFT_17 = true;
    public static final boolean DEALER_BLACKJACK_IS_STRONGEST = true;
    public static final int NORMAL_WIN_MULTIPLIER = 2;
    public static final int BLACKJACK_WIN_MULTIPLIER = 3;
    public static final int PUSH_MULTIPLIER = 1;
    public static final int LOSE_MULTIPLIER = 0;
    public static final boolean DOUBLE_DOWN_ALWAYS_ALLOWED = true;

    public static final List<Long> BET_VALUES = List.of(
            10L, 25L, 50L,
            100L, 250L, 500L,
            1_000L, 2_500L, 5_000L,
            10_000L, 25_000L, 50_000L,
            100_000L, 250_000L, 500_000L,
            1_000_000L
    );

    public static final int DEFAULT_BET_INDEX = 0;

}