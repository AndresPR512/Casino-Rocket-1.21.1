package net.andrespr.casinorocket.network;

import net.andrespr.casinorocket.network.c2s.*;
import net.andrespr.casinorocket.network.c2s_handlers.*;
import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.network.s2c.SendSlotBalanceS2CPayload;
import net.andrespr.casinorocket.network.s2c_handlers.MenuScreenSettingsReceiver;
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

        // MENU SCREEN
        PayloadTypeRegistry.playC2S().register(
                OpenMenuScreenC2SPayload.ID,
                OpenMenuScreenC2SPayload.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                OpenMenuScreenC2SPayload.ID,
                MenuScreenReceiver::openMenuScreen
        );

        // CHANGE BET BASE
        PayloadTypeRegistry.playC2S().register(
                ChangeBetBaseC2SPayload.ID,
                ChangeBetBaseC2SPayload.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                ChangeBetBaseC2SPayload.ID,
                ChangeBetBaseReceiver::handle
        );

        // CHANGE LINES MODE
        PayloadTypeRegistry.playC2S().register(
                ChangeLinesModeC2SPayload.ID,
                ChangeLinesModeC2SPayload.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                ChangeLinesModeC2SPayload.ID,
                ChangeLinesModeReceiver::handle
        );

    }

    private static void registerS2C() {

        PayloadTypeRegistry.playS2C().register(
                SendSlotBalanceS2CPayload.ID,
                SendSlotBalanceS2CPayload.CODEC
        );
        SlotBalanceReceiver.register();

        PayloadTypeRegistry.playS2C().register(
                SendMenuSettingsS2CPayload.ID,
                SendMenuSettingsS2CPayload.CODEC
        );
        MenuScreenSettingsReceiver.register();

    }

}