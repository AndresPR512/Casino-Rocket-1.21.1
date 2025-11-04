package net.andrespr.casinorocket.item.custom;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.CobblemonUtils;
import net.andrespr.casinorocket.util.gacha.PokemonGachaponUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;

public class PokemonGachaponItem extends Item {

    private final String poolKey;

    public PokemonGachaponItem(Settings settings, String poolKey) {
        super(settings.maxCount(1));
        this.poolKey = poolKey;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient && user instanceof ServerPlayerEntity player) {
            MinecraftServer server = player.getServer();
            var reward = PokemonGachaponUtils.pickPokemonReward(world.random, poolKey);

            if (reward != null) {
                PokemonProperties properties = CobblemonUtils.safeParse(reward.pokemonId(), player, server);
                Objects.requireNonNull(properties).setLevel(reward.level());
                properties.setIvs(CobblemonUtils.createFixedIVs(reward.ivs()));
                properties.setShiny(reward.shiny());

                CobblemonUtils.addPokemon(properties, player);

                stack.decrement(1);
            } else {
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.item_gachapon_empty", true, poolKey);
                CasinoRocketLogger.warn("[Pokémon Gachapon] All Pokémon in '{}' are invalid or have 0 weight!", poolKey);
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