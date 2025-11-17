package net.andrespr.casinorocket.network.c2s;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenWithdrawScreenC2SPayload() implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "open_withdraw_screen");
    public static final Id<OpenWithdrawScreenC2SPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, OpenWithdrawScreenC2SPayload> CODEC =
            PacketCodec.unit(new OpenWithdrawScreenC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}