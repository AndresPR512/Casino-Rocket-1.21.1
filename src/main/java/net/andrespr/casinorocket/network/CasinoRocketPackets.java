package net.andrespr.casinorocket.network;

import net.andrespr.casinorocket.network.c2s.*;
import net.andrespr.casinorocket.network.c2s_handlers.*;
import net.andrespr.casinorocket.network.s2c.SendBetAmountS2CPayload;
import net.andrespr.casinorocket.network.s2c.SendSlotBalanceS2CPayload;
import net.andrespr.casinorocket.network.s2c_handlers.BetScreenAmountReceiver;
import net.andrespr.casinorocket.network.s2c_handlers.SlotBalanceReceiver;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class CasinoRocketPackets {

    public static void register() {
        registerC2S();
        registerS2C();
    }

    private static void registerC2S() {

        // BET SCREEN
        PayloadTypeRegistry.playC2S().register(
                OpenBetScreenC2SPayload.ID,
                OpenBetScreenC2SPayload.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                OpenBetScreenC2SPayload.ID,
                BetScreenReceiver::openBetScreen
        );

        // WITHDRAW SCREEN
        PayloadTypeRegistry.playC2S().register(
                OpenWithdrawScreenC2SPayload.ID,
                OpenWithdrawScreenC2SPayload.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                OpenWithdrawScreenC2SPayload.ID,
                WithdrawScreenReceiver::openWithdrawScreen
        );

        // DO BET PRESSED
        PayloadTypeRegistry.playC2S().register(
                DoBetC2SPayload.ID,
                DoBetC2SPayload.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
                DoBetC2SPayload.ID,
                DoBetReceiver::handle
        );

        // DO WITHDRAW PRESSED
        PayloadTypeRegistry.playC2S().register(
                DoWithdrawC2SPayload.ID,
                DoWithdrawC2SPayload.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
                DoWithdrawC2SPayload.ID,
                DoWithdrawReceiver::handle
        );

    }

    private static void registerS2C() {

        PayloadTypeRegistry.playS2C().register(
                SendBetAmountS2CPayload.ID,
                SendBetAmountS2CPayload.CODEC
        );
        BetScreenAmountReceiver.register();

        PayloadTypeRegistry.playS2C().register(
                SendSlotBalanceS2CPayload.ID,
                SendSlotBalanceS2CPayload.CODEC
        );
        SlotBalanceReceiver.register();

    }

}