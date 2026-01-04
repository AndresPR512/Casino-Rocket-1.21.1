package net.andrespr.casinorocket.network;

import net.andrespr.casinorocket.network.c2s.common.*;
import net.andrespr.casinorocket.network.c2s.slots.*;
import net.andrespr.casinorocket.network.c2s.blackjack.*;
import net.andrespr.casinorocket.network.c2s_handlers.common.*;
import net.andrespr.casinorocket.network.c2s_handlers.slots.*;
import net.andrespr.casinorocket.network.c2s_handlers.blackjack.*;
import net.andrespr.casinorocket.network.s2c.*;
import net.andrespr.casinorocket.network.s2c_handlers.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class CasinoRocketPackets {

    // =============================
    // SERVER: register C2S + S2C TYPES
    // =============================
    public static void registerServer() {

        registerC2S();

        // Register ONLY TYPES (CODEC) — NO handlers here
        PayloadTypeRegistry.playS2C().register(SendMachineBalanceS2CPayload.ID, SendMachineBalanceS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendMenuSettingsS2CPayload.ID, SendMenuSettingsS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendSpinResultS2CPayload.ID, SendSpinResultS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SendBlackjackStateS2CPayload.ID, SendBlackjackStateS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SlotConfigSyncS2CPayload.ID, SlotConfigSyncS2CPayload.CODEC);

    }

    // =============================
    // CLIENT: register S2C HANDLERS ONLY
    // =============================
    public static void registerClient() {

        MachineBalanceReceiver.register();
        MenuScreenSettingsReceiver.register();
        SpinResultReceiver.register();
        BlackjackStateReceiver.register();
        SlotConfigSyncReceiver.register();

    }

    // =============================
    // REGISTER C2S PACKETS (SERVER ONLY)
    // =============================
    private static void registerC2S() {

        // ===== COMMON =====

        // BetScreen
        PayloadTypeRegistry.playC2S().register(OpenBetScreenC2SPayload.ID, OpenBetScreenC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(OpenBetScreenC2SPayload.ID, BetScreenReceiver::openBetScreen);

        // DoBet
        PayloadTypeRegistry.playC2S().register(DoBetC2SPayload.ID, DoBetC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(DoBetC2SPayload.ID, DoBetReceiver::handle);

        // WithdrawScreen
        PayloadTypeRegistry.playC2S().register(OpenWithdrawScreenC2SPayload.ID, OpenWithdrawScreenC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(OpenWithdrawScreenC2SPayload.ID, WithdrawScreenReceiver::openWithdrawScreen);

        // DoWithdraw
        PayloadTypeRegistry.playC2S().register(DoWithdrawC2SPayload.ID, DoWithdrawC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(DoWithdrawC2SPayload.ID, DoWithdrawReceiver::handle);

        // MenuScreen
        PayloadTypeRegistry.playC2S().register(OpenMenuScreenC2SPayload.ID, OpenMenuScreenC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(OpenMenuScreenC2SPayload.ID, MenuScreenReceiver::openMenuScreen);

        // ReturnToMachine
        PayloadTypeRegistry.playC2S().register(ReturnToMachineScreenC2SPayload.ID, ReturnToMachineScreenC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ReturnToMachineScreenC2SPayload.ID, ReturnToMachineScreenReceiver::handle);

        // ===== SLOTS =====

        // DoSpin
        PayloadTypeRegistry.playC2S().register(DoSpinC2SPayload.ID, DoSpinC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(DoSpinC2SPayload.ID, DoSpinReceiver::handle);

        // ChangeBetBase
        PayloadTypeRegistry.playC2S().register(ChangeBetBaseC2SPayload.ID, ChangeBetBaseC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ChangeBetBaseC2SPayload.ID, ChangeBetBaseReceiver::handle);

        // ChangeLinesMode
        PayloadTypeRegistry.playC2S().register(ChangeLinesModeC2SPayload.ID, ChangeLinesModeC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ChangeLinesModeC2SPayload.ID, ChangeLinesModeReceiver::handle);

        // ===== BLACKJACK =====

        // BlackjackAction
        PayloadTypeRegistry.playC2S().register(BlackjackActionC2SPayload.ID, BlackjackActionC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(BlackjackActionC2SPayload.ID, BlackjackActionReceiver::handle);

        // ChangeBetIndex
        PayloadTypeRegistry.playC2S().register(ChangeBlackjackBetIndexC2SPayload.ID, ChangeBlackjackBetIndexC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ChangeBlackjackBetIndexC2SPayload.ID, ChangeBlackjackBetIndexReceiver::handle);

    }

}