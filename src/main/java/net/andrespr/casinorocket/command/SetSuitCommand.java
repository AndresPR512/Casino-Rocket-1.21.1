package net.andrespr.casinorocket.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.andrespr.casinorocket.network.SuitSync;
import net.andrespr.casinorocket.util.SuitData;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.entity.projectile.ProjectileUtil;
import java.util.Map;
import java.util.TreeMap;

public class SetSuitCommand {

    private static final Map<String, Integer> SUIT_NAMES = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        SUIT_NAMES.put("None", 0);
        SUIT_NAMES.put("Black Tuxedo", 1);
        SUIT_NAMES.put("White Tuxedo", 2);
        SUIT_NAMES.put("Gold Tuxedo", 3);
    }

    public static LiteralArgumentBuilder<ServerCommandSource> buildSubcommand() {
        return CommandManager.literal("setsuit")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("suit", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            SUIT_NAMES.keySet().forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String suitName = StringArgumentType.getString(context, "suit");
                            ServerCommandSource source = context.getSource();
                            return executeSetSuit(source, suitName);
                        })
                );
    }

    private static int executeSetSuit(ServerCommandSource source, String suitName) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.literal("This command can only be used by a player."));
            return 0;
        }

        Integer suitValue = SUIT_NAMES.get(suitName);
        if (suitValue == null) {
            source.sendError(Text.literal("Unknown suit name: " + suitName));
            return 0;
        }

        Entity target = getVillagerLookingAt(source);
        if (target instanceof VillagerEntity villager) {
            SuitData.setSuitServer(villager, suitValue);
            SuitSync.sendSuitSync(source.getServer(), villager, suitValue);

            source.sendFeedback(() ->
                            Text.literal(suitName + " set to " +
                                    villager.getName().getString()), true);
            return 1;
        } else {
            source.sendError(Text.literal("You must be looking at a Villager!"));
            return 0;
        }
    }

    private static Entity getVillagerLookingAt(ServerCommandSource source) {
        try {
            var player = source.getPlayer();
            if (player == null) return null;
            var world = player.getWorld();

            double reachDistance = 10.0D;
            var eyePos = player.getCameraPosVec(1.0F);
            var lookVec = player.getRotationVec(1.0F);
            var endVec = eyePos.add(lookVec.multiply(reachDistance));

            var box = player.getBoundingBox().stretch(lookVec.multiply(reachDistance)).expand(1.0D);

            EntityHitResult hitResult = ProjectileUtil.getEntityCollision(
                    world, player, eyePos, endVec, box,
                    entity -> entity instanceof VillagerEntity && entity.isAlive()
            );

            if (hitResult != null) {
                return hitResult.getEntity();
            }
        } catch (Exception ignored) {}
        return null;
    }

}