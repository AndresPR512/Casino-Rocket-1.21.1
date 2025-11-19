package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record SendSlotBalanceS2CPayload(long amount) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "slot_balance");
    public static final Id<SendSlotBalanceS2CPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, SendSlotBalanceS2CPayload> CODEC =
            PacketCodec.of(SendSlotBalanceS2CPayload::write, SendSlotBalanceS2CPayload::read);

    private static void write(SendSlotBalanceS2CPayload payload, RegistryByteBuf buf) {
        buf.writeLong(payload.amount);
    }

    private static SendSlotBalanceS2CPayload read(RegistryByteBuf buf) {
        return new SendSlotBalanceS2CPayload(buf.readLong());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send(ServerPlayerEntity player, long amount) {
        ServerPlayNetworking.send(player, new SendSlotBalanceS2CPayload(amount));
    }

}