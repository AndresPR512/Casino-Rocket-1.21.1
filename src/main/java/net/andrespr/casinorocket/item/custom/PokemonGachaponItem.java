package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.ItemGachaponConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PokemonGachaponItem extends Item {

    private final String poolKey;

    public PokemonGachaponItem(Settings settings, String poolKey) {
        super(settings);
        this.poolKey = poolKey;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            Item reward = pickWeightedReward(world.random);

            if (reward != null) {
                user.giveItemStack(new ItemStack(reward));
                user.sendMessage(Text.literal("¡Has obtenido un " + reward.getName().getString() + "!"), true);
                stack.decrement(1);
            } else {
                user.sendMessage(Text.literal("No hay recompensas configuradas para: " + poolKey), true);
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

    private Item pickWeightedReward(Random random) {
        List<ItemGachaponConfig.GachaEntry> pool = CasinoRocket.CONFIG.itemGachapon.pools.get(poolKey);

        if (pool == null || pool.isEmpty()) {
            return null;
        }

        int totalWeight = pool.stream().mapToInt(r -> r.weight).sum();
        int roll = random.nextInt(totalWeight);
        int cumulative = 0;

        for (ItemGachaponConfig.GachaEntry entry : pool) {
            cumulative += entry.weight;
            if (roll < cumulative) {
                return Registries.ITEM.get(Identifier.of(entry.itemId));
            }
        }

        return null;
    }

}