package net.andrespr.casinorocket.network.c2s.blackjack;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChangeBlackjackBetIndexC2SPayload(boolean increase) implements CustomPayload {

    public static final Id<ChangeBlackjackBetIndexC2SPayload> ID =
            new Id<>(Identifier.of(CasinoRocket.MOD_ID, "blackjack_change_bet_index"));

    public static final PacketCodec<RegistryByteBuf, ChangeBlackjackBetIndexC2SPayload> CODEC =
            PacketCodec.of(ChangeBlackjackBetIndexC2SPayload::write, ChangeBlackjackBetIndexC2SPayload::read);

    private static void write(ChangeBlackjackBetIndexC2SPayload p, RegistryByteBuf buf) {
        buf.writeBoolean(p.increase());
    }

    private static ChangeBlackjackBetIndexC2SPayload read(RegistryByteBuf buf) {
        return new ChangeBlackjackBetIndexC2SPayload(buf.readBoolean());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}