/* package net.andrespr.casinorocket.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class SpawnCasinoWorkersCommand {

    private static final Map<String, Supplier<NbtCompound>> TEMPLATES = Map.of(
            "Dealer", SpawnCasinoWorkersCommand::createDealerNbt,
            "Prize Manager", SpawnCasinoWorkersCommand::createPrizeManagerNbt,
            "Food Merchant", SpawnCasinoWorkersCommand::createFoodMerchantNbt
    );

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("casinorocket").requires(source -> source.hasPermissionLevel(2)).
                then(CommandManager.literal("spawn"))
                        .then(CommandManager.argument("type", StringArgumentType.greedyString())
                            .executes(ctx -> {
                            String type = StringArgumentType.getString(ctx, "type");
                            return executeSpawn(type, ctx.getSource());
                            })
                        )
        );
    }

    private static int executeSpawn(String type, ServerCommandSource source) {
        Supplier<NbtCompound> factory = TEMPLATES.get(type);
        if (factory == null) {
            source.sendError(Text.literal("Tipo desconocido: " + type));
            return 0;
        }

        ServerWorld world = source.getWorld();
        BlockPos spawnPos = BlockPos.ofFloored(source.getPosition());

        // Construir NBT raíz (plantilla)
        NbtCompound root = factory.get();
        root.putString("id", "minecraft:villager");

        // CUSTOMNAME: buildear JSON manualmente para evitar dependencias de mappings
        String customNameJson = "{\"text\":\"" + escapeJsonString(type) + "\"}";
        root.putString("CustomName", customNameJson);

        // Crear entidad desde NBT (devuelve Optional<Entity> en 1.21)
        Optional<Entity> maybe;
        try {
            maybe = EntityType.getEntityFromNbt(root, (World) world); // ServerWorld es World; cast no obligatorio pero mantenido por claridad
        } catch (Exception e) {
            CasinoRocket.LOGGER.error("Error al parsear NBT para crear entidad", e);
            source.sendError(Text.literal("Error al parsear NBT: " + e.getMessage()));
            return 0;
        }

        if (maybe.isEmpty()) {
            source.sendError(Text.literal("No se pudo crear la entidad desde el NBT (Optional vacío)."));
            return 0;
        }

        Entity entity = maybe.get();

        // Posicionar la entidad
        entity.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0f, 0f);

        // Intentar spawnear (spawnEntity devuelve boolean)
        boolean spawned;
        try {
            spawned = world.spawnEntity(entity);
        } catch (Exception e) {
            CasinoRocket.LOGGER.error("Excepción al spawnear la entidad", e);
            source.sendError(Text.literal("Excepción al spawnear la entidad: " + e.getMessage()));
            return 0;
        }

        if (!spawned) {
            source.sendError(Text.literal("No se pudo spawnear la entidad en el mundo."));
            return 0;
        }

        source.sendFeedback(() -> Text.literal("§a" + type + " spawneado correctamente."), false);
        return 1;
    }

    // ---------------------
    // Plantillas de NBT
    // ---------------------

    private static NbtCompound createDealerNbt() {
        NbtCompound root = new NbtCompound();

        // VillagerData: profession debe coincidir con tu registro (modid:nombre)
        NbtCompound villagerData = new NbtCompound();
        villagerData.putString("profession", "casinorocket:casino_worker"); // si tu profession registró otro id, cámbialo aquí
        villagerData.putInt("level", 1);
        villagerData.putString("type", "minecraft:plains");
        root.put("VillagerData", villagerData);

        // CobbleMerchantShop
        NbtList shops = new NbtList();
        NbtCompound shop = new NbtCompound();
        shop.putString("Category", "Casino");

        NbtList offers = new NbtList();
        offers.add(makeOfferCompound("casinorocket:basic_chip", 1, "100"));
        offers.add(makeOfferCompound("casinorocket:copper_chip", 1, "1000"));
        offers.add(makeOfferCompound("casinorocket:iron_chip", 1, "5000"));
        offers.add(makeOfferCompound("casinorocket:amethyst_chip", 1, "25000"));
        offers.add(makeOfferCompound("casinorocket:golden_chip", 1, "100000"));
        offers.add(makeOfferCompound("casinorocket:emerald_chip", 1, "1000000"));
        offers.add(makeOfferCompound("casinorocket:diamond_chip", 1, "10000000"));
        offers.add(makeOfferCompound("casinorocket:netherite_chip", 1, "100000000"));

        shop.put("Offers", offers);
        shops.add(shop);
        root.put("CobbleMerchantShop", shops);

        return root;
    }

    private static NbtCompound createPrizeManagerNbt() {
        NbtCompound root = new NbtCompound();

        NbtCompound villagerData = new NbtCompound();
        villagerData.putString("profession", "casinorocket:casino");
        villagerData.putInt("level", 1);
        villagerData.putString("type", "minecraft:plains");
        root.put("VillagerData", villagerData);

        NbtList shops = new NbtList();
        NbtCompound shop = new NbtCompound();
        shop.putString("Category", "Casino");

        NbtList offers = new NbtList();
        offers.add(makeOfferCompound("casinorocket:basic_chip", 5, "450"));
        offers.add(makeOfferCompound("casinorocket:emerald_chip", 1, "1000000"));
        shop.put("Offers", offers);
        shops.add(shop);
        root.put("CobbleMerchantShop", shops);

        return root;
    }

    private static NbtCompound createFoodMerchantNbt() {
        NbtCompound root = new NbtCompound();

        NbtCompound villagerData = new NbtCompound();
        villagerData.putString("profession", "casinorocket:casino");
        villagerData.putInt("level", 1);
        villagerData.putString("type", "minecraft:plains");
        root.put("VillagerData", villagerData);

        NbtList shops = new NbtList();
        NbtCompound shop = new NbtCompound();
        shop.putString("Category", "Casino");

        NbtList offers = new NbtList();
        offers.add(makeOfferCompound("minecraft:apple", 1, "200"));
        offers.add(makeOfferCompound("minecraft:cooked_beef", 1, "1000"));
        shop.put("Offers", offers);
        shops.add(shop);
        root.put("CobbleMerchantShop", shops);

        return root;
    }

    private static NbtCompound makeOfferCompound(String itemId, int count, String price) {
        NbtCompound offer = new NbtCompound();
        NbtCompound item = new NbtCompound();

        item.putString("id", itemId);
        item.putByte("Count", (byte) count);
        item.putInt("count", count);

        offer.put("Item", item);
        offer.putString("Price", price);
        return offer;
    }

    private static String escapeJsonString(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

} */