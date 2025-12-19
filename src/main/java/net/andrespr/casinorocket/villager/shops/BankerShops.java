package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.BillItem;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtList;

public final class BankerShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        NbtList shops = new NbtList();

        // ===== IN CASH MONEY =====
        NbtList billOffers = new NbtList();
        for (Item bill : ModItems.ALL_BILL_ITEMS) VillagerTradeHelper.makeInCashOffer(billOffers, (BillItem) bill);
        shops.add(VillagerTradeHelper.makeShopCompound("Bills", billOffers));

        return new VillagerTradeHelper.ShopData(shops, "cobbledollars:cobble_merchant", 2, "cobblemon:display_case");

    }

    @Override
    public String getName() {
        return "Banker";
    }

}