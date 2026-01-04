package net.andrespr.casinorocket.games.slot;

import me.shedaniel.autoconfig.AutoConfig;
import net.andrespr.casinorocket.config.CasinoRocketConfig;
import net.andrespr.casinorocket.config.SlotMachineConfig;

import java.util.List;

public final class SlotMachineConstants {

    private static final List<Integer> FALLBACK_BET_VALUES =
            List.of(10, 25, 50, 100, 250, 500, 1_000, 5_000, 10_000, 25_000, 50_000, 100_000, 250_000, 500_000, 1_000_000, 5_000_000);
    
    public static final long MAX_BALANCE = 9_000_000_000_000_000_000L;

    private SlotMachineConstants() {}

    private static SlotMachineConfig cfg() {
        CasinoRocketConfig root = AutoConfig.getConfigHolder(CasinoRocketConfig.class).getConfig();
        return root.slotMachine;
    }

    public static List<Integer> betValues() {

        if (SlotClientSynced.has()) {
            return SlotClientSynced.BET_VALUES;
        }

        try {
            List<Integer> v = cfg().betValues;
            return (v == null || v.isEmpty()) ? FALLBACK_BET_VALUES : v;
        } catch (Exception e) {
            return FALLBACK_BET_VALUES;
        }

    }

    public static int defaultBetBase() {
        List<Integer> v = betValues();
        return v.isEmpty() ? 10 : v.getFirst();
    }

    public static int defaultLinesMode() {
        return 1;
    }

    public static int getBetMultiplierForMode(int mode) {

        if (SlotClientSynced.has()) {
            return switch (mode) {
                case 2 -> SlotClientSynced.MODE2;
                case 3 -> SlotClientSynced.MODE3;
                default -> SlotClientSynced.MODE1;
            };
        }

        try {
            SlotMachineConfig c = cfg();
            return switch (mode) {
                case 2 -> Math.max(1, c.betMultipliers.mode2);
                case 3 -> Math.max(1, c.betMultipliers.mode3);
                default -> Math.max(1, c.betMultipliers.mode1);
            };
        } catch (Exception e) {
            return switch (mode) {
                case 2 -> 3;
                case 3 -> 5;
                default -> 1;
            };
        }

    }

}