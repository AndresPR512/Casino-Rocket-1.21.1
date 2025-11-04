package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.villager.VillagerTradeHelper;

public interface IShop {
    VillagerTradeHelper.ShopData build();
    String getName();
}