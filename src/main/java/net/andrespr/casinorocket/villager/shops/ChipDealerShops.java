package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.NbtList;

public final class ChipDealerShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        NbtList shops = new NbtList();

        // ===== MONEY TO CHIPS =====
        NbtList chipOffers = new NbtList();
        VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.BASIC_CHIP);
        VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.COPPER_CHIP);
        VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.IRON_CHIP);
        VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.AMETHYST_CHIP);
        VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.GOLDEN_CHIP);
        VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.EMERALD_CHIP);
        VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.DIAMOND_CHIP);
        VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.NETHERITE_CHIP);
        shops.add(VillagerTradeHelper.makeShopCompound("Chips", chipOffers));

        return new VillagerTradeHelper.ShopData(shops, "cobbledollars:cobble_merchant", 2, "cobblemon:display_case");

    }

    @Override
    public String getName() {
        return "Chip Dealer";
    }

}