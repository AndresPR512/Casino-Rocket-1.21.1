package net.andrespr.casinorocket.network.s2c.sender;

import net.andrespr.casinorocket.network.s2c.SendSlotBalanceS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class SlotBalanceSender {

    public static void send(ServerPlayerEntity player, long amount) {
        ServerPlayNetworking.send(player, new SendSlotBalanceS2CPayload(amount));
    }

}