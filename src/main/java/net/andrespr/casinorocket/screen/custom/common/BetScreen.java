package net.andrespr.casinorocket.screen.custom.common;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.network.c2s.common.DoBetC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.custom.CasinoMachineScreen;
import net.andrespr.casinorocket.screen.widget.CommonButton;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.andrespr.casinorocket.util.TextUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class BetScreen extends CasinoMachineScreen<BetScreenHandler> {

    private CommonButton betButton;
    private long currentTotal = 0L;

    public BetScreen(BetScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 174;
        this.backgroundHeight = 166;
    }

    @Override
    @SuppressWarnings("unused")
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
            if (this.handler instanceof IMachineBoundHandler m) {
                ClientPlayNetworking.send(new DoBetC2SPayload(m.getMachineKey(), m.getMachinePos()));
            }
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

        int width = textRenderer.getWidth(formatted);
        int drawX = Math.max(105 - width, 79);

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