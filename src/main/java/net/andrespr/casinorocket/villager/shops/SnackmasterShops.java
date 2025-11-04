package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.NbtList;

import java.util.List;

public final class SnackmasterShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {
        NbtList shops = new NbtList();

        // ===== CURRIES =====
        NbtList curryOffers = new NbtList();
        List<String> curries = List.of(
                "bitter_leek_curry",
                "dry_frozen_curry",
                "salty_boiled_egg_curry",
                "bitter_herb_medley_curry",
                "bitter_herb_medley_curry",
                "dry_bone_curry",
                "spicy_mushroom_medley_curry",
                "sweet_apple_curry",
                "spicy_potato_curry",
                "sweet_whipped_cream_curry"
        );
        VillagerTradeHelper.makeListOffer(curryOffers, curries, "cobblecuisine", "10000");
        shops.add(VillagerTradeHelper.makeShopCompound("Curries", curryOffers));

        // ===== SANDWICHES =====
        NbtList sandwichOffers = new NbtList();
        List<String> sandwiches = List.of(
                "sour_pickle_sandwich",
                "spicy_five_alarm_sandwich",
                "salty_vegetable_sandwich",
                "sour_zesty_sandwich",
                "sweet_potato_salad_sandwich",
                "sweet_jam_sandwich",
                "spicy_claw_sandwich",
                "bitter_jambon_beurre",
                "salty_egg_sandwich"
        );
        VillagerTradeHelper.makeListOffer(sandwichOffers, sandwiches, "cobblecuisine", "10000");
        shops.add(VillagerTradeHelper.makeShopCompound("Sandwiches", sandwichOffers));

        // ===== SPECIAL FOOD =====
        NbtList specialFoodOffers = new NbtList();
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:dry_curry", "25000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:fruity_flan", "50000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:eclair", "50000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:dry_smoked_tail_curry", "100000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:bean_medley_curry", "1000000"));
        shops.add(VillagerTradeHelper.makeShopCompound("Special Food", specialFoodOffers));

        return new VillagerTradeHelper.ShopData(shops,"cobbledollars:cobble_merchant",1, "cobblemon:display_case");

    }

    @Override
    public String getName() {
        return "Snackmaster";
    }

}