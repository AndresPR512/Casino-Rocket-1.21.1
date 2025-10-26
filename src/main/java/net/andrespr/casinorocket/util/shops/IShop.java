package net.andrespr.casinorocket.util.shops;

import net.andrespr.casinorocket.util.VillagerTradeHelper;

public interface IShop {
    VillagerTradeHelper.ShopData build();
    String getName();
}