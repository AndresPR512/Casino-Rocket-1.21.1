package net.andrespr.casinorocket.inventory;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;

public class ChipBankInventory extends SimpleInventory {

    public ChipBankInventory() {
        super(27);
    }

    // ===== NBT =====

    public void readNbtList(NbtList list, RegistryWrapper.WrapperLookup registries) {
        for (int i = 0; i < this.size(); i++) {
            this.setStack(i, ItemStack.EMPTY);
        }

        for (int i = 0; i < list.size(); i++) {
            NbtCompound c = list.getCompound(i);
            int slot = c.getByte("Slot") & 255;
            if (slot >= 0 && slot < this.size()) {
                this.setStack(slot, ItemStack.fromNbt(registries, c).orElse(ItemStack.EMPTY));
            }
        }
    }

    public NbtList toNbtList(RegistryWrapper.WrapperLookup registries) {
        NbtList list = new NbtList();

        for (int i = 0; i < this.size(); i++) {
            ItemStack stack = this.getStack(i);
            if (!stack.isEmpty()) {
                NbtCompound c = new NbtCompound();
                c.putByte("Slot", (byte) i);
                list.add(stack.encode(registries, c));
            }
        }

        return list;
    }

}