package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record SendBetAmountS2CPayload(long amount) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "bet_amount");
    public static final Id<SendBetAmountS2CPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, SendBetAmountS2CPayload> CODEC =
            PacketCodec.of(SendBetAmountS2CPayload::write, SendBetAmountS2CPayload::read);

    private static void write(SendBetAmountS2CPayload payload, RegistryByteBuf buf) {
        buf.writeLong(payload.amount);
    }

    private static SendBetAmountS2CPayload read(RegistryByteBuf buf) {
        return new SendBetAmountS2CPayload(buf.readLong());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send(ServerPlayerEntity player, long amount) {
        ServerPlayNetworking.send(player, new SendBetAmountS2CPayload(amount));
    }

}