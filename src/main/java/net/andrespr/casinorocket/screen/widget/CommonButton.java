package net.andrespr.casinorocket.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CommonButton extends ButtonWidget {

    private final Identifier texture;
    private final int texWidth;
    private final int texHeight;
    private final int stateHeight;

    public CommonButton(int x, int y, int width, int height, Identifier texture, PressAction onPress, Text text) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
        this.texWidth = width;
        this.stateHeight = height;
        this.texHeight = height * 3;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int vOffset;

        if (!this.active) {
            vOffset = 0;
        } else if (this.isHovered()) {
            vOffset = this.stateHeight * 2;
        } else {
            vOffset = this.stateHeight;
        }

        context.drawTexture(this.texture, this.getX(), this.getY(), 0, vOffset,
                this.getWidth(), this.stateHeight, this.texWidth, this.texHeight);

        int color = this.active ? 0xFFFFFF : 0xA0A0A0;

        int centerX = this.getX() + this.getWidth() / 2;
        int centerY = this.getY() + (this.stateHeight - 8) / 2;

        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,
                this.getMessage(), centerX, centerY, color);

    }

}