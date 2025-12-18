package net.andrespr.casinorocket.network.c2s.blackjack;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.blackjack.BlackjackAction;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record BlackjackActionC2SPayload(BlockPos pos, String machineKey, BlackjackAction action) implements CustomPayload {

    public static final Id<BlackjackActionC2SPayload> ID = new Id<>(Identifier.of(CasinoRocket.MOD_ID, "blackjack_action"));

    public static final PacketCodec<RegistryByteBuf, BlackjackActionC2SPayload> CODEC =
            PacketCodec.of(BlackjackActionC2SPayload::write, BlackjackActionC2SPayload::read);

    private static void write(BlackjackActionC2SPayload p, RegistryByteBuf buf) {
        buf.writeBlockPos(p.pos());
        buf.writeString(p.machineKey());
        buf.writeEnumConstant(p.action());
    }

    private static BlackjackActionC2SPayload read(RegistryByteBuf buf) {
        return new BlackjackActionC2SPayload(
                buf.readBlockPos(),
                buf.readString(),
                buf.readEnumConstant(BlackjackAction.class)
        );
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}