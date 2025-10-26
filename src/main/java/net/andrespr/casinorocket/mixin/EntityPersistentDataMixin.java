package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
@SuppressWarnings("unused")
public abstract class EntityPersistentDataMixin implements IEntityDataSaver {

    private NbtCompound casinorocket$persistentData;

    private static final String CASINO_KEY = "casinorocket.CustomSuit";

    @Override
    public NbtCompound getPersistentData() {
        if (casinorocket$persistentData == null) {
            casinorocket$persistentData = new NbtCompound();
        }
        return casinorocket$persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void casinorocket$write(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (casinorocket$persistentData != null && casinorocket$persistentData.contains(CASINO_KEY)) {
            nbt.putInt(CASINO_KEY, casinorocket$persistentData.getInt(CASINO_KEY));
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void casinorocket$read(NbtCompound nbt, CallbackInfo ci) {
        casinorocket$persistentData = new NbtCompound();
        if (nbt.contains(CASINO_KEY)) {
            casinorocket$persistentData.putInt(CASINO_KEY, nbt.getInt(CASINO_KEY));
        }
    }

}