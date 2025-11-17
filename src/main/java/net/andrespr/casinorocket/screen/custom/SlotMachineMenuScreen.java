package net.andrespr.casinorocket.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class SlotMachineMenuScreen extends HandledScreen<SlotMachineMenuScreenHandler> {

    public SlotMachineMenuScreen(SlotMachineMenuScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 200;
        this.backgroundHeight = 236;
    }

    @Override
    protected void init() {
        super.init();
        int baseX = (this.width - this.backgroundWidth) / 2;
        int baseY = (this.height - this.backgroundHeight) / 2;
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

    }

}