package net.andrespr.casinorocket.network.s2c.sender;

import net.andrespr.casinorocket.network.s2c.SendMachineBalanceS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public final class MachineBalanceSender {

    private MachineBalanceSender() {}

    public static void send(ServerPlayerEntity player, String machineKey, long amount) {
        ServerPlayNetworking.send(player, new SendMachineBalanceS2CPayload(machineKey, amount));
    }

}