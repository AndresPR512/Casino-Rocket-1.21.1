package net.andrespr.casinorocket.util;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.BillItem;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MoneyCalculator {

    public record MoneyResult(BillItem billType, int amount) {}

    public static MoneyResult calculateDenomination(long value) {

        if (value <= 0) return new MoneyResult(ModItems.BILL_10, 1);

        List<BillItem> bills = ModItems.ALL_BILL_ITEMS.stream()
                .map(item -> (BillItem) item)
                .sorted(Comparator.comparingLong(BillItem::getValue).reversed())
                .toList();

        for (BillItem bill : bills) {
            long billValue = bill.getValue();

            if (billValue > 0 && value % billValue == 0) {
                long amount = value / billValue;
                if (amount > 64) amount = 64;
                return new MoneyResult(bill, (int) amount);
            }
        }

        return new MoneyResult(ModItems.BILL_10, 1);
    }

    public static List<ItemStack> calculateChipWithdraw(long amount) {

        List<ItemStack> result = new ArrayList<>();
        if (amount <= 0) return result;

        List<ChipItem> chips = ModItems.ALL_CHIP_ITEMS.stream()
                .map(item -> (ChipItem) item)
                .sorted(Comparator.comparingLong(ChipItem::getValue).reversed())
                .toList();

        long remaining = amount;

        for (ChipItem chip : chips) {
            long value = chip.getValue();
            if (value <= 0) continue;

            long count = remaining / value;
            if (count <= 0) continue;

            while (count > 0) {
                int stackSize = (int) Math.min(64, count);
                result.add(new ItemStack(chip, stackSize));

                count -= stackSize;
                remaining -= (long) stackSize * value;
            }

            if (remaining <= 0) break;
        }

        return result;
    }

}