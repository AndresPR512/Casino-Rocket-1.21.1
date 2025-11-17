package net.andrespr.casinorocket.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class SlotMachineScreen extends CasinoMachineScreen<SlotMachineScreenHandler> {

    private long balance = 0L;

    public SlotMachineScreen(SlotMachineScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 230;
        this.backgroundHeight = 206;
    }

    @Override
    protected void init() {
        super.init();
        int baseX = (this.width - this.backgroundWidth) / 2;
        int baseY = (this.height - this.backgroundHeight) / 2;

        this.addDrawableChild(ModButtons.bet(baseX, baseY, 9, 9, b -> onBetPressed()));
        this.addDrawableChild(ModButtons.withdraw(baseX, baseY, 149, 9, b -> onWithdrawPressed()));
        this.addDrawableChild(ModButtons.menu(baseX, baseY, 88, 9, b -> onMenuPressed()));
        this.addDrawableChild(ModButtons.spin(baseX, baseY, 95, 157, b -> onSpinPressed()));
    }

    // === BUTTONS ===
    private void onSpinPressed() {
        if (client != null && client.player != null)
            client.player.sendMessage(Text.literal("[SlotMachine] SPIN!"), false);
    }

    // === BACKGROUND ===
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.SLOT_MACHINE_GUI);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) /2;

        context.drawTexture(ModGuiTextures.SLOT_MACHINE_GUI, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) {
        String formatted = TextUtils.formatCompact(balance);

        float targetHeight = 15f;
        float baseScale = targetHeight / 8f;
        float maxWidth = 49f;

        float textWidth = textRenderer.getWidth(formatted);
        float scaledWidth = textWidth * baseScale;

        float scale = baseScale;
        if (scaledWidth > maxWidth) {
            scale = maxWidth / textWidth;
        }

        int x1 = 14;
        int x2 = 63;
        int y = 177;

        int finalWidth = (int)(textWidth * scale);

        int drawX = Math.max(x2 - finalWidth, x1);
        drawX += 1;

        ctx.getMatrices().push();
        ctx.getMatrices().translate(drawX, y, 0);
        ctx.getMatrices().scale(scale, scale, 1);
        ctx.drawText(textRenderer, formatted, 0, 0, 0x20BB20, false);

        ctx.getMatrices().pop();
    }

    // === GETTERS ===

    public void updateBalance(long amount) {
        this.balance = amount;
    }

}