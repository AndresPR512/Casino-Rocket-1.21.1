package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.util.IdleYawData;
import net.andrespr.casinorocket.util.LookPlayerData;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerLookAtPlayerMixin {

    private static final double RANGE = 10.0;
    private static final float MAX_YAW_STEP = 12.0f;
    private static final float MAX_PITCH_STEP = 10.0f;

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void casinorocket$lookAtClosestPlayerOrIdle(CallbackInfo ci) {
        VillagerEntity villager = (VillagerEntity) (Object) this;
        if (villager.getWorld().isClient()) return;

        if (LookPlayerData.getLookPlayer(villager) != 1) return;

        PlayerEntity player = villager.getWorld().getClosestPlayer(villager, RANGE);

        float targetYaw;
        float targetPitch;

        if (player != null && !player.isSpectator()) {
            Vec3d from = villager.getEyePos();
            Vec3d to = player.getEyePos();

            double dx = to.x - from.x;
            double dy = to.y - from.y;
            double dz = to.z - from.z;

            double distXZ = Math.sqrt(dx * dx + dz * dz);
            if (distXZ < 1.0e-4) return;

            targetYaw = (float) (MathHelper.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0f;
            targetPitch = (float) (-(MathHelper.atan2(dy, distXZ) * (180.0 / Math.PI)));
        } else {
            targetYaw = IdleYawData.get(villager);
            targetPitch = 0.0f;
        }

        float newYaw = stepAngle(villager.getYaw(), targetYaw, MAX_YAW_STEP);
        float newPitch = stepAngle(villager.getPitch(), targetPitch, MAX_PITCH_STEP);

        villager.setYaw(newYaw);
        villager.bodyYaw = newYaw;
        villager.headYaw = newYaw;

        villager.setPitch(newPitch);

        villager.prevYaw = newYaw;
        villager.prevHeadYaw = newYaw;
        villager.prevBodyYaw = newYaw;
        villager.prevPitch = newPitch;
    }

    private static float stepAngle(float current, float target, float maxStep) {
        float delta = MathHelper.wrapDegrees(target - current);
        if (delta > maxStep) delta = maxStep;
        if (delta < -maxStep) delta = -maxStep;
        return current + delta;
    }

}