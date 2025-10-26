package net.andrespr.casinorocket.item.custom;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
        if (!world.isClient && entity instanceof ServerPlayerEntity player) {

            NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
            NbtCompound tag = nbtComponent != null ? nbtComponent.copyNbt() : new NbtCompound();

            if (!tag.getBoolean("Used")) {
                MinecraftServer server = player.getServer();
                Identifier id = Registries.ITEM.getId(this);
                String speciesName = id.getPath().replace("_pin", "");

                PokemonProperties properties;
                try {
                    properties = PokemonProperties.Companion.parse(speciesName);
                } catch (Exception e) {
                    player.sendMessage(Text.literal("Error: The species '" + speciesName + "' was not found."), false);
                    return;
                }

                properties.setLevel(5);

                Pokemon pokemon = properties.create();
                try {
                    pokemon.setIV(Stats.HP, fixed_ivs);
                    pokemon.setIV(Stats.ATTACK, fixed_ivs);
                    pokemon.setIV(Stats.DEFENCE, fixed_ivs);
                    pokemon.setIV(Stats.SPECIAL_ATTACK, fixed_ivs);
                    pokemon.setIV(Stats.SPECIAL_DEFENCE, fixed_ivs);
                    pokemon.setIV(Stats.SPEED, fixed_ivs);
                } catch (Exception e) {
                    System.out.println("[CasinoRocket] Error setting IVs for " + speciesName + ": " + e.getMessage());
                }

                Text pokemonName = pokemon.getSpecies().getTranslatedName();

                PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
                int before = party.occupied();
                boolean added = party.add(pokemon);
                int after = party.occupied();
                boolean sentToPC = added && before == after;

                if (added) {
                    if (!sentToPC) {
                        player.sendMessage(Text.translatable("message.casinorocket.pokemon_received_party", pokemonName), true);
                    } else {
                        player.sendMessage(Text.translatable("message.casinorocket.pokemon_received_box", pokemonName), true);
                    }
                } else {
                    player.sendMessage(Text.translatable("message.casinorocket.pokemon_box_full", pokemonName), true);
                }

                if (server != null) {
                    String logMsg = "[CasinoRocket] " + player.getName().getString() + " obtained Pokémon (" + speciesName.toUpperCase() + ")";
                    server.getPlayerManager().getPlayerList().forEach(p -> {
                        if (server.getPlayerManager().isOperator(p.getGameProfile())) {
                            p.sendMessage(Text.literal(logMsg), false);
                        }
                    });
                    server.sendMessage(Text.literal(logMsg));
                }

                tag.putBoolean("Used", true);
                stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));

                player.getInventory().removeOne(stack);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.casinorocket.pokemon_pin"));
        super.appendTooltip(stack, context, tooltip, type);
    }

}