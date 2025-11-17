package net.andrespr.casinorocket.network.c2s;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DoBetC2SPayload() implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "do_bet");
    public static final Id<DoBetC2SPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, DoBetC2SPayload> CODEC =
            PacketCodec.unit(new DoBetC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}