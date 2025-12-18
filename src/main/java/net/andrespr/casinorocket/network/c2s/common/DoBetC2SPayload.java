package net.andrespr.casinorocket.network.c2s.common;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record DoBetC2SPayload(String machineKey, BlockPos pos) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "do_bet");
    public static final Id<DoBetC2SPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, DoBetC2SPayload> CODEC =
            PacketCodec.of(DoBetC2SPayload::write, DoBetC2SPayload::read);

    private static void write(DoBetC2SPayload p, RegistryByteBuf buf) {
        buf.writeString(p.machineKey());
        buf.writeBlockPos(p.pos());
    }

    private static DoBetC2SPayload read(RegistryByteBuf buf) {
        return new DoBetC2SPayload(buf.readString(), buf.readBlockPos());
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }

}