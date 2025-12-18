package net.andrespr.casinorocket.screen.widget;

import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.minecraft.text.Text;

public final class ModButtons {

    private ModButtons() {}

    // BET MENU
    public static CommonButton doBet(int baseX, int baseY, int x, int y, CommonButton.PressAction onPress) {
        return new CommonButton(baseX + x, baseY + y, 29, 12, ModGuiTextures.BTN_SMALL, onPress, Text.translatable("button.casinorocket.bet"));
    }

    // WITHDRAW MENU
    public static CommonButton doWithdraw(int baseX, int baseY, int x, int y, CommonButton.PressAction onPress) {
        return new CommonButton(baseX + x, baseY + y, 82, 12, ModGuiTextures.BTN_LARGE, onPress, Text.translatable("button.casinorocket.withdraw"));
    }

    // COMMON CASINO MACHINE
    public static SlotButton bet(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 72, 24, ModGuiTextures.BTN_BET, onPress, Text.empty());
    }

    public static SlotButton withdraw(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 72, 24, ModGuiTextures.BTN_WITHDRAW, onPress, Text.empty());
    }

    public static SlotButton menu(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 54, 24, ModGuiTextures.BTN_MENU, onPress, Text.empty());
    }

    // SLOT MACHINE
    public static SlotButton spin(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 40, 40, ModGuiTextures.BTN_SPIN, onPress, Text.empty());
    }

    // SLOT MACHINE MENU
    public static SlotButton plus(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 12, 12, ModGuiTextures.BTN_ADD, onPress, Text.empty());
    }

    public static SlotButton subtract(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 12, 12, ModGuiTextures.BTN_SUBTRACT, onPress, Text.empty());
    }

    public static SlotButton mode1(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 14, 14, ModGuiTextures.BTN_MODE1, onPress, Text.empty());
    }

    public static SlotButton mode2(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 14, 14, ModGuiTextures.BTN_MODE2, onPress, Text.empty());
    }

    public static SlotButton mode3(int baseX, int baseY, int x, int y, SlotButton.PressAction onPress) {
        return new SlotButton(baseX + x, baseY + y, 14, 14, ModGuiTextures.BTN_MODE3, onPress, Text.empty());
    }

    // BLACKJACK
    public static CommonButton play(int baseX, int baseY, int x, int y, CommonButton.PressAction onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Text.translatable("button.casinorocket.play"));
    }

    public static CommonButton hit(int baseX, int baseY, int x, int y, CommonButton.PressAction onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Text.translatable("button.casinorocket.hit"));
    }

    public static CommonButton stand(int baseX, int baseY, int x, int y, CommonButton.PressAction onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Text.translatable("button.casinorocket.stand"));
    }

    public static CommonButton doubleDown(int baseX, int baseY, int x, int y, CommonButton.PressAction onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Text.translatable("button.casinorocket.double_down"));
    }

    public static CommonButton finish(int baseX, int baseY, int x, int y, CommonButton.PressAction onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Text.translatable("button.casinorocket.finish"));
    }

    public static CommonButton doubleOrNothing(int baseX, int baseY, int x, int y, CommonButton.PressAction onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Text.translatable("button.casinorocket.double_or_nothing"));
    }

}