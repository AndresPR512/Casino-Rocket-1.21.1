package net.andrespr.casinorocket.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SuitSyncPayload(int entityId, int suitValue) implements CustomPayload {

    public static final Id<SuitSyncPayload> ID = new Id<>(Identifier.of("casinorocket", "sync_suit"));

    public static final PacketCodec<PacketByteBuf, SuitSyncPayload> CODEC = PacketCodec.of(
            (payload, buf) -> { // Encoder
                buf.writeInt(payload.entityId());
                buf.writeInt(payload.suitValue());
            },
            buf -> new SuitSyncPayload(buf.readInt(), buf.readInt()) // Decoder
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}