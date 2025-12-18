package net.andrespr.casinorocket.screen.opening;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record CommonMachineOpenData(BlockPos pos, String machineKey) {

    public static final PacketCodec<RegistryByteBuf, CommonMachineOpenData> CODEC =
            PacketCodec.of(CommonMachineOpenData::write, CommonMachineOpenData::read);

    private static void write(CommonMachineOpenData data, RegistryByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeString(data.machineKey());
    }

    private static CommonMachineOpenData read(RegistryByteBuf buf) {
        return new CommonMachineOpenData(
                buf.readBlockPos(),
                buf.readString()
        );
    }

}