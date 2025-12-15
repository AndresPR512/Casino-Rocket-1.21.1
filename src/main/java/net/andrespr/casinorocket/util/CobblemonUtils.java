package net.andrespr.casinorocket.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.IVs;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class CobblemonUtils {

    public static PokemonProperties safeParse(String input, ServerPlayerEntity player, MinecraftServer server) {
        PokemonProperties properties = tryParse(input);
        if (properties == null) {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.species_not_found", false, input);
            String messageToOps = String.format("[Cobblemon] Player " + player.getName().getString() +
                    " tried to claim " + player.getName().getString(), input + " but failed! (species not found)");
            CasinoRocketLogger.toOps(server, CasinoRocketLogger.LogLevel.ERROR, messageToOps);
            return null;
        }
        return properties;
    }

    @Nullable
    public static PokemonProperties tryParse(String input) {
        String propertiesString = toPropertiesString(input);
        String expectedSpecies = getRawId(input);
        PokemonProperties pokemon = PokemonProperties.Companion.parse(propertiesString);
        String species = pokemon.getSpecies();
        if (species == null || !stripNamespace(species).equalsIgnoreCase(expectedSpecies)) {
            CasinoRocket.LOGGER.warn("[Cobblemon] Invalid Pokémon string: {}", input);
            return null;
        }
        return pokemon;
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
        } else {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.pokemon_box_full", true, pokemonName);
            CasinoRocket.LOGGER.warn("[Cobblemon] Player {} tried to claim Pokémon ({}) but has no space in PC!", player.getName().getString(), pokemonName);
        }
    }

    // === SPECIAL FORMS ===
    public static String getRawId(String pokemonId) {
        String rawPokemonId = pokemonId.toLowerCase();
        if (rawPokemonId.startsWith("alolan_")) return rawPokemonId.replace("alolan_","");
        if (rawPokemonId.startsWith("galarian_")) return rawPokemonId.replace("galarian_","");
        if (rawPokemonId.startsWith("hisuian_")) return rawPokemonId.replace("hisuian_","");
        if (rawPokemonId.startsWith("paldean_")) return rawPokemonId.replace("paldean_","");
        if (rawPokemonId.startsWith("netherite_")) return rawPokemonId.replace("netherite_","");
        if (rawPokemonId.endsWith("_m")) return rawPokemonId.replace("_m","");
        if (rawPokemonId.endsWith("_f")) return rawPokemonId.replace("_f","");
        return rawPokemonId;
    }

    public static String toPropertiesString(String configId) {
        String id = configId.toLowerCase();

        if (id.startsWith("alolan_")) {
            String species = id.substring("alolan_".length());
            return species + " alolan";
        }
        if (id.startsWith("galarian_")) {
            String species = id.substring("galarian_".length());
            return species + " galarian";
        }
        if (id.startsWith("hisuian_")) {
            String species = id.substring("hisuian_".length());
            return species + " hisuian";
        }
        if (id.startsWith("paldean_")) {
            String species = id.substring("paldean_".length());
            return species + " paldean";
        }

        if (id.startsWith("netherite_")) {
            String species = id.substring("netherite_".length());
            return species + " netherite_coating=full";
        }

        if (id.endsWith("_m")) {
            String species = id.replace("_m","");
            return species + " gender=male";
        }
        if (id.endsWith("_f")) {
            String species = id.replace("_f","");
            return species + " gender=female";
        }

        return id;
    }


    private static String stripNamespace(String id) {
        if (id.contains(":")) return id.substring(id.indexOf(":") + 1);
        return id;
    }

    // === SHINY ODDS ===
    public static boolean itWillBeShiny(Random random, String shiny) {
        String key = shiny.toLowerCase();
        return switch (key) {
            case "default" -> defaultOdds(random);
            case "boosted" -> boostedOdds(random);
            case "yes" -> true;
            default -> false;
        };
    }

    public static boolean defaultOdds(Random random) {
        int roll = random.nextInt(2048);
        return roll == 1;
    }

    public static boolean boostedOdds(Random random) {
        int roll = random.nextInt(512);
        return roll == 1;
    }

    // === BALL ===
    private static final Set<String> CHERISH_BALL_POKEMON = Set.of(
            "roaringmoon", "ironvaliant",
            "articuno", "zapdos", "moltres",
            "raikou", "entei", "suicune",
            "regirock", "regice", "registeel", "regieleki", "regidrago", "regigigas",
            "heatran", "cresselia",
            "tapukoko", "tapulele", "tapubulu", "tapufini",
            "magikarp", "gholdengo", "netherite_gholdengo", "mew", "diancie"
    );

    public static String getCherishBallIfLegendary(String pokemonId) {
        if (pokemonId == null) return null;
        String id = pokemonId.toLowerCase().trim();

        id = id.replaceFirst("^(alolan_|galarian_|hisuian_|paldean_)", "");

        if (CHERISH_BALL_POKEMON.contains(id)) {
            return "cherish_ball";
        }
        return "premier_ball";
    }

    // === POKÉMON NAME ===
    public static String getPokemonName(PokemonProperties properties) {
        Pokemon pokemon = properties.create();
        return pokemon.getSpecies().getTranslatedName().getString();
    }

}