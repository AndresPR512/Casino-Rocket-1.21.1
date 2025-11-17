package net.andrespr.casinorocket.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.network.c2s.DoBetC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.widget.CommonButton;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class BetScreen extends HandledScreen<BetScreenHandler> {

    private CommonButton betButton;
    private long currentTotal = 0L;

    public BetScreen(BetScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 174;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        int baseX = (this.width - this.backgroundWidth) / 2;
        int baseY = (this.height - this.backgroundHeight) / 2;

        this.betButton = ModButtons.doBet(baseX, baseY, 46, 2, b -> onDoBetPressed());
        this.addDrawableChild(this.betButton);
        updateBetButtonState();
    }

    private void onDoBetPressed() {
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal("[SlotMachine] DoBet Pressed!"), false);
            net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new DoBetC2SPayload());
        }
    }

    // === BACKGROUND ===
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.BET_GUI);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) /2;

        context.drawTexture(ModGuiTextures.BET_GUI, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        String formatted = TextUtils.formatCompact(currentTotal);

        int x1 = 79;
        int x2 = 105;
        int width = textRenderer.getWidth(formatted);

        int drawX = Math.max(x2 - width, x1);

        context.drawText(textRenderer, formatted, drawX, 4, 0x00AA00, true);
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        updateBetButtonState();
    }

    // === GETTERS ===

    public void updateTotalAmount(long amount) {
        this.currentTotal = amount;
    }

    private void updateBetButtonState() {
        if (this.betButton != null) {
            this.betButton.active = currentTotal > 0;
        }
    }

}