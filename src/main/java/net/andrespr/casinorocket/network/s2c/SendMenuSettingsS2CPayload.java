package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SendMenuSettingsS2CPayload(long balance, int betBase, int linesMode) implements CustomPayload {

    public static final Identifier ID_RAW = Identifier.of(CasinoRocket.MOD_ID, "slot_machine_menu_settings");
    public static final Id<SendMenuSettingsS2CPayload> ID = new Id<>(ID_RAW);

    public static final PacketCodec<RegistryByteBuf, SendMenuSettingsS2CPayload> CODEC =
            PacketCodec.of(SendMenuSettingsS2CPayload::write, SendMenuSettingsS2CPayload::read);

    private static void write(SendMenuSettingsS2CPayload payload, RegistryByteBuf buf) {
        buf.writeLong(payload.balance);
        buf.writeInt(payload.betBase);
        buf.writeInt(payload.linesMode);
    }

    private static SendMenuSettingsS2CPayload read(RegistryByteBuf buf) {
        return new SendMenuSettingsS2CPayload(
                buf.readLong(),
                buf.readInt(),
                buf.readInt()
        );
    }

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }

}