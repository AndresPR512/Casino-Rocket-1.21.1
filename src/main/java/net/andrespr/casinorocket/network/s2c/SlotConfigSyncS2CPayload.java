package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.slot.SlotSymbol;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public record SlotConfigSyncS2CPayload(boolean debug, int reelSize, int[] reel1, int[] reel2, int[] reel3,
                                       int[] betValues, int mode1, int mode2, int mode3, long hash) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "slot_cfg_sync");
    public static final Id<SlotConfigSyncS2CPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, SlotConfigSyncS2CPayload> CODEC =
            PacketCodec.of(SlotConfigSyncS2CPayload::write, SlotConfigSyncS2CPayload::read);

    private static void write(SlotConfigSyncS2CPayload p, RegistryByteBuf buf) {
        buf.writeBoolean(p.debug());
        buf.writeInt(p.reelSize());

        writeIntArray(buf, p.reel1());
        writeIntArray(buf, p.reel2());
        writeIntArray(buf, p.reel3());

        writeIntArray(buf, p.betValues());

        buf.writeInt(p.mode1());
        buf.writeInt(p.mode2());
        buf.writeInt(p.mode3());

        buf.writeLong(p.hash());
    }

    private static SlotConfigSyncS2CPayload read(RegistryByteBuf buf) {
        boolean debug = buf.readBoolean();
        int reelSize = buf.readInt();

        int[] r1 = readIntArray(buf);
        int[] r2 = readIntArray(buf);
        int[] r3 = readIntArray(buf);

        int[] betValues = readIntArray(buf);

        int mode1 = buf.readInt();
        int mode2 = buf.readInt();
        int mode3 = buf.readInt();

        long hash = buf.readLong();

        return new SlotConfigSyncS2CPayload(debug, reelSize, r1, r2, r3, betValues, mode1, mode2, mode3, hash);
    }

    private static void writeIntArray(RegistryByteBuf buf, int[] arr) {
        buf.writeInt(arr.length);
        for (int v : arr) buf.writeInt(v);
    }

    private static int[] readIntArray(RegistryByteBuf buf) {
        int len = buf.readInt();
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) arr[i] = buf.readInt();
        return arr;
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }

    // ===== Helpers to build from server state =====
    public static SlotConfigSyncS2CPayload fromServer() {
        var cfg = CasinoRocket.CONFIG.slotMachine;

        SlotSymbol[][] strips = net.andrespr.casinorocket.games.slot.SlotReels.STRIPS;
        int[] r1 = toOrdinalArray(strips[0]);
        int[] r2 = toOrdinalArray(strips[1]);
        int[] r3 = toOrdinalArray(strips[2]);

        int[] bets = cfg.betValues.stream().mapToInt(Integer::intValue).toArray();

        long hash = Arrays.hashCode(r1) * 31L * 31L + Arrays.hashCode(r2) * 31L + Arrays.hashCode(r3);

        hash = hash * 31L + Arrays.hashCode(bets);
        hash = hash * 31L + cfg.betMultipliers.mode1;
        hash = hash * 31L + cfg.betMultipliers.mode2;
        hash = hash * 31L + cfg.betMultipliers.mode3;
        hash = hash * 31L + cfg.reels.reelSize;

        return new SlotConfigSyncS2CPayload(
                cfg.debug,
                cfg.reels.reelSize,
                r1, r2, r3, bets,
                cfg.betMultipliers.mode1,
                cfg.betMultipliers.mode2,
                cfg.betMultipliers.mode3,
                hash
        );
    }

    private static int[] toOrdinalArray(SlotSymbol[] reel) {
        int[] out = new int[reel.length];
        for (int i = 0; i < reel.length; i++) out[i] = reel[i].ordinal();
        return out;
    }

}