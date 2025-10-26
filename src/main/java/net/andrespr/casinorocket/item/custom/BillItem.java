package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.MinecraftServer;
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
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            MinecraftServer server = user.getServer();
            if (server != null && user.isSneaking()) {
                ItemStack stack = user.getStackInHand(hand);
                int count = stack.getCount(); // cuántos billetes tiene
                long totalValue = value * count;

                String command = "cobbledollars give " + user.getName().getString() + " " + totalValue;
                server.getCommandManager().executeWithPrefix(user.getCommandSource().withLevel(4), command);

                String logMsg = "[CasinoRocket] " + user.getName().getString() + " deposited $" + TextUtils.formatCompact(totalValue) + " into his wallet.";
                server.getPlayerManager().getPlayerList().forEach(p -> {
                    if (server.getPlayerManager().isOperator(p.getGameProfile())) {
                        p.sendMessage(Text.literal(logMsg), false);
                    }
                });
                server.sendMessage(Text.literal(logMsg));

                user.getStackInHand(hand).decrement(count);
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