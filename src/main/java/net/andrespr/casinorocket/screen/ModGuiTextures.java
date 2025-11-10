package net.andrespr.casinorocket.screen;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.util.Identifier;

public final class ModGuiTextures {

    private ModGuiTextures() {}

    private static Identifier id(String path) {
        return Identifier.of(CasinoRocket.MOD_ID, path);
    }

    public static final Identifier SLOT_MACHINE_BG =
            id("textures/gui/slot_machine/slot_machine_gui.png");

    public static final Identifier BTN_BET =
            id("textures/gui/common/buttons/bet_button.png");          // 54x42 -> 54x14 (x3)
    public static final Identifier BTN_MENU =
            id("textures/gui/common/buttons/menu_button.png");         // 32x42 -> 32x14 (x3)
    public static final Identifier BTN_WITHDRAW =
            id("textures/gui/common/buttons/withdraw_button.png");     // 54x42 -> 54x14 (x3)
    public static final Identifier BTN_SPIN =
            id("textures/gui/common/buttons/spin_button.png");         // 30x90 -> 30x30 (x3)

}