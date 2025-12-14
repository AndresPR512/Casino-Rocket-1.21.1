package net.andrespr.casinorocket.screen.opening;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record SlotMachineOpenData(BlockPos pos, long balance, int betBase, int linesMode) {

    public static final PacketCodec<RegistryByteBuf, SlotMachineOpenData> CODEC =
            PacketCodec.of(SlotMachineOpenData::write, SlotMachineOpenData::read);

    private static void write(SlotMachineOpenData data, RegistryByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeLong(data.balance());
        buf.writeInt(data.betBase());
        buf.writeInt(data.linesMode());
    }

    private static SlotMachineOpenData read(RegistryByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        long balance = buf.readLong();
        int betBase = buf.readInt();
        int linesMode = buf.readInt();
        return new SlotMachineOpenData(pos, balance, betBase, linesMode);
    }

}