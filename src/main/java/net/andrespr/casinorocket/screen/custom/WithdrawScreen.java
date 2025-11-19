package net.andrespr.casinorocket.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.network.c2s.DoWithdrawC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.widget.CommonButton;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class WithdrawScreen extends HandledScreen<WithdrawScreenHandler> {

    private CommonButton withdrawButton;
    private long balance = 0L;

    public WithdrawScreen(WithdrawScreenHandler handler, PlayerInventory inv, Text title) {
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

        this.withdrawButton = ModButtons.doWithdraw(baseX, baseY, 46, 2, b -> onDoWithdrawPressed());
        this.addDrawableChild(this.withdrawButton);
        updateWithdrawButtonState();
    }

    private void onDoWithdrawPressed() {
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal("[SlotMachine] DoWithdraw Pressed!"), false);
            ClientPlayNetworking.send(new DoWithdrawC2SPayload());
        }
    }

    // === BACKGROUND ===
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.WITHDRAW_GUI);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) /2;

        context.drawTexture(ModGuiTextures.WITHDRAW_GUI, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) { }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        updateWithdrawButtonState();
    }

    // === GETTERS / UPDATERS ===

    public void updateBalance(long amount) {
        this.balance = amount;
    }

    private void updateWithdrawButtonState() {
        if (this.withdrawButton != null) {
            this.withdrawButton.active = balance > 0;
        }
    }

}