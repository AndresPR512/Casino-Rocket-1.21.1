package net.andrespr.casinorocket.network;

import net.andrespr.casinorocket.util.SuitData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class SuitSync {

    // CLIENT
    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(SuitSyncPayload.ID, (payload, context) -> {
            int entityId = payload.entityId();
            int suitValue = payload.suitValue();

            MinecraftClient.getInstance().execute(() -> {
                var mc = MinecraftClient.getInstance();
                if (mc.world == null) return;

                Entity entity = mc.world.getEntityById(entityId);
                if (entity instanceof VillagerEntity villager) {
                    SuitData.setSuitClient(villager, suitValue);
                }
            });
        });
    }

    // SERVER
    public static void sendSuitSync(VillagerEntity villager, int suitValue) {
        SuitSyncPayload payload = new SuitSyncPayload(villager.getId(), suitValue);
        for (ServerPlayerEntity player : PlayerLookup.tracking(villager)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

}