package net.andrespr.casinorocket.network.c2s_handlers.slots;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.games.slot.SlotMachineConstants;
import net.andrespr.casinorocket.games.slot.SlotSpinEngine;
import net.andrespr.casinorocket.games.slot.SlotSpinResult;
import net.andrespr.casinorocket.network.c2s.slots.DoSpinC2SPayload;
import net.andrespr.casinorocket.network.s2c.SendSpinResultS2CPayload;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreenHandler;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class DoSpinReceiver {

    public static void handle(DoSpinC2SPayload payload, ServerPlayNetworking.Context context) {

        ServerPlayerEntity player = context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.currentScreenHandler instanceof SlotMachineScreenHandler)) {
            return;
        }

        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUuid();

        long balance = storage.getBalance(uuid);
        int betBase = storage.getBetBase(uuid);
        int linesMode = storage.getLinesMode(uuid);

        if (betBase <= 0 || linesMode <= 0) return;

        int betMultiplier = SlotMachineConstants.getBetMultiplierForMode(linesMode);
        long cost = (long) betBase * betMultiplier;

        if (balance < cost) return;
        storage.addTotalSpent(uuid, cost);

        long afterCost = balance - cost;

        SlotSpinEngine.SpinOutcome outcome = SlotSpinEngine.spinOutcome(betBase, linesMode);
        SlotSpinEngine.SpinStop stop = outcome.stop();
        SlotSpinResult spinResult = outcome.result();

        int stop1 = stop.index1();
        int stop2 = stop.index2();
        int stop3 = stop.index3();

        long spinWin = spinResult.totalWin();
        storage.addTotalWon(uuid, spinWin);
        storage.setLastWin(uuid, spinWin);
        storage.updateHighestWin(uuid, spinWin);

        long newBalance = MoneyCalculator.safeAdd(afterCost, spinWin, SlotMachineConstants.MAX_BALANCE);
        storage.setBalance(uuid, newBalance);

        if (CasinoRocket.CONFIG.slotMachine.debug) {
            CasinoRocket.LOGGER.info("[SlotDebug] {} stops=({}, {}, {}) mid=[{}, {}, {}] win={}",
                    player.getName().getString(),
                    stop1, stop2, stop3,
                    spinResult.matrix()[1][0], spinResult.matrix()[1][1], spinResult.matrix()[1][2],
                    spinResult.totalWin()
            );
        }

        ServerPlayNetworking.send(player,
                SendSpinResultS2CPayload.from(newBalance, linesMode, stop1, stop2, stop3, spinResult)
        );
    }

}