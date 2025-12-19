package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(name = "plushies_gachapon")
public class PlushiesGachaponConfig implements ConfigData {

    public List<GachaEntry> plushies = defaultPlushies();

    public static class GachaEntry {
        public String itemId;
        public int weight;

        public GachaEntry() {}

        public GachaEntry(String itemId, int weight) {
            this.itemId = itemId;
            this.weight = weight;
        }
    }

    // === DEFAULT VALUES ===
    private static List<GachaEntry> defaultPlushies() {
        List<GachaEntry> list = new ArrayList<>();

        list.add(new GachaEntry("", 1));

        return list;
    }

}