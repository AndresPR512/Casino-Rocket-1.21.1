package net.andrespr.casinorocket.screen.custom.blackjack;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.games.blackjack.BlackjackAction;
import net.andrespr.casinorocket.games.blackjack.BlackjackPhase;
import net.andrespr.casinorocket.games.blackjack.BlackjackRules;
import net.andrespr.casinorocket.network.c2s.blackjack.BlackjackActionC2SPayload;
import net.andrespr.casinorocket.network.c2s.blackjack.ChangeBlackjackBetIndexC2SPayload;
import net.andrespr.casinorocket.network.s2c.SendBlackjackStateS2CPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.custom.CasinoMachineScreen;
import net.andrespr.casinorocket.screen.widget.CommonButton;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.andrespr.casinorocket.screen.widget.SlotButton;
import net.andrespr.casinorocket.sound.ModSounds;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.TextUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class BlackjackTableScreen extends CasinoMachineScreen<BlackjackTableScreenHandler> {

    // --- BUTTONS ---
    private CommonButton playBtn, hitBtn, standBtn, doubleDownBtn;
    private CommonButton finishBtn, doubleOrNothingBtn;
    private SlotButton betButton;
    private SlotButton withdrawButton;
    private SlotButton plusButton;
    private SlotButton subtractButton;

    // --- STATE (S2C) ---
    private long balance = 0L;
    private int betIndex = 0;
    private long currentBet = 0L;

    private BlackjackPhase phase = BlackjackPhase.IDLE;
    private long winPayout = 0L;

    private int[] playerCards = new int[0];
    private int[] dealerCards = new int[0];

    private String playerValueText = "";
    private String dealerValueText = "";

    private boolean canPlay, canHit, canStand, canDoubleDown, canFinish, canDoubleOrNothing;

    // --- RESULT CONTROL ---
    private int lastSeenResultId = -1;
    private Text resultText = null;
    private int resultColor = 0xFFFFFF;
    private int resultTicks = 0;

    public BlackjackTableScreen(BlackjackTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 242;
        this.backgroundHeight = 222;
    }

    @Override
    @SuppressWarnings("unused")
    protected void init() {
        super.init();

        int baseX = (this.width - this.backgroundWidth) / 2;
        int baseY = (this.height - this.backgroundHeight) / 2;

        this.betButton = ModButtons.bet(baseX, baseY, 6, 6, b -> onBetPressed());
        this.withdrawButton = ModButtons.withdraw(baseX, baseY, 164, 6, b -> onWithdrawPressed());
        this.subtractButton = ModButtons.subtract(baseX, baseY, 84, 18, b -> onSubtractPressed());
        this.plusButton = ModButtons.plus(baseX, baseY, 145, 18, b -> onPlusPressed());

        this.addDrawableChild(betButton);
        this.addDrawableChild(withdrawButton);
        this.addDrawableChild(subtractButton);
        this.addDrawableChild(plusButton);

        this.playBtn = ModButtons.play(baseX, baseY, 166, 133, b -> sendAction(BlackjackAction.PLAY));
        this.hitBtn = ModButtons.hit(baseX, baseY, 166, 148, b -> sendAction(BlackjackAction.HIT));
        this.standBtn = ModButtons.stand(baseX, baseY, 166, 163, b -> sendAction(BlackjackAction.STAND));
        this.doubleDownBtn = ModButtons.doubleDown(baseX, baseY, 166, 178, b -> sendAction(BlackjackAction.DOUBLE_DOWN));

        this.finishBtn = ModButtons.finish(baseX, baseY, 166, 148, b -> sendAction(BlackjackAction.FINISH));
        this.doubleOrNothingBtn = ModButtons.doubleOrNothing(baseX, baseY, 166, 163, b -> sendAction(BlackjackAction.DOUBLE_OR_NOTHING));

        this.addDrawableChild(playBtn);
        this.addDrawableChild(hitBtn);
        this.addDrawableChild(standBtn);
        this.addDrawableChild(doubleDownBtn);
        this.addDrawableChild(finishBtn);
        this.addDrawableChild(doubleOrNothingBtn);

        updateButtons();

    }

    // === BUTTONS ===
    private void sendAction(BlackjackAction action) {
        if (client == null || client.player == null) return;

        if (action == BlackjackAction.HIT) client.player.playSound(ModSounds.CARD, 1.0f, 1.0f);

        ClientPlayNetworking.send(new BlackjackActionC2SPayload(
                this.handler.getMachinePos(), this.handler.getMachineKey(), action));
    }

    private void onPlusPressed() {
        if (client != null && client.player != null) {
            ClientPlayNetworking.send(new ChangeBlackjackBetIndexC2SPayload(true));
        }
    }

    private void onSubtractPressed() {
        if (client != null && client.player != null) {
            ClientPlayNetworking.send(new ChangeBlackjackBetIndexC2SPayload(false));
        }
    }

    public void applyState(SendBlackjackStateS2CPayload p) {

        this.balance = p.balance();
        this.betIndex = p.betIndex();
        this.currentBet = p.currentBet();

        this.phase = p.phase();
        this.winPayout = p.winPayout();

        this.playerCards = p.playerCards();
        this.dealerCards = p.dealerCards();

        this.playerValueText = p.playerValueText();
        this.dealerValueText = p.dealerValueText();

        this.canPlay = p.canPlay();
        this.canHit = p.canHit();
        this.canStand = p.canStand();
        this.canDoubleDown = p.canDoubleDown();
        this.canFinish = p.canFinish();
        this.canDoubleOrNothing = p.canDoubleOrNothing();

        updateButtons();

        if (lastSeenResultId == -1) {
            lastSeenResultId = p.resultId();
        } else if (p.resultId() != lastSeenResultId) {
            lastSeenResultId = p.resultId();
            showResultText(p.resolvedBet(), p.resolvedPayout());
            playResultSound(p.resolvedBet(), p.resolvedPayout());
        }

    }

    // === TICK ===
    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        if (resultTicks > 0) {
            resultTicks--;
            if (resultTicks == 0) resultText = null;
        }
    }

    // === DRAW BACKGROUND ===
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        RenderSystem.setShaderTexture(0, ModGuiTextures.BLACKJACK_TABLE_GUI);
        context.drawTexture(ModGuiTextures.BLACKJACK_TABLE_GUI, x, y, 0, 0, backgroundWidth, backgroundHeight);

        drawDealerCards(context, x, y, this.dealerCards);
        drawPlayerCards(context, x, y, this.playerCards);
    }

    @Override
    protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) {

        // ===== LABELS =====
        // Bet Amount
        drawCenteredTextInBox(ctx, Text.translatable("gui.casinorocket.blackjack_table.bet_amount").getString(),
                85, 7, 155, 14, 0xFFFFFF);
        // Dealer's Hand
        drawCenteredTextInBox(ctx, Text.translatable("gui.casinorocket.blackjack_table.dealers_hand", dealerValueText).getString(),
                53, 46, 188, 53, 0xFFFFFF);
        // Player's Hand
        drawCenteredTextInBox(ctx, Text.translatable("gui.casinorocket.blackjack_table.players_hand", playerValueText).getString(),
                15, 143, 150, 150, 0xFFFFFF);
        // Balance
        drawCenteredTextInBox(ctx, Text.translatable("gui.casinorocket.blackjack_table.balance").getString(),
                15, 209, 64, 216, 0xFFFFFF);
        // Last Win
        drawCenteredTextInBox(ctx, Text.translatable("gui.casinorocket.blackjack_table.last_win").getString(),
                130, 209, 179, 216, 0xFFFFFF);
        // Result text
        if (resultText != null && resultTicks > 0) {
            drawCenteredTextInBox(ctx, resultText.getString(), 49, 131, 116, 138, resultColor);
        }

        // ===== VALUES =====
        drawNumericValues(ctx);

    }

    private void drawNumericValues(DrawContext ctx) {

        long betAmount = BlackjackRules.BET_VALUES.get(betIndex);

        // Bet Amount
        drawRightAlignedTextInBox(ctx, TextUtils.formatCompact(betAmount), 101, 20, 128, 27, resolveInsufficientBalance(balance, betIndex));
        // Balance
        drawRightAlignedTextInBox(ctx, TextUtils.formatCompact(balance), 73, 209, 100, 216, 0x00FF00);
        // Last Win
        drawRightAlignedTextInBox(ctx, TextUtils.formatCompact(winPayout), 188, 209, 215, 216, winPayout > 0 ? 0x00FF00 : 0xAAAAAA);

    }

    // === BLOCK ESC / CLOSE SCREEN ===
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isBusy() && client != null && client.player != null) {
            if (keyCode == 256 /* GLFW.GLFW_KEY_ESCAPE */) {
                CasinoRocketLogger.toPlayerTranslated(client.player, "gui.casinorocket.blackjack.esc", true);
                return true;
            }
            if (client.options != null && client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        if (isBusy()) {
            return;
        }
        super.close();
    }

    @Override
    protected boolean returnToMachineOnEsc() {
        return false;
    }

    private boolean isBusy() {
        return phase != BlackjackPhase.IDLE;
    }

    // === DRAW CARDS METHODS ===
    private void drawDealerCards(DrawContext context, int guiX, int guiY, int[] cards) {
        if (cards == null || cards.length == 0) return;

        for (int i = 0; i < cards.length; i++) {
            int cardId = cards[i];

            int slot = i % 4;
            int loop = i / 4;

            int xRel;
            int yRel;

            if (slot == 0) { xRel = 90;  yRel = 64; }
            else if (slot == 1) { xRel = 128; yRel = 64; }
            else if (slot == 2) { xRel = 166; yRel = 62; }
            else { xRel = 52;  yRel = 62; }

            xRel += loop * 7;

            drawCardAt(context, guiX + xRel, guiY + yRel, cardId);
        }
    }

    private void drawPlayerCards(DrawContext context, int guiX, int guiY, int[] cards) {
        if (cards == null || cards.length == 0) return;

        for (int i = 0; i < cards.length; i++) {
            int cardId = cards[i];

            int slot = i % 4;
            int loop = i / 4;

            int xRel;
            int yRel = 160;

            if (slot == 0) xRel = 17;
            else if (slot == 1) xRel = 53;
            else if (slot == 2) xRel = 89;
            else xRel = 125;

            xRel += loop * 7;

            drawCardAt(context, guiX + xRel, guiY + yRel, cardId);
        }
    }

    private void drawCardAt(DrawContext context, int x, int y, int cardId) {
        if (cardId < 0) {
            context.drawTexture(ModGuiTextures.CARD_BOTTOM, x, y, 0, 0, 24, 32, 24, 32);
            return;
        }
        drawCardFromSpritesheet(context, x, y, cardId);
    }

    private void drawCardFromSpritesheet(DrawContext context, int x, int y, int cardId) {
        int i = Math.floorMod(cardId, 13);
        int j = Math.floorDiv(cardId, 13);

        int u = i * 24;
        int v = j * 32;

        context.drawTexture(ModGuiTextures.CARDS_SPRITESHEET, x, y, u, v, 24, 32, 312, 128);
    }

    // === BUTTONS STATE / VISUAL ===
    private void updateButtons() {

        boolean idle = (phase == BlackjackPhase.IDLE);

        // ===== TOP COMMON BUTTON =====
        if (betButton != null) {
            betButton.visible = true;
            betButton.active = idle;
            betButton.setFakePressed(!idle);
        }

        if (withdrawButton != null) {
            withdrawButton.visible = true;
            withdrawButton.active = idle;
            withdrawButton.setFakePressed(!idle);
        }

        if (plusButton != null) {
            plusButton.visible = true;
            plusButton.active = idle;
            plusButton.setFakePressed(!idle);
        }

        if (subtractButton != null) {
            subtractButton.visible = true;
            subtractButton.active = idle;
            subtractButton.setFakePressed(!idle);
        }

        updateBetIndexButtons();

        // ===== BLACKJACK BUTTONS =====
        boolean showCore = (phase == BlackjackPhase.IDLE
                || phase == BlackjackPhase.PLAYER_TURN
                || phase == BlackjackPhase.DEALER_TURN);

        boolean showResult = (phase == BlackjackPhase.RESULT_PENDING);

        // CORE: play/hit/stand/doubleDown
        if (playBtn != null) {
            playBtn.visible = showCore;
            playBtn.active = showCore && canPlay;
        }
        if (hitBtn != null) {
            hitBtn.visible = showCore;
            hitBtn.active = showCore && canHit;
        }
        if (standBtn != null) {
            standBtn.visible = showCore;
            standBtn.active = showCore && canStand;
        }
        if (doubleDownBtn != null) {
            doubleDownBtn.visible = showCore;
            doubleDownBtn.active = showCore && canDoubleDown;
        }

        // RESULT: finish / double or nothing
        if (finishBtn != null) {
            finishBtn.visible = showResult;
            finishBtn.active = showResult && canFinish;
        }
        if (doubleOrNothingBtn != null) {
            doubleOrNothingBtn.visible = showResult;
            doubleOrNothingBtn.active = showResult && canDoubleOrNothing;
        }

    }

    private void updateBetIndexButtons() {
        int max = BlackjackRules.BET_VALUES.size() - 1;

        subtractButton.active = betIndex > 0 && phase == BlackjackPhase.IDLE;
        plusButton.active = betIndex < max && phase == BlackjackPhase.IDLE;

        subtractButton.setForcedPressed(betIndex == 0);
        plusButton.setForcedPressed(betIndex == max);
    }

    // === SOUND ===
    private void playResultSound(long resolvedBet, long resolvedPayout) {
        if (client == null || client.player == null) return;

        if (resolvedPayout > resolvedBet) {
            client.player.playSound(ModSounds.WIN, 1.0f, 1.0f);
            return;
        }

        if (resolvedPayout == resolvedBet && resolvedBet > 0) {
            client.player.playSound(ModSounds.DRAW, 1.0f, 1.0f);
            return;
        }

        client.player.playSound(ModSounds.LOSE, 1.0f, 1.0f);
    }

    // === TEXT ===
    private void drawCenteredTextInBox(DrawContext ctx, String text,
                                       int x1, int y1, int x2, int y2, int color) {
        int textWidth = textRenderer.getWidth(text);
        int textHeight = 8;

        int cx = x1 + (x2 - x1 - textWidth) / 2;
        int cy = y1 + (y2 - y1 - textHeight) / 2;

        ctx.drawText(textRenderer, text, cx, cy, color, true);
    }

    private void drawRightAlignedTextInBox(DrawContext ctx, String text,
                                           int x1, int y1, int x2, int y2, int color) {
        int textWidth = textRenderer.getWidth(text);
        int textHeight = 8;

        int cx = x2 - textWidth;
        int cy = y1 + (y2 - y1 - textHeight) / 2;

        ctx.drawText(textRenderer, text, cx, cy, color, true);
    }

    private int resolveInsufficientBalance(long balance, int betIndex) {
        long betAmount = BlackjackRules.BET_VALUES.get(betIndex);
        boolean insufficientBalance = betAmount > balance;
        return insufficientBalance ? 0xFF5555 : 0x00FF00;
    }

    private void showResultText(long bet, long payout) {
        if (payout > bet) {
            resultText = Text.translatable("gui.casinorocket.blackjack.win");
            resultColor = 0xFFFFFF;
        } else if (payout == bet && bet > 0) {
            resultText = Text.translatable("gui.casinorocket.blackjack.draw");
            resultColor = 0xAAAAAA;
        } else {
            resultText = Text.translatable("gui.casinorocket.blackjack.lose");
            resultColor = 0xFF5555;
        }

        resultTicks = 40;
    }

}