package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.network.SuitSync;
import net.andrespr.casinorocket.util.SuitData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
@SuppressWarnings("unused")
public abstract class EntityStartTrackingMixin {

    @Inject(method = "onStartedTrackingBy", at = @At("HEAD"))
    private void casinorocket$syncSuitWhenPlayerStartsTracking(ServerPlayerEntity player, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (!(self instanceof VillagerEntity villager)) return;

        int suit = SuitData.getSuit(villager);
        if (suit > 0 && player.getServer() != null) {
            SuitSync.sendSuitSync(player.getServer(), villager, suit);
        }
    }

}