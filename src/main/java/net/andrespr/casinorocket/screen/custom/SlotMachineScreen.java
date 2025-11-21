package net.andrespr.casinorocket.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.games.slot.SlotLineResult;
import net.andrespr.casinorocket.games.slot.SlotSymbol;
import net.andrespr.casinorocket.games.slot.SlotSymbolPicker;
import net.andrespr.casinorocket.network.c2s.DoSpinC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.andrespr.casinorocket.screen.widget.SlotButton;
import net.andrespr.casinorocket.util.TextUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SlotMachineScreen extends CasinoMachineScreen<SlotMachineScreenHandler> {

    private SlotButton spinButton;

    private long balance = 0L;
    private int betAmount = 10;

    private java.util.List<SlotLineResult> lastWins = java.util.Collections.emptyList();
    private int lastWinAmount = 0;

    public SlotMachineScreen(SlotMachineScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 230;
        this.backgroundHeight = 206;
    }

    @Override
    @SuppressWarnings("unused")
    protected void init() {
        super.init();
        int baseX = (this.width - this.backgroundWidth) / 2;
        int baseY = (this.height - this.backgroundHeight) / 2;

        this.addDrawableChild(ModButtons.bet(baseX, baseY, 9, 9, b -> onBetPressed()));
        this.addDrawableChild(ModButtons.withdraw(baseX, baseY, 149, 9, b -> onWithdrawPressed()));
        this.addDrawableChild(ModButtons.menu(baseX, baseY, 88, 9, b -> onMenuPressed()));

        this.spinButton = ModButtons.spin(baseX, baseY, 95, 157, b -> onSpinPressed());
        this.addDrawableChild(spinButton);
    }

    // === BUTTONS ===
    private void onSpinPressed() {
        if (client != null && client.player != null) {
            this.spinButton.active = false;
            ClientPlayNetworking.send(new DoSpinC2SPayload());
        }
    }

    // === SPIN SYMBOLS ===
    private static final int SYMBOL_SIZE = 32;
    private static final int[] COLUMN_X = { 60 , 99 , 138 };
    private static final int ROW_Y = 45;

    private boolean isSpinning = false;

    private float[] reelOffset = new float[3];
    private int[] reelTimer = new int[3];
    private boolean[] reelSpinning = new boolean[3];

    private SlotSymbol[][] tempMatrix = new SlotSymbol[3][3];
    private SlotSymbol[][] finalMatrix = null;
    private SlotSymbol[][] lastMatrix = null;

    private void startSpinAnimation() {
        isSpinning = true;
        finalMatrix = null;

        for (int col = 0; col < 3; col++) {
            reelOffset[col] = 0f;
            reelSpinning[col] = true;
            reelTimer[col] = 30 + col * 10;

            for (int row = 0; row < 3; row++) {
                tempMatrix[row][col] = SlotSymbolPicker.random();
            }

        }

    }

    public void onSpinResult(SlotSymbol[][] matrix, java.util.List<SlotLineResult> wins,
                             int totalWin, long newBalance) {
        this.finalMatrix = matrix;
        this.lastWins = wins;
        this.lastWinAmount = totalWin;
        this.updateBalance(newBalance);

        startSpinAnimation();
    }

    private void drawSymbols(DrawContext context, int originX, int originY) {
        if (tempMatrix == null) return;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                SlotSymbol symbol = tempMatrix[row][col];
                if (symbol == null) continue;

                Identifier texture = ModGuiTextures.SlotTextures.SYMBOL_TEXTURES.get(symbol);

                int drawX = originX + COLUMN_X[col];
                int drawY = originY + ROW_Y + (row * SYMBOL_SIZE) + (int) reelOffset[col];

                context.drawTexture(texture, drawX, drawY, 0, 0,
                        SYMBOL_SIZE, SYMBOL_SIZE, SYMBOL_SIZE, SYMBOL_SIZE);
            }
        }

    }

    private void drawAnimatedReels(DrawContext ctx, int originX, int originY) {

        for (int col = 0; col < 3; col++) {

            for (int row = -1; row <= 3; row++) {

                SlotSymbol temp = SlotSymbolPicker.random();
                Identifier texture = ModGuiTextures.SlotTextures.SYMBOL_TEXTURES.get(temp);

                int drawX = originX + COLUMN_X[col];
                int drawY = originY + ROW_Y + (row * SYMBOL_SIZE) + (int) reelOffset[col];

                ctx.drawTexture(texture, drawX, drawY,
                        0, 0, SYMBOL_SIZE, SYMBOL_SIZE, SYMBOL_SIZE, SYMBOL_SIZE);
            }
        }

    }

    private void finishSpin() {
        isSpinning = false;
        this.lastMatrix = finalMatrix;
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();

        if (!isSpinning) return;

        boolean anySpinning = false;

        for (int col = 0; col < 3; col++) {
            if (!reelSpinning[col]) continue;

            anySpinning = true;

            reelOffset[col] += 8.0f;

            if (reelOffset[col] >= SYMBOL_SIZE) {
                reelOffset[col] -= SYMBOL_SIZE;

                tempMatrix[2][col] = tempMatrix[1][col];
                tempMatrix[1][col] = tempMatrix[0][col];
                tempMatrix[0][col] = SlotSymbolPicker.random();
            }

            reelTimer[col]--;
            if (reelTimer[col] <= 0) {
                reelSpinning[col] = false;
                reelOffset[col] = 0f;

                if (finalMatrix != null) {
                    for (int row = 0; row < 3; row++) {
                        tempMatrix[row][col] = finalMatrix[row][col];
                    }
                }
            }
        }

        if (!anySpinning) {
            finishSpin();
        }
    }


    // === BACKGROUND ===
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.SLOT_MACHINE_GUI);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) /2;

        context.drawTexture(ModGuiTextures.REELS, x + 55, y + 39, 0, 0, 120, 112, 120, 112);

        if (isSpinning) {
            drawAnimatedReels(context, x, y);
        } else {
            drawSymbols(context, x, y);
        }

        context.drawTexture(ModGuiTextures.SLOT_MACHINE_GUI, x, y, 0, 0, backgroundWidth, backgroundHeight);

        drawSymbols(context, x, y);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        drawBalance(context);
        drawBetAmount(context);
    }

    // === UPDATERS ===

    public void updateBalance(long amount) {
        this.balance = amount;
    }

    public void updateDisplay(long balance, int betBase, int linesMode) {
        this.balance = balance;
        this.betAmount = betBase * linesMode;
    }

    // === HELPERS ===

    private void drawBalance(DrawContext context) {
        String formatted = TextUtils.formatCompact(balance);

        float targetHeight = 15f;
        float baseScale = targetHeight / 8f;
        float maxWidth = 49f;

        float textWidth = textRenderer.getWidth(formatted);
        float scaledWidth = textWidth * baseScale;

        float scale = scaledWidth > maxWidth
                ? maxWidth / textWidth
                : baseScale;

        int x1 = 14;
        int x2 = 63;
        int y = 177;

        int finalWidth = (int)(textWidth * scale);
        int drawX = Math.max(x2 - finalWidth, x1);
        drawX += 1;

        context.getMatrices().push();
        context.getMatrices().translate(drawX, y, 0);
        context.getMatrices().scale(scale, scale, 1);
        context.drawText(textRenderer, formatted, 0, 0, 0x20BB20, false);
        context.getMatrices().pop();
    }

    private void drawBetAmount(DrawContext context) {
        String formatted = TextUtils.formatCompact(betAmount);

        float targetHeight = 15f;
        float baseScale = targetHeight / 8f;
        float maxWidth = 49f;

        float textWidth = textRenderer.getWidth(formatted);
        float scaledWidth = textWidth * baseScale;

        float scale = scaledWidth > maxWidth
                ? maxWidth / textWidth
                : baseScale;

        int x1 = 154;
        int x2 = 203;
        int y = 177;

        int finalWidth = (int)(textWidth * scale);
        int drawX = Math.max(x2 - finalWidth, x1);
        drawX += 1;

        context.getMatrices().push();
        context.getMatrices().translate(drawX, y, 0);
        context.getMatrices().scale(scale, scale, 1);
        context.drawText(textRenderer, formatted, 0, 0, 0x20BB20, false);
        context.getMatrices().pop();
    }

}