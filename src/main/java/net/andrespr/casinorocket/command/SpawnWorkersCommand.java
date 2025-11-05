package net.andrespr.casinorocket.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.villager.VillagerNbtFactory;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.andrespr.casinorocket.villager.shops.IShop;
import net.andrespr.casinorocket.villager.ShopsRegistry;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.Registries;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import java.util.Map;
import java.util.Objects;

public final class SpawnWorkersCommand {

    private static final Map<String, IShop> SHOP_TYPES = ShopsRegistry.all();

    public static LiteralArgumentBuilder<ServerCommandSource> buildSubcommand(CommandRegistryAccess registryAccess) {
        return CommandManager.literal("spawn")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("type", StringArgumentType.greedyString())
                        .suggests((ctx, builder) -> {
                            for (String k : SHOP_TYPES.keySet()) builder.suggest(k);
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            String type = StringArgumentType.getString(ctx, "type");
                            return executeSpawn(type, ctx.getSource());
                        }));
    }

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

        String blockId = (data.jobBlockId != null) ? data.jobBlockId : "cobblemon:display_case"; // bloque por defecto

        Block jobBlock = Registries.BLOCK.get(Identifier.of(blockId));
        world.setBlockState(blockPos, jobBlock.getDefaultState());

        String formattedType = ShopsRegistry.all().keySet().stream()
                .filter(k -> k.equalsIgnoreCase(type))
                .findFirst()
                .orElse(type);

        NbtCompound root = VillagerNbtFactory.createBaseVillagerNbt(formattedType, blockPos, data.profession, data.suitId);

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

        villager.refreshPositionAndAngles(
                villagerPos.getX() + 0.5, villagerPos.getY(), villagerPos.getZ() + 0.5,
                Objects.requireNonNull(source.getPlayer()).getYaw(), 0f
        );

        boolean spawned;
        try {
            spawned = world.spawnEntity(villager);
        } catch (Exception e) {
            CasinoRocket.LOGGER.error("Exception when spawning entity", e);
            source.sendError(Text.literal("Excepción when spawning entity: " + e.getMessage()));
            return 0;
        }
        if (!spawned) {
            source.sendError(Text.literal("The entity couldn't be spawned in the world."));
            return 0;
        }

        source.sendFeedback(() -> Text.literal("§a¡" + type + " spawned correctly!"), false);
        return 1;
    }

}