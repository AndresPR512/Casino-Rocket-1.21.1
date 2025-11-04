package net.andrespr.casinorocket.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.IVs;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public class CobblemonUtils {

    public static PokemonProperties safeParse(String input, ServerPlayerEntity player, MinecraftServer server) {
        PokemonProperties properties = tryParse(input);
        if (properties == null) {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.species_not_found", false, input);
            CasinoRocketLogger.toOps(server, CasinoRocketLogger.LogLevel.ERROR,
                    String.format("Player '{}' tried to claim '{}' but failed! (species not found)", player.getName().getString(), input));
            return null;
        }
        return properties;
    }

    @Nullable
    public static PokemonProperties tryParse(String input) {
        try {
            return PokemonProperties.Companion.parse(input);
        } catch (Exception e) {
            CasinoRocketLogger.debug("Invalid Pokémon string: " + input + " (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
            return null;
        }
    }

    public static IVs createFixedIVs(int fixedIVs) {
        IVs ivs = new IVs();
        ivs.set(Stats.HP, fixedIVs);
        ivs.set(Stats.ATTACK, fixedIVs);
        ivs.set(Stats.DEFENCE, fixedIVs);
        ivs.set(Stats.SPECIAL_ATTACK, fixedIVs);
        ivs.set(Stats.SPECIAL_DEFENCE, fixedIVs);
        ivs.set(Stats.SPEED, fixedIVs);
        return ivs;
    }

    public static void addPokemon(PokemonProperties properties, ServerPlayerEntity player) {
        Pokemon pokemon = properties.create();
        String pokemonName = pokemon.getSpecies().getTranslatedName().getString();
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
        boolean added = party.add(pokemon);
        if (added) {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.pokemon_received_party", true, pokemonName);
            CasinoRocketLogger.info(player.getName().getString() + " claimed Pokémon (" + pokemonName + ")");
        } else {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.pokemon_box_full", true, pokemonName);
            CasinoRocketLogger.warn(player.getName().getString() + " tried to claim Pokémon (" + pokemonName + ") but has no space in PC!");
        }
    }

}