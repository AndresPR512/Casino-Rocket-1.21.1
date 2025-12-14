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

    private boolean pressed = false;
    private boolean forcedPressed = false;

    private boolean fakePressed = false;

    public SlotButton(int x, int y, int width, int height, Identifier texture, PressAction onPress, Text text) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
        this.texWidth = width;
        this.stateHeight = height;
        this.texHeight = height * 3;
    }

    public void setForcedPressed(boolean forced) {
        this.forcedPressed = forced;
    }

    public void setFakePressed(boolean fake) {
        this.fakePressed = fake;
        this.active = !fake;
        if (fake) {
            this.pressed = false;
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.pressed = true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        this.pressed = false;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int vOffset = 0;

        // Fake pressed
        if (this.fakePressed) {
            vOffset = 0;
        }
        // Disabled
        else if (!this.active) {
            vOffset = this.stateHeight * 2;
        }
        // Forced pressed
        else if (this.forcedPressed) {
            vOffset = this.stateHeight * 2;
        }
        // Real click
        else if (this.pressed) {
            vOffset = this.stateHeight * 2;
        }
        // Hover
        else if (this.isHovered()) {
            vOffset = this.stateHeight;
        }

        context.drawTexture(this.texture, this.getX(), this.getY(), 0, vOffset,
                this.getWidth(), this.stateHeight, this.texWidth, this.texHeight);
    }

}