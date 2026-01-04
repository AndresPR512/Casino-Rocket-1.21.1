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
                "salty_boiled_egg_curry", // Normal
                "drought_katsu_curry", // Fire
                "bitter_leek_curry", // Water
                "spicy_mushroom_medley_curry", // Grass
                "zing_zap_curry", // Electric
                "dry_frozen_curry", // Ice
                "dry_curry", // Fight
                "bitter_herb_medley_curry", // Poison
                "dry_bone_curry", // Ground
                "bean_medley_curry", // Flying
                "dry_smoked_tail_curry", // Psychic
                "mild_honey_curry", // Bug
                "beanburger_curry", // Rock
                "dream_eater_butter_curry", // Ghost
                "sweet_apple_curry", // Dragon
                "ninja_curry", // Dark
                "spicy_potato_curry", // Steel
                "sweet_whipped_cream_curry" // Fairy
        );
        VillagerTradeHelper.makeListOffer(curryOffers, curries, "cobblecuisine", "5000");
        shops.add(VillagerTradeHelper.makeShopCompound("Curries", curryOffers));

        // ===== INGREDIENTS =====
        NbtList ingredientOffers = new NbtList();
        ingredientOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:red_bean","500"));
        ingredientOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:blue_bean","500"));
        ingredientOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:orange_bean","500"));
        ingredientOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:green_bean","500"));
        ingredientOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:yellow_bean","500"));
        ingredientOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:violet_bean","500"));
        ingredientOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:indigo_bean","500"));
        ingredientOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:spice_mix","1000"));
        ingredientOffers.add(VillagerTradeHelper.makeOffer("cobblemon:galarica_nuts","2000"));
        shops.add(VillagerTradeHelper.makeShopCompound("Ingredients", ingredientOffers));

        // ===== SPECIAL FOOD =====
        NbtList specialFoodOffers = new NbtList();
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:dubious_food", "5000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:candied_razz_berry", "10000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:candied_pinap_berry", "10000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:ceviche", "10000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:fruity_flan", "25000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:eclair", "25000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:candied_bluk_berry", "50000"));
        specialFoodOffers.add(VillagerTradeHelper.makeOffer("cobblecuisine:candied_nanab_berry", "1000000"));
        shops.add(VillagerTradeHelper.makeShopCompound("Special Food", specialFoodOffers));

        return new VillagerTradeHelper.ShopData(shops,"cobbledollars:cobble_merchant",1, "cobblemon:display_case");

    }

    @Override
    public String getName() {
        return "Snackmaster";
    }

}