package net.andrespr.casinorocket.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.nbt.VillagerNbtFactory;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.registry.Registries;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * SpawnCasinoWorkers
 * Comando: /casinorocket spawn <tipo>
 * - Coloca cobblemon:display_case en la posición del jugador
 * - Spawnea el villager a un lado del display_case con NBT completo (job_site, shops, etc.)
 * - Soporta múltiples tipos vía un mapa de plantillas
 */
public final class SpawnCasinoWorkers {

    // Mapa tipo -> Supplier<NbtList> (devuelve un NbtList de "shop compounds").
    private static final Map<String, Supplier<NbtList>> OFFERS_TEMPLATES = Map.of(
            "Dealer", SpawnCasinoWorkers::buildDealerShops,
            "Prize Manager", SpawnCasinoWorkers::buildPrizeManagerShops,
            "Food Merchant", SpawnCasinoWorkers::buildFoodMerchantShops
    );

    // Registrar callback
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(SpawnCasinoWorkers::register);
    }

    // Registro del comando (firma Fabric 1.21.x)
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("casinorocket")
                .then(CommandManager.literal("spawn")
                        .then(CommandManager.argument("type", StringArgumentType.greedyString())
                                .suggests((ctx, builder) -> {
                                    for (String k : OFFERS_TEMPLATES.keySet()) builder.suggest(k);
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    String type = StringArgumentType.getString(ctx, "type");
                                    return executeSpawn(type, ctx.getSource());
                                }))));
        CasinoRocket.LOGGER.info("Registered /casinorocket spawn command");
    }

    // Ejecuta: coloca el block, crea NBT (base + shops) y spawnea el villager A UN LADO del block
    private static int executeSpawn(String type, ServerCommandSource source) {
        Supplier<NbtList> shopsFactory = OFFERS_TEMPLATES.get(type);
        if (shopsFactory == null) {
            source.sendError(Text.literal("Tipo desconocido: " + type));
            return 0;
        }

        ServerWorld world = source.getWorld();
        BlockPos playerPos = BlockPos.ofFloored(source.getPosition());
        BlockPos blockPos = playerPos;            // colocamos el display_case en la posición del jugador
        // colocamos el villager a un lado del bloque (por ejemplo EAST)
        BlockPos villagerPos = blockPos.east();

        // 1) Obtener y colocar el bloque cobblemon:display_case
        Block displayCase = Registries.BLOCK.get(Identifier.of("cobblemon", "display_case"));
        if (displayCase == null) {
            source.sendError(Text.literal("No se encontró el bloque cobblemon:display_case. ¿Está Cobblemon/CobbleDollars instalado?"));
            return 0;
        }

        world.setBlockState(blockPos, displayCase.getDefaultState());

        // 2) Crear NBT base con jobPos apuntando al display_case
        NbtCompound root = VillagerNbtFactory.createBaseVillagerNbt(type, blockPos,
                "cobbledollars:cobble_merchant", // la profesión que reconoce CobbleDollars
                true,  // NoAI
                true   // PersistenceRequired
        );

        // 3) Obtener lista de shops (cada elemento = shop compound { Category, Offers })
        NbtList shops = shopsFactory.get();
        root.put("CobbleMerchantShop", shops);

        // 4) Crear entidad desde NBT
        Optional<Entity> maybeEntity;
        try {
            maybeEntity = EntityType.getEntityFromNbt(root, (World) world);
        } catch (Exception e) {
            CasinoRocket.LOGGER.error("Error creando entidad desde NBT", e);
            source.sendError(Text.literal("Error creando entidad desde NBT: " + e.getMessage()));
            return 0;
        }

        if (maybeEntity.isEmpty()) {
            source.sendError(Text.literal("No se pudo crear la entidad desde NBT (Optional vacío)."));
            return 0;
        }

        Entity entity = maybeEntity.get();

        // 5) Posicionar y spawnear
        entity.refreshPositionAndAngles(villagerPos.getX() + 0.5, villagerPos.getY(), villagerPos.getZ() + 0.5,
                source.getPlayer().getYaw(), 0f);

        boolean spawned;
        try {
            spawned = world.spawnEntity(entity);
        } catch (Exception e) {
            CasinoRocket.LOGGER.error("Excepción al spawnear entidad", e);
            source.sendError(Text.literal("Excepción al spawnear entidad: " + e.getMessage()));
            return 0;
        }

        if (!spawned) {
            source.sendError(Text.literal("No se pudo spawnear la entidad en el mundo."));
            return 0;
        }

        source.sendFeedback(() -> Text.literal("§a" + type + " spawneado correctamente al lado del display_case."), false);
        return 1;
    }

    // ----------------------------
    // PLANTILLAS: devuelven NbtList de "shop compounds"
    // ----------------------------

    // Dealer: una sola shop "Casino"
    private static NbtList buildDealerShops() {
        NbtList shops = new NbtList();
        NbtList offers = new NbtList();
        offers.add(makeSimpleOffer("casinorocket:basic_chip", 1, "100"));
        offers.add(makeSimpleOffer("casinorocket:copper_chip", 1, "1000"));
        offers.add(makeSimpleOffer("casinorocket:iron_chip", 1, "5000"));
        offers.add(makeSimpleOffer("casinorocket:amethyst_chip", 1, "25000"));
        offers.add(makeSimpleOffer("casinorocket:golden_chip", 1, "100000"));
        offers.add(makeSimpleOffer("casinorocket:emerald_chip", 1, "1000000"));
        offers.add(makeSimpleOffer("casinorocket:diamond_chip", 1, "10000000"));
        offers.add(makeSimpleOffer("casinorocket:netherite_chip", 1, "100000000"));

        shops.add(makeShopCompound("Casino", offers));
        return shops;
    }

    // Prize Manager: devuelve varias shops (Pokemon + Fossils)
    private static NbtList buildPrizeManagerShops() {
        NbtList shops = new NbtList();

        // --- Pokemon shop (offers con 'components') ---
        NbtList pokemonOffers = new NbtList();

        pokemonOffers.add(makeOfferWithComponents("cobbreeding:pokemon_egg", 1,
                new Object[][] {
                        { "cobbreeding:second", 1 },
                        { "cobbreeding:timer", 23020 },
                        { "cobbreeding:egg_info", "dratini" }
                }, "100000"));

        pokemonOffers.add(makeOfferWithComponents("cobbreeding:pokemon_egg", 1,
                new Object[][] {
                        { "cobbreeding:second", 11 },
                        { "cobbreeding:timer", 23580 },
                        { "cobbreeding:egg_info", "deino" }
                }, "100000"));

        pokemonOffers.add(makeOfferWithComponents("cobbreeding:pokemon_egg", 1,
                new Object[][] {
                        { "cobbreeding:second", 4 },
                        { "cobbreeding:timer", 23180 },
                        { "cobbreeding:egg_info", "axew" }
                }, "100000"));

        pokemonOffers.add(makeOfferWithComponents("cobbreeding:pokemon_egg", 1,
                new Object[][] {
                        { "cobbreeding:second", 6 },
                        { "cobbreeding:timer", 23720 },
                        { "cobbreeding:egg_info", "gible" }
                }, "100000"));

        shops.add(makeShopCompound("Pokemon", pokemonOffers));

        // --- Fossils shop ---
        NbtList fossilOffers = new NbtList();
        String[] fossils = new String[]{
                "cobblemon:helix_fossil", "cobblemon:dome_fossil", "cobblemon:old_amber_fossil",
                "cobblemon:root_fossil", "cobblemon:claw_fossil", "cobblemon:skull_fossil",
                "cobblemon:armor_fossil", "cobblemon:cover_fossil", "cobblemon:plume_fossil",
                "cobblemon:jaw_fossil", "cobblemon:sail_fossil", "cobblemon:fossilized_bird",
                "cobblemon:fossilized_fish", "cobblemon:fossilized_drake", "cobblemon:fossilized_dino"
        };
        for (String id : fossils) {
            fossilOffers.add(makeSimpleOffer(id, 1, "50000"));
        }
        shops.add(makeShopCompound("Fossils", fossilOffers));

        return shops;
    }


    // Food Merchant: Curries, Sandwiches, Special Food
    private static NbtList buildFoodMerchantShops() {
        NbtList shops = new NbtList();

        NbtList curries = new NbtList();
        curries.add(makeSimpleOffer("cobblecuisine:bitter_leek_curry", 1, "5000"));
        curries.add(makeSimpleOffer("cobblecuisine:dry_frozen_curry", 1, "5000"));
        curries.add(makeSimpleOffer("cobblecuisine:salty_boiled_egg_curry", 1, "5000"));
        curries.add(makeSimpleOffer("cobblecuisine:bitter_herb_medley_curry", 1, "5000"));
        curries.add(makeSimpleOffer("cobblecuisine:dry_bone_curry", 1, "5000"));
        curries.add(makeSimpleOffer("cobblecuisine:spicy_mushroom_medley_curry", 1, "5000"));
        curries.add(makeSimpleOffer("cobblecuisine:sweet_apple_curry", 1, "5000"));
        curries.add(makeSimpleOffer("cobblecuisine:spicy_potato_curry", 1, "5000"));
        curries.add(makeSimpleOffer("cobblecuisine:sweet_whipped_cream_curry", 1, "5000"));
        shops.add(makeShopCompound("Curries", curries));

        NbtList sandwiches = new NbtList();
        sandwiches.add(makeSimpleOffer("cobblecuisine:sour_pickle_sandwich", 1, "5000"));
        sandwiches.add(makeSimpleOffer("cobblecuisine:spicy_five_alarm_sandwich", 1, "5000"));
        sandwiches.add(makeSimpleOffer("cobblecuisine:salty_vegetable_sandwich", 1, "5000"));
        sandwiches.add(makeSimpleOffer("cobblecuisine:sour_zesty_sandwich", 1, "5000"));
        sandwiches.add(makeSimpleOffer("cobblecuisine:sweet_potato_salad_sandwich", 1, "5000"));
        sandwiches.add(makeSimpleOffer("cobblecuisine:sweet_jam_sandwich", 1, "5000"));
        sandwiches.add(makeSimpleOffer("cobblecuisine:spicy_claw_sandwich", 1, "5000"));
        sandwiches.add(makeSimpleOffer("cobblecuisine:bitter_jambon_beurre", 1, "5000"));
        sandwiches.add(makeSimpleOffer("cobblecuisine:salty_egg_sandwich", 1, "5000"));
        shops.add(makeShopCompound("Sandwiches", sandwiches));

        NbtList special = new NbtList();
        special.add(makeSimpleOffer("cobblecuisine:dry_curry", 1, "10000"));
        special.add(makeSimpleOffer("cobblecuisine:fruity_flan", 1, "50000"));
        special.add(makeSimpleOffer("cobblecuisine:eclair", 1, "50000"));
        special.add(makeSimpleOffer("cobblecuisine:dry_smoked_tail_curry", 1, "100000"));
        special.add(makeSimpleOffer("cobblecuisine:bean_medley_curry", 1, "500000"));
        shops.add(makeShopCompound("Special Food", special));

        return shops;
    }

    // ----------------------------
    // HELPERS para construir shops / offers
    // ----------------------------

    // Make a shop compound: { Category: "...", Offers: [ ... ] }
    private static NbtCompound makeShopCompound (String category, NbtList offers){
        NbtCompound shop = new NbtCompound();
        shop.putString("Category", category);
        shop.put("Offers", offers);
        return shop;
    }

    // Make a simple offer: { Item: { id, count }, Price: "..." }
    private static NbtCompound makeSimpleOffer (String itemId,int count, String price){
        NbtCompound offer = new NbtCompound();
        NbtCompound item = new NbtCompound();
        item.putString("id", itemId);
        item.putByte("Count", (byte) count);
        item.putInt("count", count);
        offer.put("Item", item);
        offer.putString("Price", price);
        return offer;
    }

    /**
     * Make an offer where the item has an inner 'components' compound.
     * componentsData is passed as an Object[][] where each row is { key, value }.
     * Values may be Integer, Long, Double, Float, Boolean or String.
     */
    private static NbtCompound makeOfferWithComponents (String itemId,int count, Object[][] componentsData, String price){
        NbtCompound offer = new NbtCompound();
        NbtCompound item = new NbtCompound();
        item.putString("id", itemId);
        item.putByte("Count", (byte) count);
        item.putInt("count", count);

        NbtCompound comps = new NbtCompound();
        if (componentsData != null) {
            for (Object[] pair : componentsData) {
                String key = String.valueOf(pair[0]);
                Object val = pair[1];
                if (val instanceof Integer) comps.putInt(key, (Integer) val);
                else if (val instanceof Long) comps.putLong(key, (Long) val);
                else if (val instanceof Double) comps.putDouble(key, (Double) val);
                else if (val instanceof Float) comps.putFloat(key, (Float) val);
                else if (val instanceof Boolean) comps.putBoolean(key, (Boolean) val);
                else comps.putString(key, String.valueOf(val));
            }
        }
        item.put("components", comps);

        offer.put("Item", item);
        offer.putString("Price", price);
        return offer;
    }
}