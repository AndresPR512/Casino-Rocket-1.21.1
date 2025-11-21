package net.andrespr.casinorocket.network.s2c_handlers;

import net.andrespr.casinorocket.games.slot.SlotLineResult;
import net.andrespr.casinorocket.games.slot.SlotSymbol;
import net.andrespr.casinorocket.network.s2c.SendSpinResultS2CPayload;
import net.andrespr.casinorocket.screen.custom.SlotMachineScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class SpinResultReceiver {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                SendSpinResultS2CPayload.ID,
                (payload, context) -> {

                    long newBalance = payload.newBalance();
                    int totalWin = payload.totalWin();
                    int[] flatMatrix = payload.matrix();
                    List<SendSpinResultS2CPayload.LineWin> netWins = payload.wins();

                    SlotSymbol[][] matrix = new SlotSymbol[3][3];
                    SlotSymbol[] symbols = SlotSymbol.values();

                    int idx = 0;
                    for (int row = 0; row < 3; row++) {
                        for (int col = 0; col < 3; col++) {
                            int ord = flatMatrix[idx++];
                            matrix[row][col] = symbols[ord];
                        }
                    }

                    List<SlotLineResult> wins = new ArrayList<>();
                    for (SendSpinResultS2CPayload.LineWin w : netWins) {
                        SlotSymbol symbol = symbols[w.symbolOrdinal()];
                        wins.add(new SlotLineResult(true, symbol, w.count(), w.multiplier(), w.winAmount()));
                    }

                    MinecraftClient client = MinecraftClient.getInstance();
                    client.execute(() -> {
                        if (client.currentScreen instanceof SlotMachineScreen screen) {
                            screen.onSpinResult(matrix, wins, totalWin, newBalance);
                        }
                    });

                }
        );
    }

}