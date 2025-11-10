package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BillItem extends Item {

    private final long value;

    public BillItem(Settings settings, long value) {
        super(settings);
        this.value = value;
        ModItems.ALL_BILL_ITEMS.add(this);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof ServerPlayerEntity player && player.isSneaking()) {
            MinecraftServer server = player.getServer();
            if (server != null) {
                ItemStack stack = player.getStackInHand(hand);
                int count = stack.getCount();
                long totalValue = value * count;

                String command = "cobbledollars give " + player.getName().getString() + " " + totalValue;
                server.getCommandManager().executeWithPrefix(player.getCommandSource().withLevel(4).withSilent(), command);

                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.money_deposited", true, TextUtils.formatCompact(totalValue));
                CasinoRocketLogger.info(player.getName().getString() + " deposited $" + TextUtils.formatCompact(totalValue) + " into his wallet.");

                stack.decrement(count);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, @NotNull List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.casinorocket.bill"));
        super.appendTooltip(stack, context, tooltip, type);
    }

}