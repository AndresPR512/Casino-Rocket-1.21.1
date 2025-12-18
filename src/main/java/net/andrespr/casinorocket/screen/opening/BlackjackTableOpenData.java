package net.andrespr.casinorocket.screen.opening;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record BlackjackTableOpenData(BlockPos pos, String machineKey, long balance, int betIndex) {

    public static final PacketCodec<RegistryByteBuf, BlackjackTableOpenData> CODEC =
            PacketCodec.of(BlackjackTableOpenData::write, BlackjackTableOpenData::read);

    private static void write(BlackjackTableOpenData data, RegistryByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeString(data.machineKey());
        buf.writeLong(data.balance());
        buf.writeInt(data.betIndex());
    }

    private static BlackjackTableOpenData read(RegistryByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String machineKey = buf.readString();
        long balance = buf.readLong();
        int betIndex = buf.readInt();
        return new BlackjackTableOpenData(pos, machineKey, balance, betIndex);
    }

}