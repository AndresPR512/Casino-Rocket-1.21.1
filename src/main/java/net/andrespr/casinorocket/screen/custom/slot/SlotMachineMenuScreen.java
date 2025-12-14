package net.andrespr.casinorocket.screen.custom.slot;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.network.c2s.ChangeBetBaseC2SPayload;
import net.andrespr.casinorocket.network.c2s.ChangeLinesModeC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.custom.CasinoMachineScreen;
import net.andrespr.casinorocket.screen.opening.MouseRestore;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.andrespr.casinorocket.screen.widget.SlotButton;
import net.andrespr.casinorocket.games.slot.SlotMachineConstants;
import net.andrespr.casinorocket.util.TextUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class SlotMachineMenuScreen extends CasinoMachineScreen<SlotMachineMenuScreenHandler> {

    private long balance = 0L;
    private int betBase = 10;
    private int linesMode = 1;

    private SlotButton plusButton;
    private SlotButton subtractButton;
    private SlotButton mode1Button;
    private SlotButton mode2Button;
    private SlotButton mode3Button;

    public SlotMachineMenuScreen(SlotMachineMenuScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 200;
        this.backgroundHeight = 236;
    }

    @Override
    @SuppressWarnings("unused")
    protected void init() {
        super.init();

        int baseX = (this.width - this.backgroundWidth) / 2;
        int baseY = (this.height - this.backgroundHeight) / 2;

        this.plusButton = ModButtons.plus(baseX, baseY, 67, 36, b -> onPlusPressed());
        this.subtractButton = ModButtons.subtract(baseX, baseY, 6, 36, b -> onSubtractPressed());
        this.mode1Button = ModButtons.mode1(baseX, baseY, 6, 67, b -> onMode1Pressed());
        this.mode2Button = ModButtons.mode2(baseX, baseY, 6, 82, b -> onMode2Pressed());
        this.mode3Button = ModButtons.mode3(baseX, baseY, 6, 97, b -> onMode3Pressed());

        this.addDrawableChild(this.plusButton);
        this.addDrawableChild(this.subtractButton);
        this.addDrawableChild(this.mode1Button);
        this.addDrawableChild(this.mode2Button);
        this.addDrawableChild(this.mode3Button);

        updateSettings(
                this.handler.getInitialBalance(),
                this.handler.getInitialBetBase(),
                this.handler.getInitialLinesMode()
        );

    }

    // === BUTTONS ===
    private void onPlusPressed() {
        if (client != null && client.player != null) {
            ClientPlayNetworking.send(new ChangeBetBaseC2SPayload(true));
        }
    }

    private void onSubtractPressed() {
        if (client != null && client.player != null) {
            ClientPlayNetworking.send(new ChangeBetBaseC2SPayload(false));
        }
    }

    private void onMode1Pressed() {
        sendMode(1);
    }

    private void onMode2Pressed() {
        sendMode(2);
    }

    private void onMode3Pressed() {
        sendMode(3);
    }

    private void sendMode(int mode) {
        if (client != null && client.player != null) {
            ClientPlayNetworking.send(new ChangeLinesModeC2SPayload(mode));
        }
    }

    // === SCREEN TICK ===
    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        MouseRestore.applyIfPending(client);
    }

    // === BACKGROUND ===
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.SLOT_MACHINE_MENU_GUI);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) /2;

        context.drawTexture(ModGuiTextures.SLOT_MACHINE_MENU_GUI, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(textRenderer, Text.translatable("gui.casinorocket.slot_machine.settings_bet_amount"),
                7, 25, 0xFFFFFF, true);
        context.drawText(textRenderer, Text.translatable("gui.casinorocket.slot_machine.settings_lines"),
                7, 56, 0xFFFFFF, true);
        context.drawText(textRenderer, Text.translatable("gui.casinorocket.slot_machine.settings_one_line"),
                23, 70, 0xFFFFFF, true);
        context.drawText(textRenderer, Text.translatable("gui.casinorocket.slot_machine.settings_three_lines"),
                23, 85, 0xFFFFFF, true);
        context.drawText(textRenderer, Text.translatable("gui.casinorocket.slot_machine.settings_five_lines"),
                23, 100, 0xFFFFFF, true);

        int finalBet = betBase * linesMode;
        String formatted = TextUtils.formatCompact(finalBet);
        context.drawText(textRenderer, formatted, 23, 38, 0x00AA00, true);

        int srcY = (linesMode - 1) * 30;
        context.drawTexture(ModGuiTextures.LINES_LAYOUT, 21,112, 0, srcY,
                43, 30, 43, 30 * 3);

    }

    // === UPDATERS ===
    public void updateSettings(long balance, int base, int lines) {
        this.balance = balance;
        this.betBase = base;
        this.linesMode = lines;

        updateButtonStates();
        updateModeButtons();
    }

    private void updateButtonStates() {
        int index = SlotMachineConstants.BET_VALUES.indexOf(betBase);

        subtractButton.active = index > 0;
        plusButton.active = index < SlotMachineConstants.BET_VALUES.size() - 1;

        subtractButton.setForcedPressed(index == 0);
        plusButton.setForcedPressed(index == SlotMachineConstants.BET_VALUES.size() - 1);
    }

    private void updateModeButtons() {
        mode1Button.setForcedPressed(linesMode == 1);
        mode2Button.setForcedPressed(linesMode == 2);
        mode3Button.setForcedPressed(linesMode == 3);

        mode1Button.active = linesMode != 1;
        mode2Button.active = linesMode != 2;
        mode3Button.active = linesMode != 3;
    }

}