package net.andrespr.casinorocket.villager;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.custom.BillItem;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;

import java.util.List;

public class VillagerTradeHelper {

    // ===== BASE STRUCTURE - SHOP DATA =====

    public static class ShopData {
        public final NbtList shops;
        public final String profession;
        public final int suitId;
        public NbtCompound offersNbt;
        public final String jobBlockId;

        public ShopData(NbtList shops, String profession, int suitId) {
            this(shops, profession, suitId, null);
        }

        public ShopData(NbtList shops, String profession, int suitId, String jobBlockId) {
            this.shops = shops;
            this.profession = profession;
            this.suitId = suitId;
            this.jobBlockId = jobBlockId;
        }

        public ShopData withOffers(NbtCompound offers) {
            this.offersNbt = offers;
            return this;
        }
    }

    // ===== COBBLEDOLLARS TRADES =====

    public static NbtCompound makeShopCompound(String category, NbtList offers) {
        NbtCompound shop = new NbtCompound();
        shop.putString("Category", category);
        shop.put("Offers", offers);
        return shop;
    }

    public static NbtCompound makeOffer(String itemId, String price) {
        NbtCompound offer = new NbtCompound();
        NbtCompound item = new NbtCompound();
        item.putString("id", itemId);
        item.putByte("Count", (byte) 1);
        item.putInt("count", 1);
        offer.put("Item", item);
        offer.putString("Price", price);
        return offer;
    }

    // ===== VANILLA TRADES =====

    public static NbtCompound makeVanillaShopCompound(NbtList trades) {
        NbtCompound offers = new NbtCompound();
        offers.put("Recipes", trades);
        return offers;
    }

    public static NbtCompound makeVanillaOffer(String buyItem, int buyCount, String sellItem, int sellCount) {
        NbtCompound trade = new NbtCompound();

        NbtCompound buy = new NbtCompound();
        buy.putString("id", buyItem);
        buy.putByte("Count", (byte) buyCount);
        buy.putInt("count", buyCount);
        trade.put("buy", buy);

        NbtCompound sell = new NbtCompound();
        sell.putString("id", sellItem);
        sell.putByte("Count", (byte) sellCount);
        sell.putInt("count", sellCount);
        trade.put("sell", sell);

        trade.putBoolean("rewardExp", false);
        trade.putInt("maxUses", 999999);
        trade.putInt("uses", 0);
        trade.putFloat("priceMultiplier", 0.0f);
        trade.putInt("demand", 0);
        trade.putInt("specialPrice", 0);
        trade.putInt("xp", 0);
        trade.putBoolean("ignorePriceMultiplier", true);

        return trade;
    }

    // ===== SPECIFIC TRADES =====

    public static void makeMoneyToChipOffer(NbtList trades, ChipItem chip) {
        String chipId = Registries.ITEM.getId(chip).toString();
        String value = String.valueOf(chip.getValue());
        trades.add(makeOffer(chipId,value));
    }

    public static void makeChipToMoneyOffer(NbtList trades, ChipItem chip) {
        String chipId = Registries.ITEM.getId(chip).toString();
        MoneyCalculator.MoneyResult result = MoneyCalculator.calculateDenomination(chip.getValue());
        String billId = Registries.ITEM.getId(result.billType()).toString();
        trades.add(makeVanillaOffer(chipId, 1, billId, result.amount()));
    }

    public static void makeInCashOffer(NbtList trades, BillItem bill) {
        String billId = Registries.ITEM.getId(bill).toString();
        String value = String.valueOf(bill.getValue());
        trades.add(makeOffer(billId,value));
    }

    public static void makeListOffer(NbtList trades, List<String> entries, String modID, String price) {
        for (String entry : entries) {
            trades.add(makeOffer(modID + ":" + entry, price));
        }
    }

    public static void makeCoinOffer(NbtList trades, Item coin) {
        String coinId = Registries.ITEM.getId(coin).toString();
        String coinPrice = CasinoRocket.CONFIG.gachaMachines.getCoinPrice(coinId);
        trades.add(makeOffer(coinId, coinPrice));
    }

    public static void makeGachaponOffer(NbtList trades, Item gachapon) {
        String gachaponId = Registries.ITEM.getId(gachapon).toString();
        boolean isGachaponEnable = CasinoRocket.CONFIG.gachaMachines.getGachaponEnable(gachaponId);
        if (isGachaponEnable) {
            String gachaponPrice = CasinoRocket.CONFIG.gachaMachines.getGachaponPrice(gachaponId);
            trades.add(makeOffer(gachaponId, gachaponPrice));
        }
    }

}