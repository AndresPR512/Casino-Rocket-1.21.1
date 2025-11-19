package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.NbtList;

import java.util.List;

public final class BattleGearShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        NbtList shops = new NbtList();

        // ===== BATTLE ITEMS =====
        NbtList battleOffers = new NbtList();
        battleOffers.add(VillagerTradeHelper.makeOffer("cobblemon:sitrus_berry","500"));
        battleOffers.add(VillagerTradeHelper.makeOffer("cobblemon:muscle_band","2500"));
        battleOffers.add(VillagerTradeHelper.makeOffer("cobblemon:wise_glasses","2500"));
        battleOffers.add(VillagerTradeHelper.makeOffer("cobblemon:metronome","5000"));
        battleOffers.add(VillagerTradeHelper.makeOffer("cobblemon:shell_bell","7500"));
        battleOffers.add(VillagerTradeHelper.makeOffer("cobblemon:focus_band","10000"));
        battleOffers.add(VillagerTradeHelper.makeOffer("cobblemon:scope_lens","10000"));
        battleOffers.add(VillagerTradeHelper.makeOffer("cobblemon:quick_claw","10000"));
        battleOffers.add(VillagerTradeHelper.makeOffer("cobblemon:eviolite","20000"));
        shops.add(VillagerTradeHelper.makeShopCompound("Battle Items", battleOffers));

        // ===== TYPE BOOST ITEMS =====
        NbtList typeBoostOffers = new NbtList();
        List<String> typeBoostItems = List.of(
                "silk_scarf",
                "charcoal_stick",
                "mystic_water",
                "miracle_seed",
                "magnet",
                "never_melt_ice",
                "hard_stone",
                "sharp_beak",
                "black_belt",
                "soft_sand",
                "silver_powder",
                "poison_barb",
                "twisted_spoon",
                "spell_tag",
                "black_glasses",
                "metal_coat",
                "dragon_fang",
                "fairy_feather"
        );
        VillagerTradeHelper.makeListOffer(typeBoostOffers, typeBoostItems, "cobblemon", "10000");
        shops.add(VillagerTradeHelper.makeShopCompound("Type Boost", typeBoostOffers));

        return new VillagerTradeHelper.ShopData(shops,"cobbledollars:cobble_merchant",1, "cobblemon:display_case");

    }

    @Override
    public String getName() {
        return "Battle Gear";
    }

}