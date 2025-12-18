package net.andrespr.casinorocket.network.c2s.common;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ReturnToMachineScreenC2SPayload(BlockPos pos, String machineKey) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "return_to_machine");
    public static final Id<ReturnToMachineScreenC2SPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, ReturnToMachineScreenC2SPayload> CODEC =
            PacketCodec.of(ReturnToMachineScreenC2SPayload::write, ReturnToMachineScreenC2SPayload::read);

    private static void write(ReturnToMachineScreenC2SPayload p, RegistryByteBuf buf) {
        buf.writeBlockPos(p.pos());
        buf.writeString(p.machineKey());
    }

    private static ReturnToMachineScreenC2SPayload read(RegistryByteBuf buf) {
        return new ReturnToMachineScreenC2SPayload(buf.readBlockPos(), buf.readString());
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }

}