package net.andrespr.casinorocket.screen.opening;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record SlotMachineOpenData(BlockPos pos, String machineKey, long balance, int betBase, int linesMode) {

    public static final PacketCodec<RegistryByteBuf, SlotMachineOpenData> CODEC =
            PacketCodec.of(SlotMachineOpenData::write, SlotMachineOpenData::read);

    private static void write(SlotMachineOpenData data, RegistryByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeString(data.machineKey());
        buf.writeLong(data.balance());
        buf.writeInt(data.betBase());
        buf.writeInt(data.linesMode());
    }

    private static SlotMachineOpenData read(RegistryByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String machineKey = buf.readString();
        long balance = buf.readLong();
        int betBase = buf.readInt();
        int linesMode = buf.readInt();
        return new SlotMachineOpenData(pos, machineKey, balance, betBase, linesMode);
    }

}