package net.andrespr.casinorocket.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class PokemonPinItem extends Item {

    private final int fixed_ivs;

    public PokemonPinItem(Settings settings, int fixed_ivs) {
        super(settings.maxCount(1));
        this.fixed_ivs = fixed_ivs;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player) {

            NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
            NbtCompound tag = nbtComponent != null ? nbtComponent.copyNbt() : new NbtCompound();

            if (!tag.getBoolean("Used")) {
                MinecraftServer server = player.getServer();
                Identifier id = Registries.ITEM.getId(this);
                String pokemonid = id.getPath();
                String pokemon = pokemonid.replace("_pin", "");

                if (server != null) {
                    String command = "givepokemon " + player.getName().getString() + " " + pokemon +
                            " hp_iv=" + fixed_ivs +
                            " attack_iv=" + fixed_ivs +
                            " defence_iv=" + fixed_ivs +
                            " special_attack_iv=" + fixed_ivs +
                            " special_defence_iv=" + fixed_ivs +
                            " speed_iv=" + fixed_ivs;

                    server.getCommandManager().executeWithPrefix(
                            player.getCommandSource().withLevel(4),
                            command
                    );

                    String logMsg = "[CasinoRocket] " + player.getName().getString() + " obtained Pokémon (" + pokemon.toUpperCase() + ")";
                    server.getPlayerManager().getPlayerList().forEach(p -> {
                        if (server.getPlayerManager().isOperator(p.getGameProfile())) {
                            p.sendMessage(Text.literal(logMsg), false);
                        }
                    });
                    server.sendMessage(Text.literal(logMsg));

                    tag.putBoolean("Used", true);
                    stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));

                    player.getInventory().removeOne(stack);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    /*
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            MinecraftServer server = user.getServer();
            Identifier id = Registries.ITEM.getId(this);
            String pokemonid = id.getPath();
            String pokemon = pokemonid.replace("_pin","");
            if (server != null) {

                String command = "givepokemon " + user.getName().getString() + " " + pokemon +
                        " hp_iv=" + fixed_ivs +
                        " attack_iv=" + fixed_ivs +
                        " defence_iv=" + fixed_ivs +
                        " special_attack_iv=" + fixed_ivs +
                        " special_defence_iv=" + fixed_ivs +
                        " speed_iv=" + fixed_ivs;

                server.getCommandManager().executeWithPrefix(user.getCommandSource().withLevel(4), command);

                String logMsg = "[CasinoRocket] " + user.getName().getString() + " used a Pokémon Pin (" + pokemon.toUpperCase() + ")";
                server.getPlayerManager().getPlayerList().forEach(p -> {
                    if (server.getPlayerManager().isOperator(p.getGameProfile())) {
                        p.sendMessage(Text.literal(logMsg), false);
                    }
                });
                server.sendMessage(Text.literal(logMsg));

                user.getStackInHand(hand).decrement(1);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    */
    /*
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
    */

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.casinorocket.pokemon_pin"));
        super.appendTooltip(stack, context, tooltip, type);
    }

}