package net.andrespr.casinorocket.screen.custom.chip_table;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ChipTableScreen extends HandledScreen<ChipTableScreenHandler> {

    public ChipTableScreen(ChipTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 174;
        this.backgroundHeight = 171;
    }

    @Override
    protected void init() {
        super.init();
        titleX = 7;
        titleY = 3;
        playerInventoryTitleX = 7;
        playerInventoryTitleY = 79;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.CHIP_TABLE_GUI);

        context.drawTexture(ModGuiTextures.CHIP_TABLE_GUI, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 0xFFFFFF, false);
        context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 0xFFFFFF, false);
    }

}