package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public final class CashierShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        // ===== CHIPS TO MONEY =====
        NbtList chipOffers = new NbtList();
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.BASIC_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.RED_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.BLUE_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.PURPLE_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.COPPER_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.IRON_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.EMERALD_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.GOLD_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.DIAMOND_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.NETHERITE_CHIP);
        NbtCompound offers = VillagerTradeHelper.makeVanillaShopCompound(chipOffers);

        return new VillagerTradeHelper.ShopData(new NbtList(), "casinorocket:casino_worker", 2, "casinorocket:chip_table").withOffers(offers);

    }

    @Override
    public String getName() {
        return "Cashier";
    }

}