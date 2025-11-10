package net.andrespr.casinorocket.screen;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.screen.custom.SlotMachineScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {

    public static final ScreenHandlerType<SlotMachineScreenHandler> SLOT_MACHINE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CasinoRocket.MOD_ID, "slot_machine_screen_handler"),
                    new ExtendedScreenHandlerType<>(SlotMachineScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void registerScreenHandlers() {
        CasinoRocket.LOGGER.info("Registering Screen Handlers for " + CasinoRocket.MOD_ID);
    }

}