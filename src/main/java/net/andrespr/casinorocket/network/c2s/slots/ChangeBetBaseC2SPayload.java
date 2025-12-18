package net.andrespr.casinorocket.network.c2s.slots;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChangeBetBaseC2SPayload(boolean increase) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "change_bet_base");
    public static final Id<ChangeBetBaseC2SPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, ChangeBetBaseC2SPayload> CODEC =
            PacketCodec.of(ChangeBetBaseC2SPayload::write, ChangeBetBaseC2SPayload::read);

    private static void write(ChangeBetBaseC2SPayload payload, RegistryByteBuf buf) {
        buf.writeBoolean(payload.increase());
    }

    private static ChangeBetBaseC2SPayload read(RegistryByteBuf buf) {
        return new ChangeBetBaseC2SPayload(buf.readBoolean());
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }

}