package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.lang.reflect.Field;
import java.util.List;

@Mixin(VillagerEntityRenderer.class)
@SuppressWarnings("unused")
public class CustomVillagerTextureMixin {

    // Texture
    private static final Identifier SUIT_TEXTURE =
            Identifier.of(CasinoRocket.MOD_ID, "textures/entity/villager/casino_worker.png");

    // Name List
    private static final String[] NAMES = new String[] { "Dealer", "Prize Dealer", "Snackmaster", "TM Instructor", "Battle Gear Merchant" };

    @Inject(method = "getTexture(Lnet/minecraft/entity/passive/VillagerEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("HEAD"),
            cancellable = true)
    private void onGetTexture(VillagerEntity villager, CallbackInfoReturnable<Identifier> cir) {
        if (villager == null) return;
        try {
            if (!villager.hasCustomName()) return;
            Text t = villager.getCustomName();
            if (t == null) return;
            String name = t.getString().trim().replace("§", "");
            for (String k : NAMES) {
                if (k.equalsIgnoreCase(name)) {
                    cir.setReturnValue(SUIT_TEXTURE);
                    return;
                }
            }
        } catch (Throwable ex) {
            CasinoRocket.LOGGER.warn("CustomVillagerTextureMixin: Error checking name ", ex);
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onCtorReturn(CallbackInfo ci) {
        try {
            Object self = this;

            for (Field f : self.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                Object val;
                try {
                    val = f.get(self);
                } catch (IllegalAccessException e) {
                    continue;
                }
                if (!(val instanceof List<?>)) continue;

                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) val;
                if (list.isEmpty()) continue;

                boolean removedAny = list.removeIf(o -> o instanceof VillagerClothingFeatureRenderer);

                if (removedAny) {
                    CasinoRocket.LOGGER.info("CustomVillagerTextureMixin: removed VillagerClothingFeatureRenderer from field '{}'", f.getName());
                }
            }
        } catch (Throwable t) {
            CasinoRocket.LOGGER.warn("CustomVillagerTextureMixin: Failure when attempting to remove VillagerClothingFeatureRenderer (non-critical)", t);
        }
    }

}