package net.andrespr.casinorocket.network.c2s;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChangeLinesModeC2SPayload(int mode) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "change_lines_mode");
    public static final Id<ChangeLinesModeC2SPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, ChangeLinesModeC2SPayload> CODEC =
            PacketCodec.of(ChangeLinesModeC2SPayload::write, ChangeLinesModeC2SPayload::read);

    private static void write(ChangeLinesModeC2SPayload payload, RegistryByteBuf buf) {
        buf.writeInt(payload.mode());
    }

    private static ChangeLinesModeC2SPayload read(RegistryByteBuf buf) {
        return new ChangeLinesModeC2SPayload(buf.readInt());
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }

}