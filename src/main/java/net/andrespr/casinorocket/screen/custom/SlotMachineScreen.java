package net.andrespr.casinorocket.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.games.slot.SlotLineResult;
import net.andrespr.casinorocket.games.slot.SlotReels;
import net.andrespr.casinorocket.games.slot.SlotSymbol;
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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SlotMachineScreen extends CasinoMachineScreen<SlotMachineScreenHandler> {

    private SlotButton spinButton;

    private long balance = 0L;
    private long pendingBalance = -1L;
    private int betAmount = 10;

    private List<SlotLineResult> lastWins = List.of();
    private int lastWinAmount = 0;

    // --- REEL / LAYOUT CONSTANTS ---

    private static final int SYMBOL_SIZE = 32;
    private static final int[] COLUMN_X = { 60 , 99 , 138 };
    // Y de la fila superior visible
    private static final int ROW_Y = 45;
    // Dibujamos 5 filas virtuales: [oculta arriba, visible top, visible mid, visible bottom, oculta abajo]
    private static final int VIRTUAL_ROWS = 5;

    // --- ANIMACIÓN / STRIPS ---

    private boolean isSpinning = false;

    // Índice en el strip de la fila 0 (la oculta superior) por carrete
    private final int[] reelIndex = new int[3];
    // Índice objetivo de la fila 0 cuando el carrete termine de girar
    private final int[] targetTopIndex = new int[3];

    // Desplazamiento vertical (en píxeles) de cada carrete
    private final float[] reelOffset = new float[3];
    // Tiempo mínimo que debe seguir girando cada carrete
    private final int[] reelTimer = new int[3];
    // Si el carrete sigue girando o ya está detenido
    private final boolean[] reelSpinning = new boolean[3];

    public SlotMachineScreen(SlotMachineScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 230;
        this.backgroundHeight = 206;
    }

    // === INIT ===

    @Override
    @SuppressWarnings("unused")
    protected void init() {
        super.init();

        initRandomReels();

        int baseX = (this.width - this.backgroundWidth) / 2;
        int baseY = (this.height - this.backgroundHeight) / 2;

        this.addDrawableChild(ModButtons.bet(baseX, baseY, 9, 9, b -> onBetPressed()));
        this.addDrawableChild(ModButtons.withdraw(baseX, baseY, 149, 9, b -> onWithdrawPressed()));
        this.addDrawableChild(ModButtons.menu(baseX, baseY, 88, 9, b -> onMenuPressed()));

        this.spinButton = ModButtons.spin(baseX, baseY, 95, 157, b -> onSpinPressed());
        this.addDrawableChild(spinButton);
    }

    private void initRandomReels() {
        // Estado inicial: cada carrete en una posición aleatoria del strip
        for (int col = 0; col < 3; col++) {
            int len = SlotReels.STRIPS[col].length;
            reelIndex[col] = ThreadLocalRandom.current().nextInt(len);
            reelOffset[col] = 0f;
            reelSpinning[col] = false;
            reelTimer[col] = 0;
            targetTopIndex[col] = reelIndex[col];
        }
    }

    // === BOTONES ===

    private void onSpinPressed() {
        if (client != null && client.player != null && !isSpinning) {
            ClientPlayNetworking.send(new DoSpinC2SPayload());
        }
    }

    // === SPIN / RESULTADO DEL SERVIDOR ===

    /**
     * Este método lo llama SpinResultReceiver cuando llega el S2C.
     * matrix es la matriz 3x3 final (evaluada en el servidor).
     */
    public void onSpinResult(SlotSymbol[][] matrix, List<SlotLineResult> wins,
                             int totalWin, long newBalance) {

        this.lastWins = wins;
        this.lastWinAmount = totalWin;
        this.pendingBalance = newBalance;

        // Calculamos para cada carrete qué índice del strip debe quedar
        // como fila 0 (oculta superior) para que:
        // fila1 -> matrix[0][col], fila2 -> matrix[1][col], fila3 -> matrix[2][col]
        for (int col = 0; col < 3; col++) {
            SlotSymbol top    = matrix[0][col];
            SlotSymbol middle = matrix[1][col];
            SlotSymbol bottom = matrix[2][col];

            SlotSymbol[] strip = SlotReels.STRIPS[col];
            int len = strip.length;

            int found = reelIndex[col]; // fallback
            for (int i = 0; i < len; i++) {
                SlotSymbol a = strip[(i + 1) % len]; // fila visible superior
                SlotSymbol b = strip[(i + 2) % len]; // fila visible central
                SlotSymbol c = strip[(i + 3) % len]; // fila visible inferior

                if (a == top && b == middle && c == bottom) {
                    found = i;
                    break;
                }
            }

            targetTopIndex[col] = found;
        }

        startSpinAnimation();
    }

    private void startSpinAnimation() {
        isSpinning = true;
        if (this.spinButton != null) this.spinButton.active = false;

        // tiempos exactos de giro
        reelTimer[0] = 30;  // izquierda
        reelTimer[1] = 40;  // centro
        reelTimer[2] = 50;  // derecha

        for (int col = 0; col < 3; col++) {
            reelOffset[col] = 0f;
            reelSpinning[col] = true;
        }
    }

    private void finishSpin() {
        isSpinning = false;

        if (pendingBalance >= 0) {
            this.balance = pendingBalance;
            this.pendingBalance = -1L;
        }

        if (this.spinButton != null) {
            this.spinButton.active = true;
        }
    }

    // === TICK DE LA PANTALLA: ANIMACIÓN ===

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();

        if (!isSpinning) return;

        boolean anySpinning = false;

        for (int col = 0; col < 3; col++) {

            if (!reelSpinning[col]) continue;

            anySpinning = true;

            int len = SlotReels.STRIPS[col].length;

            // velocidad
            reelOffset[col] += 20.0f;

            if (reelOffset[col] >= SYMBOL_SIZE) {
                reelOffset[col] -= SYMBOL_SIZE;
                reelIndex[col] = (reelIndex[col] + 1) % len;
            }

            // cuenta regresiva del carrete
            reelTimer[col]--;

            if (reelTimer[col] <= 0) {
                // TIEMPO CUMPLIDO → DETIENE EL CARRETE EXACTO

                // alineamos EXACTO en targetTopIndex
                reelIndex[col] = targetTopIndex[col];
                reelOffset[col] = 0f;

                reelSpinning[col] = false;
            }
        }

        if (!anySpinning) {
            finishSpin();
        }
    }

    // === DIBUJO ===

    private void drawSymbols(DrawContext ctx, int originX, int originY) {
        for (int col = 0; col < 3; col++) {
            SlotSymbol[] strip = SlotReels.STRIPS[col];
            int len = strip.length;

            for (int row = 0; row < VIRTUAL_ROWS; row++) {

                int stripIndex = (reelIndex[col] + row) % len;
                SlotSymbol symbol = strip[stripIndex];
                if (symbol == null) continue;

                Identifier texture = ModGuiTextures.SlotTextures.SYMBOL_TEXTURES.get(symbol);

                int drawX = originX + COLUMN_X[col];
                int drawY = originY + ROW_Y + ((row - 1) * SYMBOL_SIZE) + (int) reelOffset[col];

                ctx.drawTexture(texture, drawX, drawY,
                        0, 0, SYMBOL_SIZE, SYMBOL_SIZE, SYMBOL_SIZE, SYMBOL_SIZE);
            }
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.SLOT_MACHINE_GUI);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        // Reels
        context.drawTexture(ModGuiTextures.REELS, x + 55, y + 39, 0, 0, 120, 112, 120, 112);

        // Símbolos (debajo del marco principal)
        drawSymbols(context, x, y);

        // Marco principal encima
        context.drawTexture(ModGuiTextures.SLOT_MACHINE_GUI, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        drawBalance(context);
        drawBetAmount(context);
        // Aquí en un futuro puedes dibujar lastWinAmount, líneas ganadoras, etc.
    }

    // === UPDATERS ===

    public void updateBalance(long amount) {
        this.balance = amount;
    }

    public void updateDisplay(long balance, int betBase, int linesMode) {
        this.balance = balance;
        this.betAmount = betBase * linesMode;
    }

    // === HELPERS: TEXTO ===

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

    // GETTER opcional si algún día lo quieres usar en el server o en C2S extra
    public boolean isSpinning() {
        return this.isSpinning;
    }

}