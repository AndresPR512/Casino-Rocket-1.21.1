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

public record SendSpinResultS2CPayload(long newBalance, int totalWin, int modeUsed, int[] matrix, List<LineWin> wins) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "spin_result");
    public static final Id<SendSpinResultS2CPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, SendSpinResultS2CPayload> CODEC =
            PacketCodec.of(SendSpinResultS2CPayload::write, SendSpinResultS2CPayload::read);

    public static record LineWin(int symbolOrdinal, int count, int multiplier, int winAmount, int lineIndex) { }

    public static SendSpinResultS2CPayload from(long newBalance, int modeUsed, SlotSpinResult result) {
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
            wins.add(new LineWin(line.symbol().ordinal(), line.count(), line.multiplier(), line.lineWin(), line.lineIndex()));
        }

        return new SendSpinResultS2CPayload(newBalance, result.totalWin(), modeUsed, flat, wins);
    }

    private static void write(SendSpinResultS2CPayload payload, RegistryByteBuf buf) {
        buf.writeLong(payload.newBalance());
        buf.writeInt(payload.totalWin());
        buf.writeInt(payload.modeUsed());

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
            buf.writeInt(w.lineIndex());
        }
    }

    private static SendSpinResultS2CPayload read(RegistryByteBuf buf) {
        long newBalance = buf.readLong();
        int totalWin = buf.readInt();
        int modeUsed = buf.readInt();

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
            int lineIndex = buf.readInt();

            wins.add(new LineWin(symbolOrdinal, count, multiplier, winAmount, lineIndex));
        }

        return new SendSpinResultS2CPayload(newBalance, totalWin, modeUsed, matrix, wins);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}