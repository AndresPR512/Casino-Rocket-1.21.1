package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.NbtList;

public final class GachaDealerShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        NbtList shops = new NbtList();

        // ===== COINS =====
        NbtList coinOffers = new NbtList();
        VillagerTradeHelper.makeCoinOffer(coinOffers, ModItems.COPPER_COIN);
        VillagerTradeHelper.makeCoinOffer(coinOffers, ModItems.IRON_COIN);
        VillagerTradeHelper.makeCoinOffer(coinOffers, ModItems.GOLD_COIN);
        VillagerTradeHelper.makeCoinOffer(coinOffers, ModItems.DIAMOND_COIN);
        shops.add(VillagerTradeHelper.makeShopCompound("Gacha Coins", coinOffers));

        if (CasinoRocket.CONFIG.gachaMachines.gacha_store.gachapon_store.enableItemGachaponStore) {
            NbtList ItemGachaponOffers = new NbtList();
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.POKE_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.GREAT_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.ULTRA_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.MASTER_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.CHERISH_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.PREMIER_GACHAPON);
            shops.add(VillagerTradeHelper.makeShopCompound("Item Gacha", ItemGachaponOffers));
        }
        if (CasinoRocket.CONFIG.gachaMachines.gacha_store.gachapon_store.enablePokemonGachaponStore) {
            NbtList PokemonGachaponOffers = new NbtList();
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_POKE_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_GREAT_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_ULTRA_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_MASTER_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_CHERISH_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_PREMIER_GACHAPON);
            shops.add(VillagerTradeHelper.makeShopCompound("Pokémon Gacha", PokemonGachaponOffers));
        }

        return new VillagerTradeHelper.ShopData(shops,"cobbledollars:cobble_merchant",3, "cobblemon:display_case");

    }

    @Override
    public String getName() {
        return "Mr. Lucky";
    }

}