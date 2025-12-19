package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.network.SuitSync;
import net.andrespr.casinorocket.util.SuitData;
import net.andrespr.casinorocket.villager.ModVillagers;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerSetProfessionMixin {

    @Shadow
    public abstract VillagerData getVillagerData();

    @Inject(method = "setVillagerData", at = @At("HEAD"))
    private void casinorocket$onProfessionChange(VillagerData newData, CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity) (Object) this;
        if (villager.getWorld().isClient()) return;

        VillagerProfession oldProfession = this.getVillagerData().getProfession();
        VillagerProfession newProfession = newData.getProfession();

        if (oldProfession != ModVillagers.CASINO_WORKER && newProfession == ModVillagers.CASINO_WORKER) {
            casinorocket$applySuit(villager, 1);
        } else if (oldProfession == ModVillagers.CASINO_WORKER && newProfession != ModVillagers.CASINO_WORKER) {
            casinorocket$applySuit(villager, 0);
        }
    }

    private static void casinorocket$applySuit(VillagerEntity villager, int suit) {
        int current = SuitData.getSuit(villager);
        if (current == suit) return;

        SuitData.setSuitServer(villager, suit);
        SuitSync.sendSuitSync(villager, suit);
    }

}