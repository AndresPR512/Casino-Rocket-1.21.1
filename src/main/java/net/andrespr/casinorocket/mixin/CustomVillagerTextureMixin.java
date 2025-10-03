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

/**
 * Mixin cliente: aplica textura personalizada por nombre y, en el constructor
 * del VillagerEntityRenderer, remueve la VillagerClothingFeatureRenderer para
 * evitar overlays de ropa/profession que tapan la textura.
 * Coloca este mixin en la sección "client" de tu mixins json.
 */
@Mixin(VillagerEntityRenderer.class)
@SuppressWarnings("unused")
public class CustomVillagerTextureMixin {

    // Mapa nombre visible -> textura a usar
    private static final Identifier SUIT_TEXTURE =
            Identifier.of(CasinoRocket.MOD_ID, "textures/entity/villager/casino_worker.png");

    // Si quieres varias texturas por nombre, cambia esto a Map<String, Identifier>
    private static final String[] NAMES = new String[] { "Dealer", "Prize Manager", "Food Merchant" };

    // -------------------------
    // 1) Reemplazo de textura
    // -------------------------
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
            CasinoRocket.LOGGER.warn("CustomVillagerTextureMixin: error comprobando nombre", ex);
        }
    }

    // --------------------------------------------------
    // 2) En el constructor del renderer: quitar clothing
    // --------------------------------------------------
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onCtorReturn(CallbackInfo ci) {
        try {
            Object self = this; // instancia de VillagerEntityRenderer

            // Recorremos campos declarados y buscamos List<?> (la lista de features)
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

                // Removemos solo instancias de VillagerClothingFeatureRenderer
                boolean removedAny = list.removeIf(o -> o != null && o instanceof VillagerClothingFeatureRenderer);

                if (removedAny) {
                    CasinoRocket.LOGGER.info("CustomVillagerTextureMixin: removed VillagerClothingFeatureRenderer from field '{}'", f.getName());
                }
            }
        } catch (Throwable t) {
            CasinoRocket.LOGGER.warn("CustomVillagerTextureMixin: fallo al intentar quitar VillagerClothingFeatureRenderer (no crítico)", t);
        }
    }
}