package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.slot.SlotLineResult;
import net.andrespr.casinorocket.games.slot.SlotSpinResult;
import net.andrespr.casinorocket.games.slot.SlotSymbol;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record SendSpinResultS2CPayload(long newBalance, int totalWin, int[] matrix, List<LineWin> wins) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "spin_result");
    public static final Id<SendSpinResultS2CPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, SendSpinResultS2CPayload> CODEC =
            PacketCodec.of(SendSpinResultS2CPayload::write, SendSpinResultS2CPayload::read);

    public static record LineWin(int symbolOrdinal, int count, int multiplier, int winAmount) { }

    public static SendSpinResultS2CPayload from(long newBalance, SlotSpinResult result) {
        SlotSymbol[][] matrixSymbols = result.matrix();
        int[] flat = new int[9];
        int idx = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                flat[idx++] = matrixSymbols[row][col].ordinal();
            }
        }

        List<LineWin> wins = new ArrayList<>();
        for (SlotLineResult line : result.lines()) {
            if (!line.win()) continue;
            wins.add(new LineWin(line.symbol().ordinal(), line.count(), line.multiplier(), line.lineWin()));
        }

        return new SendSpinResultS2CPayload(newBalance, result.totalWin(), flat, wins);
    }

    private static void write(SendSpinResultS2CPayload payload, RegistryByteBuf buf) {
        buf.writeLong(payload.newBalance());
        buf.writeInt(payload.totalWin());

        int[] m = payload.matrix();
        for (int i = 0; i < 9; i++) {
            buf.writeInt(m[i]);
        }

        List<LineWin> wins = payload.wins();
        buf.writeInt(wins.size());
        for (LineWin w : wins) {
            buf.writeInt(w.symbolOrdinal());
            buf.writeInt(w.count());
            buf.writeInt(w.multiplier());
            buf.writeInt(w.winAmount());
        }
    }

    private static SendSpinResultS2CPayload read(RegistryByteBuf buf) {
        long newBalance = buf.readLong();
        int totalWin = buf.readInt();

        int[] matrix = new int[9];
        for (int i = 0; i < 9; i++) {
            matrix[i] = buf.readInt();
        }

        int winCount = buf.readInt();
        List<LineWin> wins = new ArrayList<>(winCount);
        for (int i = 0; i < winCount; i++) {
            int symbolOrdinal = buf.readInt();
            int count = buf.readInt();
            int multiplier = buf.readInt();
            int winAmount = buf.readInt();
            wins.add(new LineWin(symbolOrdinal, count, multiplier, winAmount));
        }

        return new SendSpinResultS2CPayload(newBalance, totalWin, matrix, wins);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}