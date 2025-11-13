package net.andrespr.casinorocket.screen.widget;

import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.minecraft.text.Text;

public final class ModButtons {

    private ModButtons() {}

    public static CommonButton doBet(int baseX, int baseY, int x, int y, CommonButton.PressAction onPress) {
        return new CommonButton(baseX + x, baseY + y, 29, 12, ModGuiTextures.BTN_SMALL, onPress, Text.translatable("button.casinorocket.bet"));
    }

    public static CommonButton doWithdraw(int baseX, int baseY, int x, int y, CommonButton.PressAction onPress) {
        return new CommonButton(baseX + x, baseY + y, 82, 12, ModGuiTextures.BTN_LARGE, onPress, Text.translatable("button.casinorocket.withdraw"));
    }

    public static SlotButton bet(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 72, 24, ModGuiTextures.BTN_BET, onPress, Text.empty());
    }

    public static SlotButton withdraw(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 72, 24, ModGuiTextures.BTN_WITHDRAW, onPress, Text.empty());
    }

    public static SlotButton menu(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 54, 24, ModGuiTextures.BTN_MENU, onPress, Text.empty());
    }

    public static SlotButton spin(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 40, 40, ModGuiTextures.BTN_SPIN, onPress, Text.empty());
    }

}