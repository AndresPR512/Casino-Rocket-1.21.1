package net.andrespr.casinorocket.screen.layout;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class DancingClefairy {

    public enum Phase {
        NEUTRAL, // idle / spinning
        LOSS,    // no prize
        WIN      // prize
    }

    private final Identifier texture;
    private final int width;
    private final int frameHeight;
    private final int totalHeight;

    private Phase currentPhase = Phase.NEUTRAL;
    private int frameToggle = 0;
    private int tickCounter = 0;

    private static final int TICKS_PER_SWAP = 6;

    public DancingClefairy(Identifier texture, int width, int frameHeight) {
        this.texture = texture;
        this.width = width;
        this.frameHeight = frameHeight;
        this.totalHeight = frameHeight * 6; // 6 frames
    }

    public void tick(Phase phase) {
        this.currentPhase = phase;

        tickCounter++;
        if (tickCounter >= TICKS_PER_SWAP) {
            tickCounter = 0;
            frameToggle ^= 1;
        }
    }

    public void render(DrawContext context, int x, int y) {
        int pairIndex = switch (currentPhase) {
            case NEUTRAL -> 0;
            case LOSS    -> 1;
            case WIN     -> 2;
        };

        int frame = pairIndex * 2 + frameToggle;
        int v = frame * frameHeight;

        context.drawTexture(texture, x, y, 0, v, width, frameHeight, width, totalHeight);
    }

}