package net.andrespr.casinorocket.network.c2s;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record DoWithdrawC2SPayload(String machineKey, BlockPos pos) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "do_withdraw");
    public static final Id<DoWithdrawC2SPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, DoWithdrawC2SPayload> CODEC =
            PacketCodec.of(DoWithdrawC2SPayload::write, DoWithdrawC2SPayload::read);

    private static void write(DoWithdrawC2SPayload p, RegistryByteBuf buf) {
        buf.writeString(p.machineKey());
        buf.writeBlockPos(p.pos());
    }

    private static DoWithdrawC2SPayload read(RegistryByteBuf buf) {
        String key = buf.readString();
        BlockPos pos = buf.readBlockPos();
        return new DoWithdrawC2SPayload(key, pos);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }

}