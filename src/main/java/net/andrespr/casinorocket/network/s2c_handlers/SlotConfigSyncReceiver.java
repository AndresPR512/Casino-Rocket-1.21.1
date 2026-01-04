package net.andrespr.casinorocket.network.s2c_handlers;

import net.andrespr.casinorocket.games.slot.SlotClientSynced;
import net.andrespr.casinorocket.games.slot.SlotReels;
import net.andrespr.casinorocket.games.slot.SlotSymbol;
import net.andrespr.casinorocket.network.s2c.SlotConfigSyncS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public final class SlotConfigSyncReceiver {

    public static volatile long LAST_HASH = -1;

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(SlotConfigSyncS2CPayload.ID, (payload, ctx) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.execute(() -> {
                SlotSymbol[] symbols = SlotSymbol.values();

                SlotSymbol[] r1 = fromOrdinals(payload.reel1(), symbols);
                SlotSymbol[] r2 = fromOrdinals(payload.reel2(), symbols);
                SlotSymbol[] r3 = fromOrdinals(payload.reel3(), symbols);

                SlotReels.applySyncedStrips(payload.reelSize(), r1, r2, r3);
                SlotClientSynced.apply(payload.debug(), payload.betValues(), payload.mode1(), payload.mode2(), payload.mode3());
                LAST_HASH = payload.hash();
            });
        });
    }

    private static SlotSymbol[] fromOrdinals(int[] ords, SlotSymbol[] symbols) {
        SlotSymbol[] out = new SlotSymbol[ords.length];
        for (int i = 0; i < ords.length; i++) out[i] = symbols[ords[i]];
        return out;
    }

}