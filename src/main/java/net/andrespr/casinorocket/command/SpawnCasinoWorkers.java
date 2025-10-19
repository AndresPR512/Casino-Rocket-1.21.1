package net.andrespr.casinorocket.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
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
import net.minecraft.registry.Registries;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class SpawnCasinoWorkers {

    private static final Map<String, Supplier<NbtList>> OFFERS_TEMPLATES = Map.of(
            "Dealer", SpawnCasinoWorkers::buildDealerShops,
            "Prize Dealer", SpawnCasinoWorkers::buildPrizeDealerShops,
            "Snackmaster", SpawnCasinoWorkers::buildSnackmasterShops,
            "TM Instructor", SpawnCasinoWorkers::buildTMInstructorShops,
            "Battle Gear Merchant", SpawnCasinoWorkers::buildBattleGearMerchantShops
    );

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(SpawnCasinoWorkers::register);
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("casinorocket").requires(source -> source.hasPermissionLevel(2))
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

    private static int executeSpawn(String type, ServerCommandSource source) {

        Supplier<NbtList> shopsFactory = OFFERS_TEMPLATES.get(type);
        if (shopsFactory == null) {
            source.sendError(Text.literal("Unknown casino worker type: " + type));
            return 0;
        }

        ServerWorld world = source.getWorld();
        BlockPos blockPos = BlockPos.ofFloored(source.getPosition());
        BlockPos villagerPos = blockPos.east();

        Block displayCase = Registries.BLOCK.get(Identifier.of("cobblemon", "display_case"));
        world.setBlockState(blockPos, displayCase.getDefaultState());

        NbtCompound root = VillagerNbtFactory.createBaseVillagerNbt(type, blockPos, "cobbledollars:cobble_merchant");
        NbtList shops = shopsFactory.get();
        root.put("CobbleMerchantShop", shops);

        Optional<Entity> maybeEntity;
        try {
            maybeEntity = EntityType.getEntityFromNbt(root, world);
        } catch (Exception e) {
            CasinoRocket.LOGGER.error("Error creating entity from NBT", e);
            source.sendError(Text.literal("Error creating entity from NBT: " + e.getMessage()));
            return 0;
        }
        if (maybeEntity.isEmpty()) {
            source.sendError(Text.literal("Error creating entity from NBT (Optional empty)."));
            return 0;
        }
        Entity entity = maybeEntity.get();

        entity.refreshPositionAndAngles(villagerPos.getX() + 0.5, villagerPos.getY(), villagerPos.getZ() + 0.5,
                Objects.requireNonNull(source.getPlayer()).getYaw(), 0f);

        boolean spawned;
        try {
            spawned = world.spawnEntity(entity);
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

    // Dealer Shop
    private static NbtList buildDealerShops() {
        NbtList shops = new NbtList();

        NbtList chipOffers = new NbtList();
        chipOffers.add(makeSimpleOffer("casinorocket:basic_chip", String.valueOf(ModItems.BASIC_CHIP.getValue())));
        chipOffers.add(makeSimpleOffer("casinorocket:copper_chip", String.valueOf(ModItems.COPPER_CHIP.getValue())));
        chipOffers.add(makeSimpleOffer("casinorocket:iron_chip", String.valueOf(ModItems.IRON_CHIP.getValue())));
        chipOffers.add(makeSimpleOffer("casinorocket:amethyst_chip", String.valueOf(ModItems.AMETHYST_CHIP.getValue())));
        chipOffers.add(makeSimpleOffer("casinorocket:golden_chip", String.valueOf(ModItems.GOLDEN_CHIP.getValue())));
        chipOffers.add(makeSimpleOffer("casinorocket:emerald_chip", String.valueOf(ModItems.EMERALD_CHIP.getValue())));
        chipOffers.add(makeSimpleOffer("casinorocket:diamond_chip", String.valueOf(ModItems.DIAMOND_CHIP.getValue())));
        chipOffers.add(makeSimpleOffer("casinorocket:netherite_chip", String.valueOf(ModItems.NETHERITE_CHIP.getValue())));
        shops.add(makeShopCompound("Chips", chipOffers));

        return shops;
    }

    // Prize Dealer Shop
    private static NbtList buildPrizeDealerShops() {
        NbtList shops = new NbtList();

        NbtList pokemonOffers = new NbtList();
        pokemonOffers.add(makeSimpleOffer("casinorocket:litwick_pin", "10000"));
        pokemonOffers.add(makeSimpleOffer("casinorocket:staryu_pin", "10000"));
        pokemonOffers.add(makeSimpleOffer("casinorocket:bellsprout_pin", "10000"));
        pokemonOffers.add(makeSimpleOffer("casinorocket:tyrogue_pin", "25000"));
        pokemonOffers.add(makeSimpleOffer("casinorocket:scyther_pin", "50000"));
        pokemonOffers.add(makeSimpleOffer("casinorocket:eevee_pin", "75000"));
        pokemonOffers.add(makeSimpleOffer("casinorocket:dratini_pin", "100000"));
        pokemonOffers.add(makeSimpleOffer("casinorocket:rotom_pin", "250000"));
        pokemonOffers.add(makeSimpleOffer("casinorocket:ditto_pin", "500000"));
        pokemonOffers.add(makeSimpleOffer("casinorocket:porygon_pin", "999999"));
        shops.add(makeShopCompound("Pokemon", pokemonOffers));

        NbtList fossilOffers = new NbtList();
        fossilOffers.add(makeSimpleOffer("cobblemon:helix_fossil","50000"));
        fossilOffers.add(makeSimpleOffer("cobblemon:dome_fossil","50000"));
        fossilOffers.add(makeSimpleOffer("cobblemon:root_fossil","75000"));
        fossilOffers.add(makeSimpleOffer("cobblemon:claw_fossil","75000"));
        fossilOffers.add(makeSimpleOffer("cobblemon:old_amber_fossil","100000"));
        shops.add(makeShopCompound("Fossils", fossilOffers));

        return shops;
    }

    // Snackmaster Shop
    private static NbtList buildSnackmasterShops() {
        NbtList shops = new NbtList();

        NbtList curryOffers = new NbtList();
        List<String> curries = List.of(
                "bitter_leek_curry",
                "dry_frozen_curry",
                "salty_boiled_egg_curry",
                "bitter_herb_medley_curry",
                "bitter_herb_medley_curry",
                "dry_bone_curry",
                "spicy_mushroom_medley_curry",
                "sweet_apple_curry",
                "spicy_potato_curry",
                "sweet_whipped_cream_curry"
        );
        for(String curry : curries) { curryOffers.add(makeSimpleOffer("cobblecuisine:" + curry, "10000")); }
        shops.add(makeShopCompound("Curries", curryOffers));

        NbtList sandwichOffers = new NbtList();
        List<String> sandwiches = List.of(
                "sour_pickle_sandwich",
                "spicy_five_alarm_sandwich",
                "salty_vegetable_sandwich",
                "sour_zesty_sandwich",
                "sweet_potato_salad_sandwich",
                "sweet_jam_sandwich",
                "spicy_claw_sandwich",
                "bitter_jambon_beurre",
                "salty_egg_sandwich"
        );
        for(String sandwich : sandwiches) { sandwichOffers.add(makeSimpleOffer("cobblecuisine:" + sandwich, "10000")); }
        shops.add(makeShopCompound("Sandwiches", sandwichOffers));

        NbtList specialFoodOffers = new NbtList();
        specialFoodOffers.add(makeSimpleOffer("cobblecuisine:dry_curry", "25000"));
        specialFoodOffers.add(makeSimpleOffer("cobblecuisine:fruity_flan", "50000"));
        specialFoodOffers.add(makeSimpleOffer("cobblecuisine:eclair", "50000"));
        specialFoodOffers.add(makeSimpleOffer("cobblecuisine:dry_smoked_tail_curry", "100000"));
        specialFoodOffers.add(makeSimpleOffer("cobblecuisine:bean_medley_curry", "1000000"));
        shops.add(makeShopCompound("Special Food", specialFoodOffers));

        return shops;
    }

    // TM Instructor Shop
    private static NbtList buildTMInstructorShops() {
        NbtList shops = new NbtList();

        NbtList attackTMsOffers = new NbtList();
        attackTMsOffers.add(makeSimpleOffer("tmcraft:tm_ember","1000"));
        attackTMsOffers.add(makeSimpleOffer("tmcraft:tm_watergun","1000"));
        attackTMsOffers.add(makeSimpleOffer("tmcraft:tm_razorleaf","1000"));
        attackTMsOffers.add(makeSimpleOffer("tmcraft:tm_mudslap","1000"));
        attackTMsOffers.add(makeSimpleOffer("tmcraft:tm_thundershock","1500"));
        attackTMsOffers.add(makeSimpleOffer("tmcraft:tm_icywind","2000"));
        attackTMsOffers.add(makeSimpleOffer("tmcraft:tm_confusion","3000"));
        attackTMsOffers.add(makeSimpleOffer("tmcraft:tm_rockthrow","3000"));
        shops.add(makeShopCompound("Attack TM's", attackTMsOffers));

        NbtList statusTMsOffers = new NbtList();
        statusTMsOffers.add(makeSimpleOffer("tmcraft:tm_supersonic","1500"));
        statusTMsOffers.add(makeSimpleOffer("tmcraft:tm_protect","2000"));
        statusTMsOffers.add(makeSimpleOffer("tmcraft:tm_leechseed","2500"));
        statusTMsOffers.add(makeSimpleOffer("tmcraft:tm_thunderwave","3000"));
        statusTMsOffers.add(makeSimpleOffer("tmcraft:tm_substitute","5000"));
        statusTMsOffers.add(makeSimpleOffer("tmcraft:tm_taunt","5000"));
        shops.add(makeShopCompound("Status TM's", statusTMsOffers));

        return shops;
    }

    // Battle Gear Merchant Shop
    private static NbtList buildBattleGearMerchantShops() {
        NbtList shops = new NbtList();

        NbtList battleOffers = new NbtList();
        battleOffers.add(makeSimpleOffer("cobblemon:sitrus_berry","2500"));
        battleOffers.add(makeSimpleOffer("cobblemon:muscle_band","5000"));
        battleOffers.add(makeSimpleOffer("cobblemon:wise_glasses","5000"));
        battleOffers.add(makeSimpleOffer("cobblemon:metronome","7500"));
        battleOffers.add(makeSimpleOffer("cobblemon:shell_bell","7500"));
        battleOffers.add(makeSimpleOffer("cobblemon:focus_band","10000"));
        battleOffers.add(makeSimpleOffer("cobblemon:scope_lens","10000"));
        battleOffers.add(makeSimpleOffer("cobblemon:quick_claw","10000"));
        battleOffers.add(makeSimpleOffer("cobblemon:eviolite","20000"));
        shops.add(makeShopCompound("Battle Items", battleOffers));

        NbtList typeBoostOffers = new NbtList();
        List<String> typeBoostItems = List.of(
                "silk_scarf",
                "charcoal_stick",
                "mystic_water",
                "miracle_seed",
                "magnet",
                "never_melt_ice",
                "hard_stone",
                "sharp_beak",
                "black_belt",
                "soft_sand",
                "silver_powder",
                "poison_barb",
                "twisted_spoon",
                "spell_tag",
                "black_glasses",
                "metal_coat",
                "dragon_fang",
                "fairy_feather"
        );
        for(String typeBoostItem : typeBoostItems) { typeBoostOffers.add(makeSimpleOffer("cobblemon:" + typeBoostItem, "10000")); }
        shops.add(makeShopCompound("Type Boost", typeBoostOffers));

        return shops;
    }

    /** Helpers */

    private static NbtCompound makeShopCompound(String category, NbtList offers) {
        NbtCompound shop = new NbtCompound();
        shop.putString("Category", category);
        shop.put("Offers", offers);
        return shop;
    }

    private static NbtCompound makeSimpleOffer(String itemId, String price) {
        NbtCompound offer = new NbtCompound();
        NbtCompound item = new NbtCompound();
        item.putString("id", itemId);
        item.putByte("Count", (byte) 1);
        item.putInt("count", 1);
        offer.put("Item", item);
        offer.putString("Price", price);
        return offer;
    }

    private static NbtCompound makeOfferWithComponents(String itemId, Object[][] componentsData, String price) {
        NbtCompound offer = new NbtCompound();
        NbtCompound item = new NbtCompound();
        item.putString("id", itemId);
        item.putByte("Count", (byte) 1);
        item.putInt("count", 1);

        NbtCompound comps = new NbtCompound();
        if (componentsData != null) {
            for (Object[] pair : componentsData) {
                String key = String.valueOf(pair[0]);
                Object val = pair[1];
                switch (val) {
                    case Integer i -> comps.putInt(key, i);
                    case Long l -> comps.putLong(key, l);
                    case Double v -> comps.putDouble(key, v);
                    case Float v -> comps.putFloat(key, v);
                    case Boolean b -> comps.putBoolean(key, b);
                    case null, default -> comps.putString(key, String.valueOf(val));
                }
            }
        }
        item.put("components", comps);

        offer.put("Item", item);
        offer.putString("Price", price);
        return offer;
    }

}