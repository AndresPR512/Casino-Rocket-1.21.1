package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Config(name = "item_gachapon")
public class ItemGachaponConfig implements ConfigData {

    @CollapsibleObject
    public Map<String, List<GachaEntry>> pools = new LinkedHashMap<>();

    public static class GachaEntry {
        public String itemId;
        public int weight;
        public int count;

        public GachaEntry() {}
        public GachaEntry(String itemId, int weight, int count) {
            this.itemId = itemId;
            this.weight = weight;
            this.count = count;
        }
    }

    @Override
    public void validatePostLoad() {
        if (pools.isEmpty()) {

            pools.put("common", List.of(
                    new GachaEntry("cobblemon:poke_ball", 10, 10),
                    new GachaEntry("cobblemon:potion", 8, 2),
                    new GachaEntry("cobblemon:antidote", 6, 3),
                    new GachaEntry("cobblemon:paralyze_heal", 5, 3),
                    new GachaEntry("cobblemon:revive", 2, 1),
                    new GachaEntry("minecraft:iron_ingot", 3, 4),
                    new GachaEntry("minecraft:coal", 3, 8)
            ));

            pools.put("uncommon", List.of(
                    new GachaEntry("cobblemon:great_ball", 10, 5),
                    new GachaEntry("minecraft:iron_ingot", 5, 6),
                    new GachaEntry("cobblemon:quick_ball", 2, 3)
            ));

            pools.put("rare", List.of(
                    new GachaEntry("cobblemon:ultra_ball", 6, 10),
                    new GachaEntry("cobblemon:full_heal", 5, 2),
                    new GachaEntry("cobblemon:super_potion", 5, 2),
                    new GachaEntry("cobblemon:revive", 4, 1),
                    new GachaEntry("cobblemon:sitrus_berry", 3, 5),
                    new GachaEntry("minecraft:gold_ingot", 2, 2),
                    new GachaEntry("cobblemon:dusk_ball", 2, 2)
            ));

            pools.put("ultrarare", List.of(
                    new GachaEntry("cobblemon:quick_ball", 6, 5),
                    new GachaEntry("cobblemon:rare_candy", 5, 1),
                    new GachaEntry("cobblemon:exp_candy_m", 4, 3),
                    new GachaEntry("cobblemon:dawn_stone", 3, 1),
                    new GachaEntry("cobblemon:dusk_stone", 3, 1),
                    new GachaEntry("cobblemon:fire_stone", 3, 1),
                    new GachaEntry("cobblemon:water_stone", 3, 1),
                    new GachaEntry("minecraft:diamond", 2, 3),
                    new GachaEntry("cobblemon:exp_share", 1, 1)
            ));

            pools.put("legendary", List.of(
                    new GachaEntry("cobblemon:master_ball", 1, 1),
                    new GachaEntry("cobblemon:cherish_ball", 1, 1),
                    new GachaEntry("cobblemon:exp_candy_l", 3, 3),
                    new GachaEntry("cobblemon:exp_candy_xl", 2, 2),
                    new GachaEntry("cobblemon:shiny_stone", 3, 1),
                    new GachaEntry("cobblemon:ability_patch", 2, 1),
                    new GachaEntry("minecraft:netherite_ingot", 1, 1)
            ));

            pools.put("bonus", List.of(
                    new GachaEntry("cobblemon:premier_ball", 6, 6),
                    new GachaEntry("minecraft:iron_ingot", 3, 8),
                    new GachaEntry("cobblemon:super_potion", 4, 6),
                    new GachaEntry("cobblemon:exp_candy_m", 1, 2)
            ));

        }
    }

}