package net.andrespr.casinorocket.network;

import net.andrespr.casinorocket.network.c2s.*;
import net.andrespr.casinorocket.network.c2s_handlers.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class CasinoPackets {

    public static void register() {
        registerC2S();
        registerS2C();
    }

    private static void registerC2S() {

        // BET
        PayloadTypeRegistry.playC2S().register(
                OpenBetScreenC2SPayload.ID,
                OpenBetScreenC2SPayload.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                OpenBetScreenC2SPayload.ID,
                BetScreenReceiver::openBetScreen
        );

        // WITHDRAW
        PayloadTypeRegistry.playC2S().register(
                OpenWithdrawScreenC2SPayload.ID,
                OpenWithdrawScreenC2SPayload.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                OpenWithdrawScreenC2SPayload.ID,
                WithdrawScreenReceiver::openWithdrawScreen
        );

    }

    private static void registerS2C() { }

}