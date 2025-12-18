package net.andrespr.casinorocket.network.c2s.common;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenBetScreenC2SPayload() implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "open_bet_screen");
    public static final Id<OpenBetScreenC2SPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, OpenBetScreenC2SPayload> CODEC =
            PacketCodec.unit(new OpenBetScreenC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}