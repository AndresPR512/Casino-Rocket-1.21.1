package net.andrespr.casinorocket.screen;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.screen.custom.blackjack.BlackjackTableScreenHandler;
import net.andrespr.casinorocket.screen.custom.chip_table.ChipTableScreenHandler;
import net.andrespr.casinorocket.screen.custom.common.BetScreenHandler;
import net.andrespr.casinorocket.screen.custom.common.WithdrawScreenHandler;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineMenuScreenHandler;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreenHandler;
import net.andrespr.casinorocket.screen.opening.BlackjackTableOpenData;
import net.andrespr.casinorocket.screen.opening.CommonMachineOpenData;
import net.andrespr.casinorocket.screen.opening.SlotMachineOpenData;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {

    public static final ScreenHandlerType<SlotMachineScreenHandler> SLOT_MACHINE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CasinoRocket.MOD_ID, "slot_machine_screen_handler"),
                    new ExtendedScreenHandlerType<>(SlotMachineScreenHandler::new, SlotMachineOpenData.CODEC));

    public static final ScreenHandlerType<SlotMachineMenuScreenHandler> SLOT_MACHINE_MENU_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CasinoRocket.MOD_ID, "slot_machine_menu_screen_handler"),
                    new ExtendedScreenHandlerType<>(SlotMachineMenuScreenHandler::new, SlotMachineOpenData.CODEC));

    public static final ScreenHandlerType<BetScreenHandler> BET_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CasinoRocket.MOD_ID, "bet_screen_handler"),
                    new ExtendedScreenHandlerType<>(BetScreenHandler::new, CommonMachineOpenData.CODEC));

    public static final ScreenHandlerType<WithdrawScreenHandler> WITHDRAW_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CasinoRocket.MOD_ID, "withdraw_screen_handler"),
                    new ExtendedScreenHandlerType<>(WithdrawScreenHandler::new, CommonMachineOpenData.CODEC));

    public static final ScreenHandlerType<BlackjackTableScreenHandler> BLACKJACK_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CasinoRocket.MOD_ID, "blackjack_table_screen_handler"),
                    new ExtendedScreenHandlerType<>(BlackjackTableScreenHandler::new, BlackjackTableOpenData.CODEC));

    public static final ScreenHandlerType<ChipTableScreenHandler> CHIP_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CasinoRocket.MOD_ID, "chip_table_screen_handler"),
                    new ExtendedScreenHandlerType<>(ChipTableScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void registerScreenHandlers() {
        CasinoRocket.LOGGER.info("Registering Screen Handlers for " + CasinoRocket.MOD_ID);
    }

}