package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.gacha.GachaponUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class GachaponItem extends Item {

    private final String poolKey;

    public GachaponItem(Settings settings, String poolKey) {
        super(settings);
        this.poolKey = poolKey;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient && user instanceof ServerPlayerEntity player) {
            ItemStack reward = GachaponUtils.pickItemReward(world.random, poolKey);

            if (!reward.isEmpty()) {
                String itemName = reward.getItem().getName(reward).getString();
                int amount = reward.getCount();
                user.giveItemStack(reward);
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.item_gachapon_received", true, amount, itemName);
                CasinoRocketLogger.info("Player '{}' opened a '{}' and got '{}'",
                        player.getName().getString(), stack.getName().getString(), itemName);
                stack.decrement(1);
            } else {
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.item_gachapon_empty", true);
                CasinoRocketLogger.warn("Player '{}' tried to open a '{}' but all items in pool '{}' are invalid!",
                        player.getName().getString(), stack.getName().getString(), poolKey);
            }

        }
        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, @NotNull List<Text> tooltip, TooltipType type) {
        Identifier id = Registries.ITEM.getId(this);
        tooltip.add(Text.translatable("tooltip.casinorocket." + id.getPath()));
        super.appendTooltip(stack, context, tooltip, type);
    }

}