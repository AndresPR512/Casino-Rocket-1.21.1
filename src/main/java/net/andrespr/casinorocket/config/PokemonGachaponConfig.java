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
        public int weight;
        public int level;
        public int ivs;
        public boolean shiny;

        public GachaEntry() {}
        public GachaEntry(String pokemonId, int weight, int level, int ivs, boolean shiny) {
            this.pokemonId = pokemonId;
            this.weight = weight;
            this.level = level;
            this.ivs = ivs;
            this.shiny = shiny;
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
                    new GachaEntry("caterpie",8, 10, 15, false),
                    new GachaEntry("abra",6, 10, 15, false),
                    new GachaEntry("ralts",4, 10, 15, false),
                    new GachaEntry("lechonk",12, 5, 15, false),
                    new GachaEntry("charmander",1, 5, 21, false)
            ));

            pools.put("uncommon", List.of(
                    new GachaEntry("rotom", 8, 20,  21, false),
                    new GachaEntry("eevee", 12, 20,  21, false),
                    new GachaEntry("dratini", 8, 20,  21, false),
                    new GachaEntry("munchlax", 4, 20,  21, false),
                    new GachaEntry("lapras", 1, 30,  31, true)
            ));

            pools.put("rare", List.of(
                    new GachaEntry("gible", 8, 30,  21, false),
                    new GachaEntry("feebas", 12, 30,  21, false),
                    new GachaEntry("deino", 8, 30,  21, false),
                    new GachaEntry("goomy", 4, 30,  21, false),
                    new GachaEntry("ditto", 1, 50,  31, true)
            ));

            pools.put("ultrarare", List.of(
                    new GachaEntry("pikachu", 8, 50,  21, false),
                    new GachaEntry("squirtle", 4, 50,  21, false),
                    new GachaEntry("bulbasaur", 4, 50,  21, false),
                    new GachaEntry("charmander", 4, 50,  21, false),
                    new GachaEntry("mew", 1, 50,  31, true)
            ));

            pools.put("legendary", List.of(
                    new GachaEntry("magikarp", 8, 100,  31, true),
                    new GachaEntry("deoxys", 2, 100,  31, false),
                    new GachaEntry("zacian", 2, 100,  31, false),
                    new GachaEntry("cosmog", 4, 100,  31, false),
                    new GachaEntry("rayquaza", 1, 100,  31, false),
                    new GachaEntry("arceus", 1, 100,  31, true)
            ));

            pools.put("bonus", List.of(
                    new GachaEntry("spheal", 6, 5, 15, false),
                    new GachaEntry("vulpix", 3, 5, 15, false),
                    new GachaEntry("growlithe", 4, 5, 15, false),
                    new GachaEntry("riolu", 1, 5, 15, false)
            ));

        }

        for (List<GachaEntry> entries : pools.values()) {
            for (GachaEntry entry : entries) {
                entry.validate();
            }
        }

    }

}