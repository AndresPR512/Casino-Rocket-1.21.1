package net.andrespr.casinorocket.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SlotButton extends ButtonWidget {
    private final Identifier texture;
    private final int texWidth;
    private final int texHeight;
    private final int stateHeight;

    public SlotButton(int x, int y, int width, int height, Identifier texture, PressAction onPress, Text text) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
        this.texWidth = width;
        this.stateHeight = height;       // la altura de un estado (ej: 14)
        this.texHeight = height * 3;     // textura total (3 estados)
    }

    @Override
    public void renderWidget(DrawContext ctx, int mouseX, int mouseY, float delta) {
        int vOffset = 0;
        if (!this.active) vOffset = this.stateHeight * 2; // estado desactivado (opcional)
        else if (this.isHovered()) vOffset = this.stateHeight; // hovered

        ctx.drawTexture(this.texture, this.getX(), this.getY(),
                0, vOffset, this.getWidth(), this.stateHeight,
                this.texWidth, this.texHeight);
    }

}