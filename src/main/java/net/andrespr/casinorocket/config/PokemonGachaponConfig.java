package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Config(name = "pokemon_gachapon")
public class PokemonGachaponConfig implements ConfigData {

    @CollapsibleObject
    public Map<String, List<GachaEntry>> pools = new LinkedHashMap<>();

    public static class GachaEntry {
        public String pokemonId;
        public int level;
        public int ivs;
        public String shiny;
        public int weight;

        public GachaEntry() {}
        public GachaEntry(String pokemonId, int level, int ivs, String shiny, int weight) {
            this.pokemonId = pokemonId;
            this.level = level;
            this.ivs = ivs;
            this.shiny = shiny;
            this.weight = weight;
        }

        public void validate() {
            if (ivs < 0) ivs = 0;
            if (ivs > 31) ivs = 31;
            if (level < 1) level = 1;
            if (level > 100) level = 100;
        }
    }

    @Override
    public void validatePostLoad() {
        if (pools.isEmpty()) {

            pools.put("common", List.of(
                    new GachaEntry("weedle", 5, 15,"default", 3),
                    new GachaEntry("pidgey", 5, 15,"default", 3),
                    new GachaEntry("clefable", 5, 15,"default", 2),
                    new GachaEntry("abra", 5, 15,"default", 3),
                    new GachaEntry("bellsprout", 5, 15,"default", 2),
                    new GachaEntry("slowpoke", 5, 15,"default", 1),
                    new GachaEntry("gastly", 5, 15,"default", 3),
                    new GachaEntry("kangaskhan", 5, 15,"default", 1),
                    new GachaEntry("staryu", 5, 15,"default", 2),
                    new GachaEntry("pinsir", 10, 15,"default", 1),
                    new GachaEntry("magikarp", 5, 15,"default", 4),
                    new GachaEntry("mareep", 5, 15,"default", 3),
                    new GachaEntry("onix", 10, 15,"default", 1),
                    new GachaEntry("scyther", 10, 15,"default", 1),
                    new GachaEntry("heracross", 10, 15,"default", 1),
                    new GachaEntry("skarmory", 5, 15,"default", 2),
                    new GachaEntry("houndour", 5, 15,"default", 3),
                    new GachaEntry("ralts", 5, 15,"default", 3),
                    new GachaEntry("sableye", 10, 15,"default", 1),
                    new GachaEntry("mawile", 10, 15,"default", 1),
                    new GachaEntry("aron", 5, 15,"default", 3),
                    new GachaEntry("meditite", 5, 15,"default", 2),
                    new GachaEntry("electrike", 5, 15,"default", 2),
                    new GachaEntry("carvanha", 5, 15,"default", 3),
                    new GachaEntry("numel", 10, 15,"default", 1),
                    new GachaEntry("swablu", 5, 15,"default", 3),
                    new GachaEntry("shuppet", 5, 15,"default", 2),
                    new GachaEntry("chingling", 10, 15,"default", 2),
                    new GachaEntry("absol", 10, 15,"default", 1),
                    new GachaEntry("snorunt", 5, 15,"default", 2),
                    new GachaEntry("buneary", 5, 15,"default", 3),
                    new GachaEntry("riolu", 10, 15,"default", 1),
                    new GachaEntry("snover", 5, 15,"default", 3),
                    new GachaEntry("drilbur", 5, 15,"default", 2),
                    new GachaEntry("audino", 10, 15,"default", 1),
                    new GachaEntry("venipede", 5, 15,"default", 2),
                    new GachaEntry("scraggy", 5, 15,"default", 2),
                    new GachaEntry("tynamo", 5, 15,"default", 2),
                    new GachaEntry("litwick", 5, 15,"default", 2),
                    new GachaEntry("litleo", 5, 15,"default", 2),
                    new GachaEntry("inkay", 5, 15,"default", 2),
                    new GachaEntry("binacle", 5, 15,"default", 2),
                    new GachaEntry("hawlucha", 10, 15,"default", 2),
                    new GachaEntry("drampa", 10, 15,"default", 2),
                    new GachaEntry("falinks", 10, 15,"default", 2)
            ));

            pools.put("uncommon", List.of(
                    new GachaEntry("caterpie", 10, 18,"default", 4),
                    new GachaEntry("meowth", 10, 18,"default", 3),
                    new GachaEntry("machop", 10, 18,"default", 4),
                    new GachaEntry("gastly", 10, 18,"default", 4),
                    new GachaEntry("krabby", 10, 18,"default", 4),
                    new GachaEntry("lapras", 15, 18,"default", 1),
                    new GachaEntry("eevee", 15, 18,"default", 1),
                    new GachaEntry("munchlax", 10, 18,"default", 3),
                    new GachaEntry("trubbish", 10, 18,"default", 3),
                    new GachaEntry("rookidee", 10, 18,"default", 4),
                    new GachaEntry("blipbug", 10, 18,"default", 3),
                    new GachaEntry("chewtle", 10, 18,"default", 3),
                    new GachaEntry("rolycoly", 10, 18,"default", 4),
                    new GachaEntry("applin", 10, 18,"default", 2),
                    new GachaEntry("silicobra", 10, 18,"default", 3),
                    new GachaEntry("toxel", 10, 18,"default", 1),
                    new GachaEntry("sizzlipede", 10, 18,"default", 3),
                    new GachaEntry("hatenna", 10, 18,"default", 2),
                    new GachaEntry("impidimp", 10, 18,"default", 2),
                    new GachaEntry("milcery", 10, 18,"default", 3),
                    new GachaEntry("cufant", 10, 18,"default", 3),
                    new GachaEntry("duraludon", 15, 18,"default", 1),
                    new GachaEntry("pinsir", 15, 18,"boosted", 2),
                    new GachaEntry("scyther", 15, 18,"boosted", 2),
                    new GachaEntry("numel", 10, 18,"boosted", 3),
                    new GachaEntry("absol", 15, 18,"boosted", 2),
                    new GachaEntry("riolu", 15, 18,"boosted", 2),
                    new GachaEntry("trapinch", 10, 18,"boosted", 3),
                    new GachaEntry("salandit", 10, 18,"boosted", 3),
                    new GachaEntry("tyrogue", 10, 18,"boosted", 4),
                    new GachaEntry("shinx", 10, 21,"boosted", 4),
                    new GachaEntry("rockruff", 10, 21,"boosted", 3),
                    new GachaEntry("foongus", 10, 21,"boosted", 3),
                    new GachaEntry("cottonee", 10, 21,"boosted", 4),
                    new GachaEntry("wingull", 10, 21,"boosted", 3),
                    new GachaEntry("bounsweet", 10, 21,"boosted", 3),
                    new GachaEntry("fletchling", 10, 21,"boosted", 4),
                    new GachaEntry("pawniard", 10, 21,"boosted", 3),
                    new GachaEntry("ponyta", 10, 18,"boosted", 4),
                    new GachaEntry("growlithe", 10, 18,"boosted", 4),
                    new GachaEntry("vulpix", 10, 18,"boosted", 4)
            ));

            pools.put("rare", List.of(
                    new GachaEntry("alolan_rattata", 20, 21,"default", 4),
                    new GachaEntry("alolan_pikachu", 20, 21,"default", 2),
                    new GachaEntry("alolan_sandshrew", 20, 21,"default", 2),
                    new GachaEntry("alolan_vulpix", 20, 21,"default", 2),
                    new GachaEntry("alolan_diglett", 20, 21,"default", 3),
                    new GachaEntry("alolan_meowth", 20, 21,"default", 3),
                    new GachaEntry("alolan_geodude", 20, 21,"default", 4),
                    new GachaEntry("alolan_grimer", 20, 21,"default", 3),
                    new GachaEntry("alolan_exeggutor", 25, 21,"default", 1),
                    new GachaEntry("alolan_marowak", 25, 21,"default", 1),
                    new GachaEntry("galarian_meowth", 20, 21,"default", 4),
                    new GachaEntry("galarian_ponyta", 20, 21,"default", 3),
                    new GachaEntry("galarian_slowpoke", 20, 21,"default", 3),
                    new GachaEntry("galarian_farfetchd", 20, 21,"default", 2),
                    new GachaEntry("galarian_weezing", 25, 21,"default", 1),
                    new GachaEntry("galarian_mrmime", 20, 21,"default", 3),
                    new GachaEntry("galarian_corsola", 20, 21,"default", 3),
                    new GachaEntry("galarian_zigzagoon", 20, 21,"default", 4),
                    new GachaEntry("galarian_darumaka", 25, 21,"default", 1),
                    new GachaEntry("galarian_yamask", 20, 21,"default", 3),
                    new GachaEntry("galarian_stunfisk", 20, 21,"default", 2),
                    new GachaEntry("hisuian_growlithe", 20, 21,"default", 4),
                    new GachaEntry("hisuian_voltorb", 20, 21,"default", 4),
                    new GachaEntry("hisuian_qwilfish", 20, 21,"default", 3),
                    new GachaEntry("hisuian_sneasel", 20, 21,"default", 3),
                    new GachaEntry("hisuian_lilligant", 25, 21,"default", 2),
                    new GachaEntry("hisuian_zorua", 25, 21,"default", 1),
                    new GachaEntry("hisuian_braviary", 25, 21,"default", 2),
                    new GachaEntry("hisuian_sliggoo", 25, 21,"default", 1),
                    new GachaEntry("hisuian_avalugg", 25, 21,"default", 2),
                    new GachaEntry("paldean_tauros", 25, 21,"default", 1),
                    new GachaEntry("paldean_wooper", 20, 21,"default", 3),
                    new GachaEntry("mimikyu", 20, 24,"boosted", 4),
                    new GachaEntry("eevee", 20, 24,"boosted", 3),
                    new GachaEntry("rotom", 20, 24,"boosted", 3),
                    new GachaEntry("toxel", 20, 24,"boosted", 4),
                    new GachaEntry("honedge", 20, 24,"boosted", 3),
                    new GachaEntry("pikachu", 20, 24,"boosted", 3),
                    new GachaEntry("zorua", 20, 24,"boosted", 2),
                    new GachaEntry("smeargle", 20, 24,"boosted", 3),
                    new GachaEntry("torkoal", 20, 24,"boosted", 2),
                    new GachaEntry("mienfoo", 20, 24,"boosted", 2),
                    new GachaEntry("tinkatink", 20, 24,"boosted", 3),
                    new GachaEntry("shellos", 20, 24,"boosted", 3),
                    new GachaEntry("gligar", 20, 24,"boosted", 4),
                    new GachaEntry("charcadet", 20, 24,"boosted", 3),
                    new GachaEntry("ferroseed", 20, 24,"boosted", 4)
            ));

            pools.put("ultrarare", List.of(
                    new GachaEntry("dratini", 30, 24,"boosted", 16),
                    new GachaEntry("larvitar", 30, 24,"boosted", 16),
                    new GachaEntry("bagon", 30, 24,"boosted", 18),
                    new GachaEntry("beldum", 30, 24,"boosted", 14),
                    new GachaEntry("gible", 30, 24,"boosted", 16),
                    new GachaEntry("deino", 30, 24,"boosted", 16),
                    new GachaEntry("goomy", 30, 24,"boosted", 16),
                    new GachaEntry("jangmoo", 30, 24,"boosted", 18),
                    new GachaEntry("dreepy", 30, 24,"boosted", 14),
                    new GachaEntry("frigibax", 30, 24,"boosted", 18),
                    new GachaEntry("omanyte", 30, 24,"boosted", 15),
                    new GachaEntry("kabuto", 30, 24,"boosted", 15),
                    new GachaEntry("aerodactyl", 30, 24,"boosted", 15),
                    new GachaEntry("lileep", 30, 24,"boosted", 17),
                    new GachaEntry("anorith", 30, 24,"boosted", 17),
                    new GachaEntry("cranidos", 30, 24,"boosted", 17),
                    new GachaEntry("shieldon", 30, 24,"boosted", 17),
                    new GachaEntry("tirtouga", 30, 24,"boosted", 17),
                    new GachaEntry("archen", 30, 24,"boosted", 17),
                    new GachaEntry("tyrunt", 30, 24,"boosted", 17),
                    new GachaEntry("amaura", 30, 24,"boosted", 17),
                    new GachaEntry("dracozolt", 30, 24,"boosted", 17),
                    new GachaEntry("dracovish", 30, 24,"boosted", 17),
                    new GachaEntry("arctozolt", 30, 24,"boosted", 17),
                    new GachaEntry("arctovish", 30, 24,"boosted", 17),
                    new GachaEntry("ditto", 35, 31,"boosted", 8),
                    new GachaEntry("porygon", 30, 27,"boosted", 10),
                    new GachaEntry("indeedee_f", 35, 27,"boosted", 12),
                    new GachaEntry("meltan", 50, 31,"boosted", 6),
                    new GachaEntry("larvesta", 35, 27,"boosted", 12),
                    new GachaEntry("eevee", 50, 31,"yes", 1),
                    new GachaEntry("ditto", 50, 31,"yes", 1),
                    new GachaEntry("greattusk", 50, 31,"default", 4),
                    new GachaEntry("screamtail", 50, 31,"default", 4),
                    new GachaEntry("brutebonnet", 50, 31,"default", 4),
                    new GachaEntry("fluttermane", 50, 31,"default", 4),
                    new GachaEntry("slitherwing", 50, 31,"default", 4),
                    new GachaEntry("sandyshocks", 50, 31,"default", 4),
                    new GachaEntry("irontreads", 50, 31,"default", 4),
                    new GachaEntry("ironbundle", 50, 31,"default", 4),
                    new GachaEntry("ironhands", 50, 31,"default", 4),
                    new GachaEntry("ironjugulis", 50, 31,"default", 4),
                    new GachaEntry("ironmoth", 50, 31,"default", 4),
                    new GachaEntry("ironthorns", 50, 31,"default", 4),
                    new GachaEntry("feebas", 50, 31,"boosted", 7)
            ));

            pools.put("legendary", List.of(
                    new GachaEntry("bulbasaur", 10, 31,"boosted", 48),
                    new GachaEntry("charmander", 10, 31,"boosted", 48),
                    new GachaEntry("squirtle", 10, 31,"boosted", 48),
                    new GachaEntry("chikorita", 10, 31,"boosted", 48),
                    new GachaEntry("cyndaquil", 10, 31,"boosted", 48),
                    new GachaEntry("totodile", 10, 31,"boosted", 48),
                    new GachaEntry("treecko", 10, 31,"boosted", 48),
                    new GachaEntry("torchic", 10, 31,"boosted", 48),
                    new GachaEntry("totodile", 10, 31,"boosted", 48),
                    new GachaEntry("snivy", 10, 31,"boosted", 48),
                    new GachaEntry("tepig", 10, 31,"boosted", 48),
                    new GachaEntry("oshawott", 10, 31,"boosted", 48),
                    new GachaEntry("chespin", 10, 31,"boosted", 48),
                    new GachaEntry("fennekin", 10, 31,"boosted", 48),
                    new GachaEntry("froakie", 10, 31,"boosted", 48),
                    new GachaEntry("rowlet", 10, 31,"boosted", 48),
                    new GachaEntry("litten", 10, 31,"boosted", 48),
                    new GachaEntry("popplio", 10, 31,"boosted", 48),
                    new GachaEntry("grookey", 10, 31,"boosted", 48),
                    new GachaEntry("scorbunny", 10, 31,"boosted", 48),
                    new GachaEntry("sobble", 10, 31,"boosted", 48),
                    new GachaEntry("sprigatito", 10, 31,"boosted", 48),
                    new GachaEntry("fuecoco", 10, 31,"boosted", 48),
                    new GachaEntry("quaxly", 10, 31,"boosted", 48),
                    new GachaEntry("roaringmoon", 50, 31,"default", 32),
                    new GachaEntry("ironvaliant", 50, 31,"default", 32),
                    new GachaEntry("articuno", 75, 31,"default", 16),
                    new GachaEntry("zapdos", 75, 31,"default", 16),
                    new GachaEntry("moltres", 75, 31,"default", 16),
                    new GachaEntry("raikou", 75, 31,"default", 16),
                    new GachaEntry("entei", 75, 31,"default", 16),
                    new GachaEntry("suicune", 75, 31,"default", 16),
                    new GachaEntry("regirock", 75, 31,"default", 16),
                    new GachaEntry("regice", 75, 31,"default", 16),
                    new GachaEntry("registeel", 75, 31,"default", 16),
                    new GachaEntry("regieleki", 75, 31,"default", 16),
                    new GachaEntry("regidrago", 75, 31,"default", 16),
                    new GachaEntry("regigigas", 75, 31,"default", 16),
                    new GachaEntry("heatran", 75, 31,"default", 16),
                    new GachaEntry("cresselia", 75, 31,"default", 16),
                    new GachaEntry("tapukoko", 75, 31,"default", 16),
                    new GachaEntry("tapulele", 75, 31,"default", 16),
                    new GachaEntry("tapubulu", 75, 31,"default", 16),
                    new GachaEntry("tapufini", 75, 31,"default", 16),
                    new GachaEntry("magikarp", 100, 31,"yes", 8),
                    new GachaEntry("gholdengo", 50, 31,"default", 32),
                    new GachaEntry("netherite_gholdengo", 100, 31,"boosted", 1),
                    new GachaEntry("mew", 100, 31,"yes", 8),
                    new GachaEntry("diancie", 100, 31,"yes", 1)
            ));

            pools.put("bonus", List.of(
                    new GachaEntry("pidgey", 10, 15,"default", 4),
                    new GachaEntry("spearow", 10, 15,"default", 4),
                    new GachaEntry("hoothoot", 10, 15,"default", 4),
                    new GachaEntry("taillow", 10, 15,"default", 4),
                    new GachaEntry("starly", 10, 15,"default", 4),
                    new GachaEntry("pidove", 10, 15,"default", 4),
                    new GachaEntry("fletchling", 10, 15,"default", 4),
                    new GachaEntry("pikipek", 10, 15,"default", 4),
                    new GachaEntry("rookidee", 10, 15,"default", 4),
                    new GachaEntry("flamigo", 10, 15,"default", 4),
                    new GachaEntry("rattata", 10, 15,"default", 5),
                    new GachaEntry("sentret", 10, 15,"default", 5),
                    new GachaEntry("zigzagoon", 10, 15,"default", 5),
                    new GachaEntry("bidoof", 10, 15,"default", 5),
                    new GachaEntry("lechonk", 10, 15,"default", 5),
                    new GachaEntry("bunnelby", 10, 15,"default", 5),
                    new GachaEntry("yungoos", 10, 15,"default", 5),
                    new GachaEntry("skwovet", 10, 15,"default", 5),
                    new GachaEntry("lechonk", 10, 15,"default", 5),
                    new GachaEntry("abra", 10, 15,"default", 3),
                    new GachaEntry("ralts", 10, 15,"default", 3),
                    new GachaEntry("gastly", 10, 15,"default", 3),
                    new GachaEntry("geodude", 10, 15,"default", 3),
                    new GachaEntry("vullaby", 10, 15,"default", 3),
                    new GachaEntry("wooper", 10, 15,"default", 3),
                    new GachaEntry("sandile", 10, 15,"default", 3),
                    new GachaEntry("golett", 10, 15,"default", 3),
                    new GachaEntry("slugma", 10, 15,"default", 3),
                    new GachaEntry("litwick", 10, 15,"default", 3),
                    new GachaEntry("bellsprout", 10, 15,"default", 3),
                    new GachaEntry("staryu", 10, 15,"default", 3),
                    new GachaEntry("salandit", 10, 15,"default", 3),
                    new GachaEntry("rolycoly", 10, 15,"default", 3),
                    new GachaEntry("goldeen", 10, 15,"default", 3),
                    new GachaEntry("psyduck", 10, 15,"default", 3),
                    new GachaEntry("lotad", 10, 15,"default", 3),
                    new GachaEntry("seedot", 10, 15,"default", 3),
                    new GachaEntry("gossifleur", 10, 15,"default", 3),
                    new GachaEntry("mareep", 10, 15,"default", 6),
                    new GachaEntry("wooloo", 10, 15,"default", 6),
                    new GachaEntry("klink", 10, 15,"default", 4),
                    new GachaEntry("spheal", 10, 15,"default", 3),
                    new GachaEntry("spiritomb", 10, 15,"default", 1)
            ));
        }

        for (List<GachaEntry> entries : pools.values()) {
            for (GachaEntry entry : entries) {
                entry.validate();
            }
        }

    }

}