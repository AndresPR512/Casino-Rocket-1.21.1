package net.andrespr.casinorocket.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.games.slot.SlotLineResult;
import net.andrespr.casinorocket.games.slot.SlotReels;
import net.andrespr.casinorocket.games.slot.SlotSymbol;
import net.andrespr.casinorocket.network.c2s.DoSpinC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.layout.DancingClefairy;
import net.andrespr.casinorocket.screen.layout.SlotLineSprite;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.andrespr.casinorocket.screen.widget.SlotButton;
import net.andrespr.casinorocket.sound.ModSounds;
import net.andrespr.casinorocket.util.TextUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.EnumSet;
import java.util.Set;

public class SlotMachineScreen extends CasinoMachineScreen<SlotMachineScreenHandler> {

    private SlotButton spinButton;

    private long balance = 0L;
    private long pendingBalance = -1L;
    private int betAmount = 10;

    private List<SlotLineResult> lastWins = List.of();
    private int lastWinAmount = 0;
    private int pendingWinAmount = -1;
    private int linesMode = 1;

    // --- LINES ---
    private SlotLineSprite lineOneSprite;
    private SlotLineSprite lineTwoSprite;
    private SlotLineSprite lineThreeTopSprite;
    private SlotLineSprite lineThreeBottomSprite;

    private enum WinLine {
        CENTER,
        TOP,
        BOTTOM,
        DIAG_TOP,
        DIAG_BOTTOM
    }

    private final Set<WinLine> flashingLines = EnumSet.noneOf(WinLine.class);

    private final Set<WinLine> pendingFlashingLines = EnumSet.noneOf(WinLine.class);
    private int pendingFlashTicks = 0;
    private boolean flashVisible = true;
    private int flashTicksRemaining = 0;
    private int flashTickCounter = 0;

    // --- REEL / LAYOUT CONSTANTS --
    private static final int SYMBOL_SIZE = 32;
    private static final int[] COLUMN_X = { 55 , 94 , 133 };
    private static final int ROW_Y = 53;
    private static final int VIRTUAL_ROWS = 5;

    // --- ANIMATION / STRIPS ---
    private boolean isSpinning = false;

    private final int[] reelIndex = new int[3];
    private final int[] targetTopIndex = new int[3];

    private final float[] reelOffset = new float[3];
    private final int[] reelTimer = new int[3];
    private final boolean[] reelSpinning = new boolean[3];
    private static final float reelVelocity = 24.0f;

    // --- CLEFAIRY ---
    private DancingClefairy clefairy;
    private boolean hasSpunOnce = false;

    public SlotMachineScreen(SlotMachineScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 220;
        this.backgroundHeight = 205;
    }

    // === INIT ===
    @Override
    @SuppressWarnings("unused")
    protected void init() {
        super.init();

        initRandomReels();

        int baseX = (this.width - this.backgroundWidth) / 2;
        int baseY = (this.height - this.backgroundHeight) / 2;

        this.addDrawableChild(ModButtons.bet(baseX, baseY, 4, 4, b -> onBetPressed()));
        this.addDrawableChild(ModButtons.menu(baseX, baseY, 83, 4, b -> onMenuPressed()));
        this.addDrawableChild(ModButtons.withdraw(baseX, baseY, 144, 4, b -> onWithdrawPressed()));

        this.spinButton = ModButtons.spin(baseX, baseY, 90, 161, b -> onSpinPressed());
        this.addDrawableChild(spinButton);

        lineOneSprite = new SlotLineSprite(ModGuiTextures.SLOT_LINE_ONE, 160, 14);
        lineTwoSprite = new SlotLineSprite(ModGuiTextures.SLOT_LINE_TWO, 160, 14);
        lineThreeTopSprite = new SlotLineSprite(ModGuiTextures.SLOT_LINE_THREE_TOP, 160, 112);
        lineThreeBottomSprite = new SlotLineSprite(ModGuiTextures.SLOT_LINE_THREE_BOTTOM, 160, 112);

        clefairy = new DancingClefairy(ModGuiTextures.DANCING_CLEFAIRY, 22, 23);
    }

