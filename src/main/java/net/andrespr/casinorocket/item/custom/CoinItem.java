package net.andrespr.casinorocket.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.List;

public class CoinItem extends Item {

    public CoinItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.casinorocket." + Registries.ITEM.getId(this).getPath()));
        super.appendTooltip(stack, context, tooltip, type);
    }

}