package net.andrespr.casinorocket.screen.layout;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class SlotLineSprite {

    private final Identifier texture;
    private final int width;
    private final int heightPerState;
    private final int fullHeight;

    public SlotLineSprite(Identifier texture, int width, int heightPerState) {
        this.texture = texture;
        this.width = width;
        this.heightPerState = heightPerState;
        this.fullHeight = heightPerState * 2;
    }

    public void render(DrawContext context, int x, int y, boolean enabled) {
        int v = enabled ? heightPerState : 0;
        context.drawTexture(texture, x, y, 0, v, width, heightPerState, width, fullHeight);
    }

}