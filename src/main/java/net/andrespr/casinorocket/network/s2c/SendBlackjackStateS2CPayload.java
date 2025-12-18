package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.blackjack.BlackjackPhase;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record SendBlackjackStateS2CPayload(

        BlockPos pos,
        String machineKey,

        long balance,
        int betIndex,
        long currentBet,

        BlackjackPhase phase,
        long winPayout,

        boolean dealerHoleRevealed,
        int[] playerCards,
        int[] dealerCards,

        String playerValueText,
        String dealerValueText,

        boolean canPlay,
        boolean canHit,
        boolean canStand,
        boolean canDoubleDown,
        boolean canFinish,
        boolean canDoubleOrNothing

) implements CustomPayload {

    public static final Id<SendBlackjackStateS2CPayload> ID =
            new Id<>(Identifier.of(CasinoRocket.MOD_ID, "blackjack_state"));

    public static final PacketCodec<RegistryByteBuf, SendBlackjackStateS2CPayload> CODEC =
            PacketCodec.of(SendBlackjackStateS2CPayload::write, SendBlackjackStateS2CPayload::read);

    private static void write(SendBlackjackStateS2CPayload p, RegistryByteBuf buf) {
        buf.writeBlockPos(p.pos());
        buf.writeString(p.machineKey());

        buf.writeLong(p.balance());
        buf.writeInt(p.betIndex());
        buf.writeLong(p.currentBet());

        buf.writeEnumConstant(p.phase());
        buf.writeLong(p.winPayout());

        buf.writeBoolean(p.dealerHoleRevealed());

        writeIntArray(buf, p.playerCards());
        writeIntArray(buf, p.dealerCards());

        buf.writeString(p.playerValueText());
        buf.writeString(p.dealerValueText());

        buf.writeBoolean(p.canPlay());
        buf.writeBoolean(p.canHit());
        buf.writeBoolean(p.canStand());
        buf.writeBoolean(p.canDoubleDown());
        buf.writeBoolean(p.canFinish());
        buf.writeBoolean(p.canDoubleOrNothing());
    }

    private static SendBlackjackStateS2CPayload read(RegistryByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String key = buf.readString();

        long balance = buf.readLong();
        int betIndex = buf.readInt();
        long currentBet = buf.readLong();

        BlackjackPhase phase = buf.readEnumConstant(BlackjackPhase.class);
        long winPayout = buf.readLong();

        boolean revealed = buf.readBoolean();

        int[] playerCards = readIntArray(buf);
        int[] dealerCards = readIntArray(buf);

        String playerText = buf.readString();
        String dealerText = buf.readString();

        boolean canPlay = buf.readBoolean();
        boolean canHit = buf.readBoolean();
        boolean canStand = buf.readBoolean();
        boolean canDoubleDown = buf.readBoolean();
        boolean canFinish = buf.readBoolean();
        boolean canDoN = buf.readBoolean();

        return new SendBlackjackStateS2CPayload(
                pos, key,
                balance, betIndex, currentBet,
                phase, winPayout,
                revealed, playerCards, dealerCards,
                playerText, dealerText,
                canPlay, canHit, canStand, canDoubleDown, canFinish, canDoN
        );
    }

    private static void writeIntArray(RegistryByteBuf buf, int[] arr) {
        buf.writeVarInt(arr.length);
        for (int v : arr) buf.writeVarInt(v);
    }

    private static int[] readIntArray(RegistryByteBuf buf) {
        int n = buf.readVarInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = buf.readVarInt();
        return arr;
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }

}