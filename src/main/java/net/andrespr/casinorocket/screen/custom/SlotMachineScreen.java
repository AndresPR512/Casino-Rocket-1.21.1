package net.andrespr.casinorocket.screen.custom;

import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.widget.SlotButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class SlotMachineScreen extends HandledScreen<SlotMachineScreenHandler> {

    public SlotMachineScreen(SlotMachineScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 140;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = Integer.MAX_VALUE;

        int x = this.x + 8;
        int y = this.y + 6;

        this.addDrawableChild(new SlotButton(
                x, y, 42, 14,
                ModGuiTextures.BTN_BET,
                b -> onBetPressed(),
                Text.empty()
        ));
    }

    private void onBetPressed() {
        if (this.client != null && this.client.player != null) {
            this.client.player.sendMessage(Text.literal("[SlotMachine] BET pressed"), false);
        }
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        ctx.drawTexture(ModGuiTextures.SLOT_MACHINE_BG, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

}