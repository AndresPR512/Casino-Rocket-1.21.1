package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SendMachineBalanceS2CPayload(String machineKey, long amount) implements CustomPayload {

    public static final Id<SendMachineBalanceS2CPayload> ID =
            new Id<>(Identifier.of(CasinoRocket.MOD_ID, "machine_balance"));

    public static final PacketCodec<RegistryByteBuf, SendMachineBalanceS2CPayload> CODEC =
            PacketCodec.of(SendMachineBalanceS2CPayload::write,
                    SendMachineBalanceS2CPayload::read);

    private static void write(SendMachineBalanceS2CPayload payload, RegistryByteBuf buf) {
        buf.writeString(payload.machineKey());
        buf.writeLong(payload.amount());
    }

    private static SendMachineBalanceS2CPayload read(RegistryByteBuf buf) {
        String machineKey = buf.readString();
        long amount = buf.readLong();
        return new SendMachineBalanceS2CPayload(machineKey, amount);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}