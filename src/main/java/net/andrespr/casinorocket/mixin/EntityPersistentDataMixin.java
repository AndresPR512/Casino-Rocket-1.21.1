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

    private static final String SUIT_KEY = "casinorocket.CustomSuit";
    private static final String LOOK_KEY = "casinorocket.LookPlayer";
    private static final String IDLE_YAW_KEY = "casinorocket.IdleYaw";

    @Override
    public NbtCompound getPersistentData() {
        if (casinorocket$persistentData == null) {
            casinorocket$persistentData = new NbtCompound();
        }
        return casinorocket$persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void casinorocket$write(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (casinorocket$persistentData == null) return;
        if (casinorocket$persistentData.contains(SUIT_KEY)) {
            nbt.putInt(SUIT_KEY, casinorocket$persistentData.getInt(SUIT_KEY));
        }
        if (casinorocket$persistentData.contains(LOOK_KEY)) {
            nbt.putInt(LOOK_KEY, casinorocket$persistentData.getInt(LOOK_KEY));
        }
        if (casinorocket$persistentData.contains(IDLE_YAW_KEY)) {
            nbt.putFloat(IDLE_YAW_KEY, casinorocket$persistentData.getFloat(IDLE_YAW_KEY));
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void casinorocket$read(NbtCompound nbt, CallbackInfo ci) {
        casinorocket$persistentData = new NbtCompound();
        if (nbt.contains(SUIT_KEY)) {
            casinorocket$persistentData.putInt(SUIT_KEY, nbt.getInt(SUIT_KEY));
        }
        if (nbt.contains(LOOK_KEY)) {
            casinorocket$persistentData.putInt(LOOK_KEY, nbt.getInt(LOOK_KEY));
        }
        if (nbt.contains(IDLE_YAW_KEY)) {
            casinorocket$persistentData.putFloat(IDLE_YAW_KEY, nbt.getFloat(IDLE_YAW_KEY));
        }
    }

}