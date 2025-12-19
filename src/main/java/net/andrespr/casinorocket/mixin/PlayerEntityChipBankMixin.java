package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.inventory.ChipBankInventory;
import net.andrespr.casinorocket.util.IChipBankHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityChipBankMixin implements IChipBankHolder {

    @Unique
    private static final String CHIP_BANK_KEY = "CasinoRocketChipBank";

    @Unique
    private final ChipBankInventory casinorocket$chipBankInventory = new ChipBankInventory();

    @Override
    public ChipBankInventory casinorocket$getChipBankInventory() {
        return casinorocket$chipBankInventory;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void casinorocket$writeChipBank(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        nbt.put(CHIP_BANK_KEY, casinorocket$chipBankInventory.toNbtList(self.getRegistryManager()));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void casinorocket$readChipBank(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (nbt.contains(CHIP_BANK_KEY, NbtElement.LIST_TYPE)) {
            casinorocket$chipBankInventory.readNbtList(
                    nbt.getList(CHIP_BANK_KEY, NbtElement.COMPOUND_TYPE),
                    self.getRegistryManager()
            );
        }
    }

}