package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerClothingFeatureRenderer.class)
public class VillagerClothingFeatureRendererMixin {

    // Name List
    private static final String[] NAMES = new String[] { "Dealer", "Prize Dealer", "Snackmaster", "TM Instructor", "Battle Gear Merchant" };

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
            at = @At("HEAD"),
            cancellable = true)
    private void onRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity livingEntity,
                          float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch,
                          CallbackInfo ci) {
        try {
            if (!(livingEntity instanceof VillagerEntity villager)) return;
            if (!villager.hasCustomName()) return;
            Text t = villager.getCustomName();
            if (t == null) return;
            String name = t.getString().trim().replace("§", "");
            for (String key : NAMES) {
                if (key.equalsIgnoreCase(name)) {
                    ci.cancel();
                    return;
                }
            }
        } catch (Throwable ex) {
            CasinoRocket.LOGGER.warn("VillagerClothingFeatureRendererMixin error", ex);
        }
    }

}