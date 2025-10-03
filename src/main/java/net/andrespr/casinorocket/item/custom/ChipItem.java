package net.andrespr.casinorocket.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class ChipItem extends Item {

    private final String tooltipkey;

    public ChipItem(Settings settings, String tooltipkey) {
        super(settings);
        this.tooltipkey = tooltipkey;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(this.tooltipkey));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
