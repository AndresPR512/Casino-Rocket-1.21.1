package net.andrespr.casinorocket.util;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.BillItem;

public class MoneyCalculator {

    public record MoneyResult(BillItem billType, int amount) {}

    public static MoneyResult calculateDenomination(long value) {

        BillItem billType;
        int amount;

        if (value <= 0) { return new MoneyResult(ModItems.BILL_100, 1); }
        if ((value % 100_000_000) == 0) {
            billType = ModItems.BILL_100M;
            amount = Math.toIntExact(value / 100_000_000L);
        } else if ((value % 10_000_000) == 0) {
            billType = ModItems.BILL_10M;
            amount = Math.toIntExact(value / 10_000_000L);
        } else if ((value % 1_000_000) == 0) {
            billType = ModItems.BILL_1M;
            amount = Math.toIntExact(value / 1_000_000);
        } else if ((value % 500_000) == 0) {
            billType = ModItems.BILL_500K;
            amount = Math.toIntExact(value / 500_000);
        } else if ((value % 100_000) == 0) {
            billType = ModItems.BILL_100K;
            amount = Math.toIntExact(value / 100_000);
        } else if ((value % 50_000) == 0) {
            billType = ModItems.BILL_50K;
            amount = Math.toIntExact(value / 50_000);
        } else if ((value % 25_000) == 0) {
            billType = ModItems.BILL_25K;
            amount = Math.toIntExact(value / 25_000);
        } else if ((value % 10_000) == 0) {
            billType = ModItems.BILL_10K;
            amount = Math.toIntExact(value / 10_000);
        } else if ((value % 5_000) == 0) {
            billType = ModItems.BILL_5K;
            amount = Math.toIntExact(value / 5_000);
        } else if ((value % 1_000) == 0) {
            billType = ModItems.BILL_1K;
            amount = Math.toIntExact(value / 1_000);
        } else if ((value % 500) == 0) {
            billType = ModItems.BILL_500;
            amount = Math.toIntExact(value / 500);
        } else if ((value % 100) == 0) {
            billType = ModItems.BILL_100;
            amount = Math.toIntExact(value / 100);
        } else {
            billType = ModItems.BILL_100;
            amount = 1;
        }
        if (amount > 64) { amount = 64; }

        return new MoneyResult(billType, amount);

    }

}