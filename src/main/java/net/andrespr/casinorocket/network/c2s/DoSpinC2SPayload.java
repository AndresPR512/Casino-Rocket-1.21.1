package net.andrespr.casinorocket.network.c2s;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DoSpinC2SPayload() implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "do_spin");
    public static final Id<DoSpinC2SPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, DoSpinC2SPayload> CODEC =
            PacketCodec.unit(new DoSpinC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}