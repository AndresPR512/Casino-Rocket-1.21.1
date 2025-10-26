package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.util.SuitData;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerClothingFeatureRenderer.class)
@SuppressWarnings("unused")
public class VillagerClothingFeatureRendererMixin {

    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void casinorocket$hideClothingWhenSuited(
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity livingEntity,
            float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch,
            CallbackInfo ci
    ) {
        try {
            if (livingEntity instanceof VillagerEntity villager) {
                int suit = SuitData.getSuit(villager);
                if (suit > 0) {
                    ci.cancel();
                    if (CasinoRocket.LOGGER.isDebugEnabled()) {
                        CasinoRocket.LOGGER.debug("[Render] Ocultando ropa vanilla para {} (suit={})", villager.getUuid(), suit);
                    }
                }
            }
        } catch (Throwable ex) {
            CasinoRocket.LOGGER.warn("Error en VillagerClothingFeatureRendererMixin", ex);
        }
    }

}