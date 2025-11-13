package net.andrespr.casinorocket.screen;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.util.Identifier;

public final class ModGuiTextures {

    private ModGuiTextures() {}

    private static Identifier id(String path) {
        return Identifier.of(CasinoRocket.MOD_ID, path);
    }

    // === BACKGROUNDS ===
    public static final Identifier SLOT_MACHINE_GUI =
            id("textures/gui/slot_machine/slot_machine_gui.png");       // 230x206
    public static final Identifier BET_GUI =
            id("textures/gui/common/gui/bet_gui.png");      // 174x166
    public static final Identifier WITHDRAW_GUI =
            id("textures/gui/common/gui/withdraw_gui.png");     // 174x166

    // === BUTTONS ===
    public static final Identifier BTN_SMALL =
            id("textures/gui/common/buttons/cobblemon_small_button.png");       // 29x36 -> 29x12 (x3)
    public static final Identifier BTN_LARGE =
            id("textures/gui/common/buttons/cobblemon_large_button.png");       // 82x36 -> 82x12 (x3)
    public static final Identifier BTN_BET =
            id("textures/gui/common/buttons/bet_button.png");       // 72x72 -> 72x72 (x3)
    public static final Identifier BTN_MENU =
            id("textures/gui/common/buttons/menu_button.png");      // 54x72 -> 54x72 (x3)
    public static final Identifier BTN_WITHDRAW =
            id("textures/gui/common/buttons/withdraw_button.png");      // 72x72 -> 72x72 (x3)
    public static final Identifier BTN_SPIN =
            id("textures/gui/common/buttons/spin_button.png");      // 40x120 -> 40x40 (x3)

}