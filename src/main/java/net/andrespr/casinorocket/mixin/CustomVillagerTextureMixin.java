package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.util.SuitData;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(VillagerEntityRenderer.class)
@SuppressWarnings("unused")
public class CustomVillagerTextureMixin {

    private static final Identifier TUX_BLACK = Identifier.of(CasinoRocket.MOD_ID, "textures/entity/villager/black_tuxedo.png");
    private static final Identifier TUX_WHITE = Identifier.of(CasinoRocket.MOD_ID, "textures/entity/villager/white_tuxedo.png");
    private static final Identifier TUX_GOLD  = Identifier.of(CasinoRocket.MOD_ID, "textures/entity/villager/gold_tuxedo.png");

    @Inject(method = "getTexture(Lnet/minecraft/entity/passive/VillagerEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("HEAD"), cancellable = true)
    private void casinorocket$getTexture(VillagerEntity villager, CallbackInfoReturnable<Identifier> cir) {
        int suit = SuitData.getSuit(villager);
        switch (suit) {
            case 1 -> cir.setReturnValue(TUX_BLACK);
            case 2 -> cir.setReturnValue(TUX_WHITE);
            case 3 -> cir.setReturnValue(TUX_GOLD);
            default -> { return; }
        }
        if (CasinoRocket.LOGGER.isDebugEnabled()) {
            CasinoRocket.LOGGER.debug("[Render] Using custom suit {} for {}", suit, villager.getUuid());
        }
    }

}