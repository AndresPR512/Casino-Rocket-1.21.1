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
        public GachaEntry(String itemId, int count, int weight) {
            this.itemId = itemId;
            this.count = count;
            this.weight = weight;
        }
    }

    @Override
    public void validatePostLoad() {
        if (pools.isEmpty()) {

            pools.put("common", List.of(
                    new GachaEntry("cobblemon:poke_ball", 8, 12),
                    new GachaEntry("cobblemon:citrine_ball", 4, 6),
                    new GachaEntry("cobblemon:verdant_ball", 4, 6),
                    new GachaEntry("cobblemon:azure_ball", 4, 6),
                    new GachaEntry("cobblemon:roseate_ball", 4, 6),
                    new GachaEntry("cobblemon:slate_ball", 4, 6),
                    new GachaEntry("cobblemon:heal_ball", 4, 6),
                    new GachaEntry("cobblemon:great_ball", 4, 4),
                    new GachaEntry("cobblemon:ultra_ball", 2, 2),
                    new GachaEntry("cobblemon:exp_candy_xs", 4, 8),
                    new GachaEntry("cobblemon:potion", 4, 10),
                    new GachaEntry("cobblemon:super_potion", 2, 6),
                    new GachaEntry("cobblemon:oran_berry", 4, 8),
                    new GachaEntry("cobblemon:sitrus_berry", 2, 4),
                    new GachaEntry("cobblemon:lum_berry", 2, 4),
                    new GachaEntry("cobblemon:galarica_nuts", 3, 4),
                    new GachaEntry("casinorocket:basic_chip", 5, 4),
                    new GachaEntry("casinorocket:copper_chip", 1, 1),
                    new GachaEntry("minecraft:coal", 16, 8),
                    new GachaEntry("minecraft:iron_ingot", 8, 6),
                    new GachaEntry("minecraft:copper_ingot", 12, 6),
                    new GachaEntry("minecraft:gold_ingot", 4, 2),
                    new GachaEntry("minecraft:leather", 4, 5),
                    new GachaEntry("minecraft:bone", 4, 5),
                    new GachaEntry("minecraft:gunpowder", 4, 5)
            ));

            pools.put("uncommon", List.of(
                    new GachaEntry("cobblemon:great_ball", 8, 16),
                    new GachaEntry("cobblemon:ultra_ball", 4, 12),
                    new GachaEntry("cobblemon:quick_ball", 2, 8),
                    new GachaEntry("cobblemon:timer_ball", 2, 8),
                    new GachaEntry("cobblemon:luxury_ball", 2, 8),
                    new GachaEntry("cobblemon:dream_ball", 2, 8),
                    new GachaEntry("cobblemon:repeat_ball", 2, 8),
                    new GachaEntry("cobblemon:exp_candy_xs", 8, 12),
                    new GachaEntry("cobblemon:exp_candy_s", 4, 8),
                    new GachaEntry("cobblemon:rare_candy", 1, 2),
                    new GachaEntry("cobblecuisine:bitter_leek_curry", 3, 6),
                    new GachaEntry("cobblecuisine:dry_frozen_curry", 3, 6),
                    new GachaEntry("cobblecuisine:salty_boiled_egg_curry", 3, 6),
                    new GachaEntry("cobblecuisine:bitter_herb_medley_curry", 3, 6),
                    new GachaEntry("cobblecuisine:bitter_leek_curry", 3, 6),
                    new GachaEntry("cobblecuisine:dry_bone_curry", 3, 6),
                    new GachaEntry("cobblecuisine:spicy_mushroom_medley_curry", 3, 6),
                    new GachaEntry("cobblecuisine:sweet_apple_curry", 3, 6),
                    new GachaEntry("cobblecuisine:spicy_potato_curry", 3, 6),
                    new GachaEntry("cobblecuisine:sweet_whipped_cream_curry", 3, 6),
                    new GachaEntry("cobblecuisine:sour_pickle_sandwich", 3, 6),
                    new GachaEntry("cobblecuisine:spicy_five_alarm_sandwich", 3, 6),
                    new GachaEntry("cobblecuisine:salty_vegetable_sandwich", 3, 6),
                    new GachaEntry("cobblecuisine:sour_zesty_sandwich", 3, 6),
                    new GachaEntry("cobblecuisine:sweet_potato_salad_sandwich", 3, 6),
                    new GachaEntry("cobblecuisine:sweet_jam_sandwich", 3, 6),
                    new GachaEntry("cobblecuisine:spicy_claw_sandwich", 3, 6),
                    new GachaEntry("cobblecuisine:bitter_jambon_beurre", 3, 6),
                    new GachaEntry("cobblecuisine:salty_egg_sandwich", 3, 6),
                    new GachaEntry("cobblemon:super_potion", 4, 12),
                    new GachaEntry("cobblemon:hyper_potion", 2, 8),
                    new GachaEntry("cobblemon:revive", 1, 4),
                    new GachaEntry("cobblemon:sitrus_berry", 4, 8),
                    new GachaEntry("cobblemon:lum_berry", 4, 4),
                    new GachaEntry("cobblemon:leppa_berry", 2, 4),
                    new GachaEntry("casinorocket:copper_chip", 5, 2),
                    new GachaEntry("casinorocket:iron_chip", 1, 1),
                    new GachaEntry("minecraft:iron_ingot", 12, 12),
                    new GachaEntry("minecraft:copper_ingot", 16, 12),
                    new GachaEntry("minecraft:gold_ingot", 8, 8),
                    new GachaEntry("minecraft:leather", 8, 4),
                    new GachaEntry("minecraft:bone", 8, 4),
                    new GachaEntry("minecraft:gunpowder", 8, 4),
                    new GachaEntry("minecraft:blaze_rod", 2, 3),
                    new GachaEntry("minecraft:ender_pearl", 2, 2)
            ));

            pools.put("rare", List.of(
                    new GachaEntry("cobblemon:ultra_ball", 12, 16),
                    new GachaEntry("cobblemon:quick_ball", 8, 12),
                    new GachaEntry("cobblemon:timer_ball", 8, 12),
                    new GachaEntry("cobblemon:luxury_ball", 8, 12),
                    new GachaEntry("cobblemon:dream_ball", 8, 12),
                    new GachaEntry("cobblemon:repeat_ball", 8, 12),
                    new GachaEntry("cobblemon:exp_candy_s", 6, 14),
                    new GachaEntry("cobblemon:exp_candy_l", 2, 8),
                    new GachaEntry("cobblemon:rare_candy", 2, 6),
                    new GachaEntry("cobblecuisine:dry_curry", 3, 8),
                    new GachaEntry("cobblemon:hp_up", 4, 6),
                    new GachaEntry("cobblemon:protein", 4, 6),
                    new GachaEntry("cobblemon:iron", 4, 6),
                    new GachaEntry("cobblemon:calcium", 4, 6),
                    new GachaEntry("cobblemon:zinc", 4, 6),
                    new GachaEntry("cobblemon:carbos", 4, 6),
                    new GachaEntry("cobblemon:pp_max", 4, 6),
                    new GachaEntry("cobblemon:hyper_potion", 4, 12),
                    new GachaEntry("cobblemon:max_potion", 2, 9),
                    new GachaEntry("cobblemon:full_heal", 2, 9),
                    new GachaEntry("cobblemon:revive", 2, 9),
                    new GachaEntry("cobblemon:max_revive", 1, 7),
                    new GachaEntry("cobblemon:fire_stone", 3, 8),
                    new GachaEntry("cobblemon:water_stone", 3, 8),
                    new GachaEntry("cobblemon:thunder_stone", 3, 8),
                    new GachaEntry("cobblemon:leaf_stone", 3, 8),
                    new GachaEntry("cobblemon:moon_stone", 3, 8),
                    new GachaEntry("cobblemon:sun_stone", 3, 8),
                    new GachaEntry("cobblemon:shiny_stone", 3, 8),
                    new GachaEntry("cobblemon:dusk_stone", 3, 8),
                    new GachaEntry("cobblemon:dawn_stone", 3, 8),
                    new GachaEntry("cobblemon:ice_stone", 3, 8),
                    new GachaEntry("cobblemon:pomeg_berry", 4, 6),
                    new GachaEntry("cobblemon:kelpsy_berry", 4, 6),
                    new GachaEntry("cobblemon:qualot_berry", 4, 6),
                    new GachaEntry("cobblemon:hondew_berry", 4, 6),
                    new GachaEntry("cobblemon:grepa_berry", 4, 6),
                    new GachaEntry("cobblemon:tamato_berry", 4, 6),
                    new GachaEntry("cobblemon:leppa_berry", 4, 8),
                    new GachaEntry("casinorocket:iron_chip", 5, 4),
                    new GachaEntry("casinorocket:amethyst_chip", 1, 1),
                    new GachaEntry("mega_showdown:max_mushroom", 2, 2),
                    new GachaEntry("minecraft:gold_ingot", 12, 12),
                    new GachaEntry("minecraft:blaze_rod", 4, 8),
                    new GachaEntry("minecraft:ender_pearl", 4, 4),
                    new GachaEntry("minecraft:dragon_breath", 2, 2)
            ));

            pools.put("ultrarare", List.of(
                    new GachaEntry("cobblemon:beast_ball", 4, 12),
                    new GachaEntry("cobblemon:exp_candy_l", 4, 16),
                    new GachaEntry("cobblemon:exp_candy_xl", 2, 12),
                    new GachaEntry("cobblemon:rare_candy", 4, 10),
                    new GachaEntry("cobblemon:ability_capsule", 2, 8),
                    new GachaEntry("cobblemon:ability_patch", 1, 6),
                    new GachaEntry("cobblemon:full_restore", 4, 16),
                    new GachaEntry("cobblemon:max_revive", 2, 16),
                    new GachaEntry("casinorocket:amethyst_chip", 5, 3),
                    new GachaEntry("casinorocket:gold_chip", 1, 1),
                    new GachaEntry("cobblemon:hp_up", 8, 12),
                    new GachaEntry("cobblemon:protein", 8, 12),
                    new GachaEntry("cobblemon:iron", 8, 12),
                    new GachaEntry("cobblemon:calcium", 8, 12),
                    new GachaEntry("cobblemon:zinc", 8, 12),
                    new GachaEntry("cobblemon:carbos", 8, 12),
                    new GachaEntry("cobblemon:pp_max", 8, 12),
                    new GachaEntry("cobblemon:life_orb", 1, 7),
                    new GachaEntry("cobblemon:choice_band", 1, 7),
                    new GachaEntry("cobblemon:choice_scarf", 1, 7),
                    new GachaEntry("cobblemon:choice_specs", 1, 7),
                    new GachaEntry("cobblemon:leftovers", 2, 8),
                    new GachaEntry("cobblemon:focus_sash", 2, 8),
                    new GachaEntry("cobblemon:assault_vest", 1, 8),
                    new GachaEntry("cobblemon:rocky_helmet", 1, 8),
                    new GachaEntry("cobblemon:weakness_policy", 1, 8),
                    new GachaEntry("mega_showdown:mega_stone", 1, 4),
                    new GachaEntry("mega_showdown:blank-z", 1, 4),
                    new GachaEntry("mega_showdown:dynamax_candy", 2, 4),
                    new GachaEntry("mega_showdown:tera_orb", 1, 4),
                    new GachaEntry("minecraft:totem_of_undying", 1, 6),
                    new GachaEntry("minecraft:shulker_shell", 4, 8),
                    new GachaEntry("minecraft:enchanted_golden_apple", 2, 6)
            ));

            pools.put("legendary", List.of(
                    new GachaEntry("cobblemon:master_ball", 1, 8),
                    new GachaEntry("cobblemon:cherish_ball", 1, 7),
                    new GachaEntry("cobblemon:exp_candy_xl", 8, 6),
                    new GachaEntry("cobblemon:rare_candy", 6, 6),
                    new GachaEntry("cobblecuisine:bean_medley_curry", 3, 4),
                    new GachaEntry("casinorocket:gold_chip", 5, 2),
                    new GachaEntry("mega_showdown:keystone", 1, 2),
                    new GachaEntry("mega_showdown:sparkling_stone_light", 1, 1),
                    new GachaEntry("mega_showdown:sparkling_stone_dark", 1, 1),
                    new GachaEntry("mega_showdown:wishing_star", 1, 2),
                    new GachaEntry("minecraft:nether_star", 2, 3),
                    new GachaEntry("minecraft:netherite_ingot", 2, 3),
                    new GachaEntry("minecraft:diamond", 8, 6),
                    new GachaEntry("brendons_bottlecaps:goldencap", 1, 4),
                    new GachaEntry("minecraft:heavy_core", 1, 2),
                    new GachaEntry("minecraft:trident", 1, 2)
            ));

            pools.put("bonus", List.of(
                    new GachaEntry("cobblemon:premier_ball", 8, 12),
                    new GachaEntry("cobblemon:safari_ball", 4, 8),
                    new GachaEntry("cobblemon:fast_ball", 4, 8),
                    new GachaEntry("cobblemon:level_ball", 4, 8),
                    new GachaEntry("cobblemon:lure_ball", 4, 8),
                    new GachaEntry("cobblemon:heavy_ball", 4, 8),
                    new GachaEntry("cobblemon:love_ball", 4, 8),
                    new GachaEntry("cobblemon:friend_ball", 4, 8),
                    new GachaEntry("cobblemon:moon_ball", 4, 8),
                    new GachaEntry("cobblemon:sport_ball", 4, 8),
                    new GachaEntry("cobblemon:park_ball", 4, 8),
                    new GachaEntry("cobblemon:net_ball", 4, 8),
                    new GachaEntry("cobblemon:dive_ball", 4, 8),
                    new GachaEntry("cobblemon:nest_ball", 4, 8),
                    new GachaEntry("cobblemon:exp_candy_xs", 8, 10),
                    new GachaEntry("cobblemon:exp_candy_s", 4, 6),
                    new GachaEntry("cobblemon:exp_candy_l", 2, 4),
                    new GachaEntry("cobblemon:rare_candy", 2, 3),
                    new GachaEntry("cobblemon:pp_max", 2, 3),
                    new GachaEntry("cobblemon:revive", 1, 5),
                    new GachaEntry("casinorocket:iron_chip", 5, 3),
                    new GachaEntry("casinorocket:amethyst_chip", 1, 1),
                    new GachaEntry("minecraft:iron_ingot", 12, 12),
                    new GachaEntry("minecraft:copper_ingot", 16, 12),
                    new GachaEntry("minecraft:gold_ingot", 8, 10),
                    new GachaEntry("minecraft:leather", 8, 6),
                    new GachaEntry("minecraft:bone", 8, 6),
                    new GachaEntry("minecraft:gunpowder", 8, 6),
                    new GachaEntry("minecraft:blaze_rod", 2, 4),
                    new GachaEntry("minecraft:ender_pearl", 2, 4),
                    new GachaEntry("casinorocket:copper_coin", 3, 16),
                    new GachaEntry("casinorocket:iron_coin", 2, 8),
                    new GachaEntry("casinorocket:gold_coin", 1, 2),
                    new GachaEntry("casinorocket:diamond_coin", 1, 1)
            ));

        }
    }

}