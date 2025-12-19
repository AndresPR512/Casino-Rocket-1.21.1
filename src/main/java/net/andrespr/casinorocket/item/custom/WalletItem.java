package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.screen.custom.chip_table.WalletScreenFactory;
import net.andrespr.casinorocket.sound.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WalletItem extends Item {

    public WalletItem(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            boolean rare = world.random.nextInt(64) == 0;
            world.playSound(null, player.getBlockPos(), rare ? ModSounds.WALLET2 : ModSounds.WALLET, SoundCategory.PLAYERS,2.0F, 1.0F);
            player.openHandledScreen(new WalletScreenFactory());
        }
        return TypedActionResult.consume(player.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, @NotNull List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.casinorocket.wallet"));
        super.appendTooltip(stack, context, tooltip, type);
    }

}