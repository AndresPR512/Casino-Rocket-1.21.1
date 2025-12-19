package net.andrespr.casinorocket.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.network.SuitSync;
import net.andrespr.casinorocket.util.IdleYawData;
import net.andrespr.casinorocket.util.LookPlayerData;
import net.andrespr.casinorocket.util.SuitData;
import net.andrespr.casinorocket.villager.VillagerNbtFactory;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.andrespr.casinorocket.villager.ShopsRegistry;
import net.andrespr.casinorocket.villager.shops.IShop;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.TreeMap;

public final class VillagerCommands {

    private VillagerCommands() {}

    // === SPAWN ===
    private static final Map<String, IShop> SHOP_TYPES = ShopsRegistry.all();

    // === SET SUIT ===
    private static final Map<String, Integer> SUIT_NAMES = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    static {
        SUIT_NAMES.put("None", 0);
        SUIT_NAMES.put("Black Tuxedo", 1);
        SUIT_NAMES.put("White Tuxedo", 2);
        SUIT_NAMES.put("Gold Tuxedo", 3);
    }

    public static LiteralArgumentBuilder<ServerCommandSource> buildSubcommand() {
        return CommandManager.literal("villager")
                .requires(src -> src.hasPermissionLevel(2))

                // /casinorocket villager spawn <type>
                .then(CommandManager.literal("spawn")
                        .then(CommandManager.argument("type", StringArgumentType.greedyString())
                                .suggests((ctx, builder) -> {
                                    for (String k : SHOP_TYPES.keySet()) builder.suggest(k);
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> executeSpawn(
                                        StringArgumentType.getString(ctx, "type"),
                                        ctx.getSource()
                                ))))

                // /casinorocket villager setsuit <suitName>
                .then(CommandManager.literal("setsuit")
                        .then(CommandManager.argument("suit", StringArgumentType.greedyString())
                                .suggests((ctx, builder) -> {
                                    SUIT_NAMES.keySet().forEach(builder::suggest);
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> executeSetSuit(
                                        ctx.getSource(),
                                        StringArgumentType.getString(ctx, "suit")
                                ))))

                // /casinorocket villager setai <true/false>
                .then(CommandManager.literal("setai")
                        .then(CommandManager.argument("value", BoolArgumentType.bool())
                                .executes(ctx -> executeSetAi(
                                        ctx.getSource(),
                                        BoolArgumentType.getBool(ctx, "value")
                                ))))

                // /casinorocket villager lookplayer <true/false>
                .then(CommandManager.literal("lookplayer")
                        .then(CommandManager.argument("value", BoolArgumentType.bool())
                                .executes(ctx -> executeLookPlayer(
                                        ctx.getSource(),
                                        BoolArgumentType.getBool(ctx, "value")
                                ))))

                // /casinorocket villager lookdirection <north/south/east/west>
                .then(CommandManager.literal("lookdirection")
                        .then(CommandManager.argument("dir", StringArgumentType.word())
                                .suggests((ctx, b) -> {
                                    b.suggest("north");
                                    b.suggest("south");
                                    b.suggest("east");
                                    b.suggest("west");
                                    return b.buildFuture();
                                })
                                .executes(ctx -> executeLookDirection(
                                        ctx.getSource(),
                                        StringArgumentType.getString(ctx, "dir")
                                ))
                        )
                );
    }

    // === SPAWN ===
    private static int executeSpawn(String type, ServerCommandSource source) {

        IShop shop = SHOP_TYPES.get(type);
        if (shop == null) {
            source.sendError(Text.literal("Unknown casino worker type: " + type));
            return 0;
        }

        ServerWorld world = source.getWorld();
        BlockPos blockPos = BlockPos.ofFloored(source.getPosition());
        BlockPos villagerPos = blockPos.east();

        VillagerTradeHelper.ShopData data = shop.build();

        String blockId = (data.jobBlockId != null) ? data.jobBlockId : "cobblemon:display_case";
        Block jobBlock = Registries.BLOCK.get(Identifier.of(blockId));
        world.setBlockState(blockPos, jobBlock.getDefaultState());

        String formattedType = ShopsRegistry.all().keySet().stream().filter(k -> k.equalsIgnoreCase(type))
                .findFirst().orElse(type);

        NbtCompound root = VillagerNbtFactory.createBaseVillagerNbt(
                formattedType, blockPos, data.profession, data.suitId
        );

        if (data.shops != null && !data.shops.isEmpty()) {
            root.put("CobbleMerchantShop", data.shops);
        }
        if (data.offersNbt != null && data.offersNbt.contains("Recipes")) {
            root.put("Offers", data.offersNbt);
        }

        VillagerEntity villager;
        try {
            villager = EntityType.VILLAGER.create(world);
            if (villager == null) {
                source.sendError(Text.literal("Failed to create villager entity."));
                return 0;
            }
            villager.readNbt(root);
        } catch (Exception e) {
            CasinoRocket.LOGGER.error("Error creating villager from NBT", e);
            source.sendError(Text.literal("Error creating villager from NBT: " + e.getMessage()));
            return 0;
        }

        ServerPlayerEntity player = source.getPlayer();
        float yaw = (player != null) ? player.getYaw() : 0f;

        villager.refreshPositionAndAngles
                (villagerPos.getX() + 0.5, villagerPos.getY(), villagerPos.getZ() + 0.5, yaw, 0f);

        boolean spawned;
        try {
            spawned = world.spawnEntity(villager);
        } catch (Exception e) {
            CasinoRocket.LOGGER.error("Exception when spawning entity", e);
            source.sendError(Text.literal("Exception when spawning entity: " + e.getMessage()));
            return 0;
        }

        if (!spawned) {
            source.sendError(Text.literal("The entity couldn't be spawned in the world."));
            return 0;
        }

        source.sendFeedback(() -> Text.literal("§a¡" + type + " spawned correctly!"), false);
        return 1;
    }

    // === SET SUIT ===
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

        VillagerEntity villager = getLookedVillager(source);
        if (villager == null) {
            source.sendError(Text.literal("You must be looking at a Villager!"));
            return 0;
        }

        int current = SuitData.getSuit(villager);
        if (current == suitValue) {
            source.sendFeedback(() -> Text.literal("Suit already set to " + suitName), false);
            return 1;
        }

        SuitData.setSuitServer(villager, suitValue);
        SuitSync.sendSuitSync(villager, suitValue);

        source.sendFeedback(() -> Text.literal(suitName + " set to " + villager.getName().getString()), true);
        return 1;
    }

    // === SET AI ===
    private static int executeSetAi(ServerCommandSource source, boolean enabled) {
        VillagerEntity villager = getLookedVillager(source);
        if (villager == null) {
            source.sendError(Text.literal("You must be looking at a Villager!"));
            return 0;
        }

        villager.setAiDisabled(!enabled);

        source.sendFeedback(() ->
                Text.literal("AI set to " + enabled + " for " + villager.getName().getString()), true);
        return 1;
    }

    // === LOOK PLAYER ===
    private static int executeLookPlayer(ServerCommandSource source, boolean enabled) {
        VillagerEntity villager = getLookedVillager(source);
        if (villager == null) {
            source.sendError(Text.literal("You must be looking at a Villager!"));
            return 0;
        }

        LookPlayerData.setLookPlayer(villager, enabled ? 1 : 0);

        source.sendFeedback(() -> Text.literal("LookPlayer set to " + enabled + " for " + villager.getName().getString()), true);
        return 1;
    }

    // === LOOK DIRECTION ===
    private static int executeLookDirection(ServerCommandSource source, String dir) {
        VillagerEntity villager = getLookedVillager(source);
        if (villager == null) {
            source.sendError(Text.literal("You must be looking at a Villager!"));
            return 0;
        }

        Float yaw = directionToYaw(dir);
        if (yaw == null) {
            source.sendError(Text.literal("Invalid direction: " + dir));
            return 0;
        }

        IdleYawData.set(villager, yaw);

        villager.setYaw(yaw);
        villager.bodyYaw = yaw;
        villager.headYaw = yaw;
        villager.setPitch(0f);

        villager.prevYaw = yaw;
        villager.prevBodyYaw = yaw;
        villager.prevHeadYaw = yaw;
        villager.prevPitch = 0f;

        source.sendFeedback(() -> Text.literal("Idle direction set to " + dir + " for " + villager.getName().getString()), true);
        return 1;
    }

    // === HELPERS ===
    private static Float directionToYaw(String dir) {
        return switch (dir.toLowerCase()) {
            case "south" -> 0f;
            case "west"  -> 90f;
            case "north" -> 180f;
            case "east"  -> -90f;
            default -> null;
        };
    }

    public static VillagerEntity getLookedVillager(ServerCommandSource source) {
        try {
            var player = source.getPlayer();
            if (player == null) return null;

            var world = player.getWorld();
            double reachDistance = 10.0D;

            var eyePos = player.getCameraPosVec(1.0F);
            var lookVec = player.getRotationVec(1.0F);
            var endVec = eyePos.add(lookVec.multiply(reachDistance));
            var box = player.getBoundingBox().stretch(lookVec.multiply(reachDistance)).expand(1.0D);

            EntityHitResult hit = ProjectileUtil.getEntityCollision(
                    world, player, eyePos, endVec, box,
                    entity -> entity instanceof VillagerEntity && entity.isAlive()
            );

            Entity e = (hit != null) ? hit.getEntity() : null;
            return (e instanceof VillagerEntity v) ? v : null;

        } catch (Exception ignored) {}
        return null;
    }

}