    private void initRandomReels() {
        for (int col = 0; col < 3; col++) {
            int len = SlotReels.STRIPS[col].length;
            reelIndex[col] = ThreadLocalRandom.current().nextInt(len);
            reelOffset[col] = 0f;
            reelSpinning[col] = false;
            reelTimer[col] = 0;
            targetTopIndex[col] = reelIndex[col];
        }
    }

    // === BUTTONS ===
    private void onSpinPressed() {
        if (client != null && client.player != null && !isSpinning) {
            ClientPlayNetworking.send(new DoSpinC2SPayload());
        }
    }

    // === SERVER RESULT ===
    public void onSpinResult(SlotSymbol[][] matrix, List<SlotLineResult> wins,
                             int totalWin, long newBalance) {

        this.lastWins = wins;
        this.pendingWinAmount = totalWin;
        this.pendingBalance = newBalance;

        pendingFlashingLines.clear();
        pendingFlashTicks = 0;

        if (!wins.isEmpty()) {
            int maxFlash = 0;

            for (SlotLineResult win : wins) {
                WinLine lineId = mapWinToLine(win);
                pendingFlashingLines.add(lineId);

                int lineFlash = getFlashDurationForLine(matrix, win);
                if (lineFlash > maxFlash) {
                    maxFlash = lineFlash;
                }
            }

            pendingFlashTicks = maxFlash;
        }

        for (int col = 0; col < 3; col++) {
            SlotSymbol top    = matrix[0][col];
            SlotSymbol middle = matrix[1][col];
            SlotSymbol bottom = matrix[2][col];

            SlotSymbol[] strip = SlotReels.STRIPS[col];
            int len = strip.length;

            int found = reelIndex[col];
            for (int i = 0; i < len; i++) {
                SlotSymbol a = strip[(i + 1) % len];
                SlotSymbol b = strip[(i + 2) % len];
                SlotSymbol c = strip[(i + 3) % len];

                if (a == top && b == middle && c == bottom) {
                    found = i;
                    break;
                }
            }

            targetTopIndex[col] = found;
        }

        startSpinAnimation();
    }

    // === SPIN ANIMATION ===
    private void startSpinAnimation() {

        isSpinning = true;
        hasSpunOnce = true;
        updateSpinButtonState();

        if (client != null && client.player != null) {
            client.player.playSound(ModSounds.REELS_SPINNING, 1.0f, 1.0f);
        }

        if (this.spinButton != null) this.spinButton.active = false;

        // Minecraft ticks of spinning
        reelTimer[0] = 40;  // Left Reel
        reelTimer[1] = 46;  // Middle Reel
        reelTimer[2] = 54;  // Right Reel

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

        if (pendingWinAmount >= 0) {
            this.lastWinAmount = pendingWinAmount;
            this.pendingWinAmount = -1;
        }

        if (this.spinButton != null) {
            this.spinButton.active = true;
        }

        flashingLines.clear();
        if (pendingFlashTicks > 0 && !pendingFlashingLines.isEmpty()) {
            flashingLines.addAll(pendingFlashingLines);
            flashTicksRemaining = pendingFlashTicks;
            flashVisible = true;
            flashTickCounter = 0;
            playWinSound(lastWins);
        }

        updateSpinButtonState();

    }

