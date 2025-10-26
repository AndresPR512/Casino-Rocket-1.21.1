package net.andrespr.casinorocket.util.shops;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.util.VillagerTradeHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public final class CashierShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        // ===== CHIPS TO MONEY =====
        NbtList chipOffers = new NbtList();
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.BASIC_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.COPPER_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.IRON_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.AMETHYST_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.GOLDEN_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.EMERALD_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.DIAMOND_CHIP);
        VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.NETHERITE_CHIP);
        NbtCompound offers = VillagerTradeHelper.makeVanillaShopCompound(chipOffers);

        return new VillagerTradeHelper.ShopData(new NbtList(), "minecraft:librarian", 2, "minecraft:lectern").withOffers(offers);

    }

    @Override
    public String getName() {
        return "Cashier";
    }

}