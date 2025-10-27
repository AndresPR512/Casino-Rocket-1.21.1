package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.ItemGachaponConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import java.util.Objects;

public class ItemGachaponItem extends Item {

    private final String poolKey;

    public ItemGachaponItem(Settings settings, String poolKey) {
        super(settings);
        this.poolKey = poolKey;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            ItemStack reward = pickWeightedReward(world.random);

            if (!Objects.requireNonNull(reward).isEmpty()) {
                String itemName = reward.getItem().getName(reward).getString();
                int amount = reward.getCount();
                user.giveItemStack(reward);
                user.sendMessage(Text.literal("¡Has obtenido " + amount + "x " + itemName + "!"), true);
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

    private ItemStack pickWeightedReward(Random random) {
        List<ItemGachaponConfig.GachaEntry> pool = CasinoRocket.CONFIG.itemGachapon.pools.get(poolKey);

        if (pool == null || pool.isEmpty()) {
            return ItemStack.EMPTY;
        }

        List<ItemGachaponConfig.GachaEntry> validEntries = pool.stream()
                .filter(entry -> {
                    Identifier id = Identifier.of(entry.itemId);
                    boolean exists = Registries.ITEM.containsId(id);
                    if (!exists) {
                        CasinoRocket.LOGGER.warn("[Gachapon] Item inválido en pool '{}': '{}'", poolKey, entry.itemId);
                    }
                    return exists;
                })
                .toList();

        if (validEntries.isEmpty()) {
            CasinoRocket.LOGGER.warn("[Gachapon] No hay ítems válidos en el pool '{}'", poolKey);
            return ItemStack.EMPTY;
        }

        int totalWeight = validEntries.stream().mapToInt(e -> e.weight).sum();
        if (totalWeight <= 0) {
            CasinoRocket.LOGGER.warn("[Gachapon] Todos los ítems en '{}' tienen peso 0 o inválido.", poolKey);
            return ItemStack.EMPTY;
        }

        int roll = random.nextInt(totalWeight);
        int cumulative = 0;

        for (ItemGachaponConfig.GachaEntry entry : validEntries) {
            cumulative += entry.weight;
            if (roll < cumulative) {
                Identifier id = Identifier.of(entry.itemId);
                Item item = Registries.ITEM.get(id);
                int count = Math.max(1, entry.count);
                return new ItemStack(item, count);
            }
        }

        return ItemStack.EMPTY;
    }

}