    // === EVERY TICK ===
    @Override
    public void handledScreenTick() {
        super.handledScreenTick();

        if (isSpinning) {
            boolean anySpinning = false;

            for (int col = 0; col < 3; col++) {
                if (!reelSpinning[col]) continue;

                anySpinning = true;

                int len = SlotReels.STRIPS[col].length;

                reelOffset[col] += reelVelocity;

                if (reelOffset[col] >= SYMBOL_SIZE) {
                    reelOffset[col] -= SYMBOL_SIZE;
                    reelIndex[col] = (reelIndex[col] + 1) % len;
                }

                reelTimer[col]--;

                if (reelTimer[col] <= 0) {
                    reelIndex[col] = targetTopIndex[col];
                    reelOffset[col] = 0f;
                    reelSpinning[col] = false;
                }
            }

            if (!anySpinning) {
                finishSpin();
            }
        } else {
            if (flashTicksRemaining > 0 && !flashingLines.isEmpty()) {
                flashTicksRemaining--;
                flashTickCounter++;

                if (flashTickCounter >= 2) {
                    flashTickCounter = 0;
                    flashVisible = !flashVisible;
                }

                if (flashTicksRemaining <= 0) {
                    flashingLines.clear();
                    flashVisible = true;
                }
            }
        }

        updateSpinButtonState();

        if (clefairy != null) {
            clefairy.tick(getClefairyPhase());
        }

    }

    // === DRAW ===
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

    private void drawLines(DrawContext context, int originX, int originY) {
        if (lineOneSprite == null) return;

        int x = originX + 30;

        int yDiag = originY + 47;
        int yTop = originY + 70;
        int yMid = originY + 96;
        int yBottom = originY + 122;

        boolean centerOnByMode = (linesMode >= 1);
        boolean horizontalsOnByMode = (linesMode >= 2);
        boolean diagonalsOnByMode = (linesMode >= 3);

        boolean centerWinning = flashingLines.contains(WinLine.CENTER);
        boolean topWinning = flashingLines.contains(WinLine.TOP);
        boolean bottomWinning = flashingLines.contains(WinLine.BOTTOM);
        boolean diagTopWinning = flashingLines.contains(WinLine.DIAG_TOP);
        boolean diagBottomWinning = flashingLines.contains(WinLine.DIAG_BOTTOM);

        boolean centerEnabled = centerOnByMode && (!centerWinning || flashVisible);
        boolean topEnabled = horizontalsOnByMode && (!topWinning || flashVisible);
        boolean bottomEnabled = horizontalsOnByMode && (!bottomWinning || flashVisible);
        boolean diagTopEnabled = diagonalsOnByMode && (!diagTopWinning || flashVisible);
        boolean diagBottomEnabled = diagonalsOnByMode && (!diagBottomWinning || flashVisible);

        lineThreeTopSprite.render(context, x, yDiag, diagTopEnabled);
        lineTwoSprite.render(context, x, yTop, topEnabled);
        lineOneSprite.render(context, x, yMid, centerEnabled);
        lineTwoSprite.render(context, x, yBottom, bottomEnabled);
        lineThreeBottomSprite.render(context, x, yDiag, diagBottomEnabled);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.SLOT_MACHINE_GUI);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(ModGuiTextures.REELS, x + 50, y + 47, 0, 0, 120, 112, 120, 112);
        drawSymbols(context, x, y);
        context.drawTexture(ModGuiTextures.SLOT_MACHINE_GUI, x, y, 0, 0, backgroundWidth, backgroundHeight);
        drawLines(context, x, y);

