package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChipItem extends Item {

    private final long value;

    public ChipItem(Settings settings, long value) {
        super(settings);
        this.value = value;
        ModItems.ALL_CHIP_ITEMS.add(this);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, @NotNull List<Text> tooltip, TooltipType type) {
        Text valueText = Text.literal(TextUtils.formatCompact(value));
        tooltip.add(Text.translatable("tooltip.casinorocket.chip", valueText.copy().formatted(Formatting.GOLD)));
        super.appendTooltip(stack, context, tooltip, type);
    }

    // === GETTERS ===

    public long getValue() {
        return value;
    }

}