        if (clefairy != null) {
            clefairy.render(context, x + 7,   y + 131);
            clefairy.render(context, x + 191, y + 131);
        }

    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        drawBalanceAmount(context);
        drawBetAmount(context);
        drawWinAmount(context);
    }

    // === UPDATERS ===
    public void updateBalance(long amount) {
        this.balance = amount;
    }

    public void updateDisplay(long balance, int betBase, int linesMode) {
        this.balance = balance;
        this.linesMode = linesMode;
        this.betAmount = betBase * linesMode;
    }

    private void updateSpinButtonState() {
        if (spinButton == null) return;

        boolean spinning = isSpinning;
        boolean flashing = flashTicksRemaining > 0;

        boolean busy = spinning || flashing;

        spinButton.active = !busy;
        spinButton.setForcedPressed(busy);
    }

    // === HELPERS: LINES ===
    private WinLine mapWinToLine(SlotLineResult win) {
        int idx = win.lineIndex();

        if (linesMode == 1) {
            return WinLine.CENTER;
        }

        if (linesMode == 2) {
            return switch (idx) {
                case 0 -> WinLine.TOP;
                case 1 -> WinLine.CENTER;
                case 2 -> WinLine.BOTTOM;
                default -> WinLine.CENTER;
            };
        }

        return switch (idx) {
            case 0 -> WinLine.TOP;
            case 1 -> WinLine.CENTER;
            case 2 -> WinLine.BOTTOM;
            case 3 -> WinLine.DIAG_TOP;
            case 4 -> WinLine.DIAG_BOTTOM;
            default -> WinLine.CENTER;
        };

    }

    private int getFlashDurationForLine(SlotSymbol[][] matrix, SlotLineResult win) {
        SlotSymbol symbol = win.symbol();

        int seconds;
        if (symbol == null) {
            seconds = 2;
        } else {
            switch (symbol) {
                case PIKACHU -> seconds = 3;
                case MEW     -> seconds = 4;
                case ROCKET  -> seconds = 6;
                case SEVEN   -> seconds = 8;
                default      -> seconds = 2;
            }
        }

        return seconds * 20;
    }

    // === HELPER: SOUND ===
    private void playWinSound(List<SlotLineResult> wins) {
        if (client == null || client.player == null) return;
        if (wins.isEmpty()) return;

        SlotSymbol best = null;

        for (SlotLineResult win : wins) {
            SlotSymbol s = win.symbol();
            if (s == null) continue;

            if (best == null || s.getTripleMultiplier() > best.getTripleMultiplier()) {
                best = s;
            }
        }

        if (best == null) return;

        switch (best) {
            case SEVEN -> client.player.playSound(ModSounds.JACKPOT, 1f, 1f);
            case ROCKET -> client.player.playSound(ModSounds.LEGENDARY_PRIZE, 1f, 1f);
            case MEW -> client.player.playSound(ModSounds.ULTRARARE_PRIZE, 1f, 1f);
            case PIKACHU -> client.player.playSound(ModSounds.RARE_PRIZE, 1f, 1f);
            case BULBASAUR, SQUIRTLE, CHARMANDER ->
                    client.player.playSound(ModSounds.COMMON_PRIZE, 1f, 1f);
            case CHERRY -> client.player.playSound(ModSounds.BONUS_PRIZE, 1f, 1f);
            default -> {} // No sound
        }
    }

    // === HELPER: CLEFAIRY ===
    private DancingClefairy.Phase getClefairyPhase() {
        if (isSpinning) return DancingClefairy.Phase.NEUTRAL;
        if (!hasSpunOnce) return DancingClefairy.Phase.NEUTRAL;
        if (lastWinAmount > 0) {
            return DancingClefairy.Phase.WIN;
        } else {
            return DancingClefairy.Phase.LOSS;
        }
    }


    // === HELPERS: TEXT ===
    private void drawBetAmount(DrawContext context) {
        String formatted = TextUtils.formatCompact(betAmount);

        int width = textRenderer.getWidth(formatted);
        int drawX = Math.max(146 - width, 120);

        context.drawText(textRenderer, formatted, drawX, 36, 0x00AA00, true);
        context.drawText(textRenderer, "Bet Amount", 60, 36, 0xFFFFFF, true);
    }

    private void drawBalanceAmount(DrawContext context) {
        String formatted = TextUtils.formatLarge(balance);

        int width = textRenderer.getWidth(formatted);
        int drawX = Math.max(64 - width, 17);

        context.drawText(textRenderer, formatted, drawX, 186,0x00AA00, true);
    }

    private void drawWinAmount(DrawContext context) {
        String formatted = TextUtils.formatLarge(lastWinAmount);

        int width = textRenderer.getWidth(formatted);
        int drawX = Math.max(188 - width, 141);

        context.drawText(textRenderer, formatted, drawX, 186, 0x00AA00, true);
    }

    // GETTERS
    public boolean isSpinning() {
        return this.isSpinning;
    }

}