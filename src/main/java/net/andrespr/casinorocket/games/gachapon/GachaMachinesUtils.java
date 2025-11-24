package net.andrespr.casinorocket.games.gachapon;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.custom.PokemonGachaMachineBlock;
import net.andrespr.casinorocket.config.GachaMachinesConfig;
import net.andrespr.casinorocket.data.GachaDataStorage;
import net.andrespr.casinorocket.data.GachaStats;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.sound.ModSounds;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.List;

public class GachaMachinesUtils {

    private static final int DELAY_TICKS = 20;
    private static final Object2LongOpenHashMap<UUID> PLAYER_COOLDOWNS = new Object2LongOpenHashMap<>();

    private static final Object2ObjectOpenHashMap<BlockPos, UUID> LAST_PLAYER_USED = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<UUID, BlockPos> LAST_MACHINE_USED = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<BlockPos, String> LAST_COIN_USED = new Object2ObjectOpenHashMap<>();

    private static final Set<Long> PENDING_PREMIER_BONUS = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static final Map<Long, UUID> PENDING_PREMIER_USER = new java.util.concurrent.ConcurrentHashMap<>();

    private static final Locale LOCALE = Locale.ROOT;

    private static final Map<UUID, Long> CLEAN_CONFIRMATION = new HashMap<>();

    // === EXECUTED WHEN A COIN IS DEPOSITED ===
    public static ActionResult handleUse(World world, BlockPos pos, PlayerEntity player, String coinKey) {
        if (world.isClient) return ActionResult.SUCCESS;

        final long currentTick = world.getTime();
        final long machineKey = pos.asLong();

        PLAYER_COOLDOWNS.object2LongEntrySet().removeIf(e -> currentTick > e.getLongValue());
        UUID currentUser = LAST_PLAYER_USED.get(pos);

        if (currentUser != null && !currentUser.equals(player.getUuid()) || PENDING_PREMIER_BONUS.contains(machineKey)) {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_another_occupied", true);
            return ActionResult.FAIL;
        }

        LAST_MACHINE_USED.put(player.getUuid(), pos);

        long playerCooldownEnd = PLAYER_COOLDOWNS.getLong(player.getUuid());
        if (currentTick < playerCooldownEnd) {
            BlockPos lastUsed = LAST_MACHINE_USED.get(player.getUuid());
            if (lastUsed != null && lastUsed.equals(pos)) {
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_occupied", true);
            } else {
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_another_occupied", true);
            }
            return ActionResult.FAIL;
        }

        player.getMainHandStack().decrement(1);

        PLAYER_COOLDOWNS.put(player.getUuid(), currentTick + DELAY_TICKS);
        LAST_COIN_USED.put(pos, coinKey);
        LAST_PLAYER_USED.put(pos, player.getUuid());

        world.playSound(null, pos, ModSounds.INSERTING_COIN, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), DELAY_TICKS);
        CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_coin_inserted", false);

        return ActionResult.SUCCESS;
    }

    // === EXECUTED AFTER THE DELAY, TO GIVE THE PRIZE ===
    public static void finishDispense(ServerWorld world, BlockPos pos, Direction facing) {
        final long key = pos.asLong();
        final long currentTick = world.getTime();

        // === CHECKING BONUS TICK ===
        if (PENDING_PREMIER_BONUS.remove(key)) {
            UUID userId = PENDING_PREMIER_USER.remove(key);
            PlayerEntity user = userId != null ? world.getPlayerByUuid(userId) : null;
            if (user == null) return;

            boolean pokemon = world.getBlockState(pos).getBlock() instanceof PokemonGachaMachineBlock;
            givePremierBonus(world, pos, facing, user, pokemon);

            LAST_COIN_USED.remove(pos);
            LAST_PLAYER_USED.remove(pos);
            return;
        }

        // === REGULAR PRIZE ===
        UUID uuid = LAST_PLAYER_USED.get(pos);
        PlayerEntity user = uuid != null ? world.getPlayerByUuid(uuid) : null;

        BlockState state = world.getBlockState(pos);
        boolean pokemon = state.getBlock() instanceof PokemonGachaMachineBlock;

        String coinKey = LAST_COIN_USED.getOrDefault(pos, "copper");

        Map<String, Double> probs = CasinoRocket.CONFIG.gachaMachines.normalizedProbabilities(coinKey);
        Map<String, Double> pityAdjusted = applyPity(probs, coinKey, user);

        String rarity = pickWeightedRarity(world.getRandom(), pityAdjusted);
        ItemStack reward = getRewardForRarity(rarity, pokemon);

        if (rarity.equalsIgnoreCase("legendary") && user != null) {
            GachaDataStorage data = GachaDataStorage.get(requireServer(user.getWorld()));
            Map<String, Integer> playerMap = data.pityTracker.get(user.getUuid());
            if (playerMap != null) {
                playerMap.put(coinKey, 0);
                data.markDirty();
                CasinoRocket.LOGGER.debug("[GachaMachines-Pity] {} pity reset for {}", coinKey, user.getName().getString());
            }
        }

        spawnRarityParticles(world, pos, rarity);
        playRaritySound(world, pos, rarity);
        spawnFireworkByRarity(world, pos, rarity);
        dropFromFront(world, pos, facing, reward);

        boolean scheduledBonus = false;

        if (user != null) {
            GachaDataStorage data = GachaDataStorage.get(world.getServer());
            GachaStats stats = data.playerStats.computeIfAbsent(user.getUuid(),
                    k -> new GachaStats(user.getName().getString()));
            stats.setPlayerName(user.getName().getString());
            stats.recordUse(coinKey, rarity);
            data.markDirty();

            var config = CasinoRocket.CONFIG.gachaMachines.premier_bonus;
            if (config.enable && stats.getTotalCoinsUsed() % config.coinsToBonus == 0) {
                PENDING_PREMIER_BONUS.add(key);
                PENDING_PREMIER_USER.put(key, user.getUuid());
                world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 30);
                scheduledBonus = true;
            }
        }

        giveUserFeedback(probs, coinKey, reward, user);

        if (!scheduledBonus || user == null) {
            LAST_COIN_USED.remove(pos);
            LAST_PLAYER_USED.remove(pos);
        }

        if (user != null) {
            int rarityDelay = getRarityDelayTicks(rarity);
            if (scheduledBonus && rarityDelay < 60) rarityDelay = 60;
            long nextAvailable = currentTick + rarityDelay;
            PLAYER_COOLDOWNS.put(user.getUuid(), nextAvailable);
            LAST_MACHINE_USED.put(user.getUuid(), pos);
        }

    }

    // ===== APPLY PITY TO BASE PROBS =====
    public static Map<String, Double> applyPity(Map<String, Double> probs, String coinKey, PlayerEntity player) {
        if (!CasinoRocket.CONFIG.gachaMachines.pity.enable) return probs;
        if (player == null) return probs;

        GachaDataStorage data = GachaDataStorage.get(requireServer(player.getWorld()));
        var pityData = getPityData(coinKey);
        if (pityData == null || pityData.usesToMax <= 0 || pityData.maxLegendaryChance <= 0) return probs;

        UUID id = player.getUuid();
        Map<String, Integer> playerMap = data.pityTracker.computeIfAbsent(id, k -> new HashMap<>());
        int uses = playerMap.getOrDefault(coinKey, 0) + 1;
        playerMap.put(coinKey, uses);

        double baseLegendary = probs.getOrDefault("legendary", 0.0);
        double t = Math.min(1.0, (double) uses / pityData.usesToMax);
        double newLegendary = baseLegendary + (pityData.maxLegendaryChance - baseLegendary) * t;

        Map<String, Double> adjusted = new HashMap<>(probs);
        double diff = newLegendary - baseLegendary;

        if (diff > 0) {
            double totalOther = 1.0 - baseLegendary;
            for (String key : adjusted.keySet()) {
                if (!key.equals("legendary")) {
                    adjusted.put(key, adjusted.get(key) * (1.0 - diff) / totalOther);
                }
            }
            adjusted.put("legendary", newLegendary);
        }

        CasinoRocket.LOGGER.debug("[GachaMachines-Pity] {} used {} {} times → legendary {}%", player.getName().getString(), coinKey, uses, newLegendary * 100);

        data.markDirty();

        return adjusted;
    }

    // ===== PICK RARITY =====
    public static String pickWeightedRarity(Random random, Map<String, Double> probs) {
        double r = random.nextDouble();
        double cumulative = 0.0;
        for (var e : probs.entrySet()) {
            cumulative += e.getValue();
            if (r <= cumulative) return e.getKey();
        }
        return "common";
    }

    // ===== REWARD GETTERS =====

    // Randomly picked Rarity -> Corresponding ItemStack
    public static ItemStack getRewardForRarity(String rarity, boolean pokemon) {
        String key = rarity.toLowerCase(LOCALE);
        ItemStack reward;

        if (pokemon) {
            reward = switch (key) {
                case "common" -> new ItemStack(ModItems.POKEMON_POKE_GACHAPON);
                case "uncommon" -> new ItemStack(ModItems.POKEMON_GREAT_GACHAPON);
                case "rare" -> new ItemStack(ModItems.POKEMON_ULTRA_GACHAPON);
                case "ultrarare" -> new ItemStack(ModItems.POKEMON_MASTER_GACHAPON);
                case "legendary" -> new ItemStack(ModItems.POKEMON_CHERISH_GACHAPON);
                case "bonus" -> new ItemStack(ModItems.POKEMON_PREMIER_GACHAPON);
                default -> ItemStack.EMPTY;
            };
        } else {
            reward = switch (key) {
                case "common" -> new ItemStack(ModItems.POKE_GACHAPON);
                case "uncommon" -> new ItemStack(ModItems.GREAT_GACHAPON);
                case "rare" -> new ItemStack(ModItems.ULTRA_GACHAPON);
                case "ultrarare" -> new ItemStack(ModItems.MASTER_GACHAPON);
                case "legendary" -> new ItemStack(ModItems.CHERISH_GACHAPON);
                case "bonus" -> new ItemStack(ModItems.PREMIER_GACHAPON);
                default -> ItemStack.EMPTY;
            };
        }

        if (reward.isEmpty()) {
            CasinoRocket.LOGGER.warn("[GachaMachines] No valid reward found for rarity '{}'", rarity);
        }

        return reward;
    }

    // Used to give the premier ball bonus prize
    private static void givePremierBonus(ServerWorld world, BlockPos pos, Direction facing, PlayerEntity user, boolean pokemon) {
        String bonusKey = "bonus";
        ItemStack bonus = getRewardForRarity(bonusKey, pokemon);

        spawnRarityParticles(world, pos, bonusKey);
        playRaritySound(world, pos, bonusKey);
        dropFromFront(world, pos, facing, bonus);

        GachaDataStorage data = GachaDataStorage.get(world.getServer());
        GachaStats stats = data.playerStats.computeIfAbsent(user.getUuid(),
                k -> new GachaStats(user.getName().getString()));
        stats.recordBonus();
        data.markDirty();

        CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_bonus_prize", true);
    }

    // ===== VISUAL AND AUDIO EFFECTS =====

    // SOUND: Picked rarity -> Play special sound
    public static void playRaritySound(World world, BlockPos pos, String rarity) {
        if (world.isClient) return;

        SoundEvent sound = switch (rarity.toLowerCase(LOCALE)) {
            case "common" -> ModSounds.COMMON_PRIZE;
            case "uncommon" -> ModSounds.UNCOMMON_PRIZE;
            case "rare" -> ModSounds.RARE_PRIZE;
            case "ultrarare" -> ModSounds.ULTRARARE_PRIZE;
            case "legendary" -> ModSounds.LEGENDARY_PRIZE;
            case "bonus" -> ModSounds.BONUS_PRIZE;
            default -> null;
        };

        if (sound == null) {
            CasinoRocket.LOGGER.warn("[GachaMachines] No sound event defined for rarity '{}' at {}", rarity, pos);
            return;
        }

        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);

    }

    // Particles: Picked rarity -> Spawn special particles
    public static void spawnRarityParticles(ServerWorld world, BlockPos pos, String rarity) {
        ParticleEffect particle = switch (rarity.toLowerCase(LOCALE)) {
            case "common" -> ParticleTypes.END_ROD;
            case "uncommon" -> ParticleTypes.HAPPY_VILLAGER;
            case "rare" -> ParticleTypes.FIREWORK;
            case "ultrarare" -> ParticleTypes.DRAGON_BREATH;
            case "legendary" -> ParticleTypes.FLAME;
            case "bonus" -> ParticleTypes.GLOW;
            default -> null;
        };

        if (particle == null) {
            CasinoRocket.LOGGER.warn("[GachaMachines] No particle effect defined for rarity '{}' at {}", rarity, pos);
            return;
        }

        world.spawnParticles(particle, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                20, 0.3, 0.3, 0.3, 0.02);
    }

    // Fireworks: Picked rarity -> Spawn special firework (Only for ultrarare/legendary)
    public static void spawnFireworkByRarity(ServerWorld world, BlockPos pos, String rarity) {
        int flight = 0;
        boolean trail = true;
        boolean twinkle = true;
        int color;
        FireworkExplosionComponent.Type shape;

        switch (rarity.toLowerCase(LOCALE)) {
            case "ultrarare" -> {
                color = 0x8000FF; // PURPLE
                shape = FireworkExplosionComponent.Type.SMALL_BALL;
            }
            case "legendary" -> {
                color = 0xFF0000; // RED
                shape = FireworkExplosionComponent.Type.STAR;
            }
            default -> {
                return;
            }
        }

        IntArrayList colors = new IntArrayList(new int[]{color});

        FireworkExplosionComponent explosion = new FireworkExplosionComponent(
                shape, colors, new IntArrayList(), trail, twinkle);

        FireworksComponent fwComponent = new FireworksComponent(flight, List.of(explosion));

        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
        rocket.set(DataComponentTypes.FIREWORKS, fwComponent);

        FireworkRocketEntity entity = new FireworkRocketEntity(
                world, pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5, rocket);

        world.spawnEntity(entity);
    }

    // Feedback: Notification of item gotten & pity update (If active)
    public static void giveUserFeedback(Map<String, Double> probs, String coinKey, ItemStack reward, PlayerEntity user) {

        if (user == null) return;
        CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_reward", true, reward.getName());

        if ((coinKey.equals("gold") || coinKey.equals("diamond")) && CasinoRocket.CONFIG.gachaMachines.pity.pityUpdateMessages) {

            GachaDataStorage data = GachaDataStorage.get(requireServer(user.getWorld()));
            Map<String, Integer> playerMap = data.pityTracker.get(user.getUuid());
            double pityChance = getCurrentLegendaryChance(coinKey, user, probs);
            int maxUses = getMaxUses(coinKey);

            MutableText coinName = Objects.requireNonNull(getCoinStack(coinKey)).getName().copy();
            String formattedChance = String.format("%.2f", pityChance * 100.0);

            if (playerMap != null && playerMap.getOrDefault(coinKey, 0) == 0) {
                CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_pity_reset", false,
                        coinName.formatted(Formatting.GOLD), Text.literal(formattedChance).formatted(Formatting.YELLOW));
            } else if (playerMap != null && playerMap.getOrDefault(coinKey, 0) >= maxUses) {
                CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_pity_max", false,
                        coinName.formatted(Formatting.GOLD), Text.literal(formattedChance).formatted(Formatting.YELLOW));
            } else {
                CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_pity_update", false,
                        coinName.formatted(Formatting.GOLD), Text.literal(formattedChance).formatted(Formatting.YELLOW));
            }

        }

    }

    // ===== GETTERS =====

    public static String getCoinKey(ItemStack stack) {
        if (stack.isOf(ModItems.COPPER_COIN)) return "copper";
        if (stack.isOf(ModItems.IRON_COIN)) return "iron";
        if (stack.isOf(ModItems.GOLD_COIN)) return "gold";
        if (stack.isOf(ModItems.DIAMOND_COIN)) return "diamond";
        return null;
    }

    public static ItemStack getCoinStack(String coinKey) {
        return switch (coinKey.toLowerCase(LOCALE)) {
            case "copper" -> new ItemStack(ModItems.COPPER_COIN);
            case "iron" -> new ItemStack(ModItems.IRON_COIN);
            case "gold" -> new ItemStack(ModItems.GOLD_COIN);
            case "diamond" -> new ItemStack(ModItems.DIAMOND_COIN);
            default -> ItemStack.EMPTY;
        };
    }

    public static GachaMachinesConfig.PityConfig.CoinPity getPityData(String coinKey) {
        return switch (coinKey) {
            case "iron" -> CasinoRocket.CONFIG.gachaMachines.pity.iron;
            case "gold" -> CasinoRocket.CONFIG.gachaMachines.pity.gold;
            case "diamond" -> CasinoRocket.CONFIG.gachaMachines.pity.diamond;
            default -> null;
        };
    }

    public static double getCurrentLegendaryChance(String coinKey, PlayerEntity player, Map<String, Double> baseProbs) {
        GachaDataStorage data = GachaDataStorage.get(requireServer(player.getWorld()));
        Map<String, Integer> playerMap = data.pityTracker.get(player.getUuid());
        if (playerMap == null) return baseProbs.getOrDefault("legendary", 0.0);

        int uses = playerMap.getOrDefault(coinKey, 0);
        var pityData = getPityData(coinKey);
        if (pityData == null) return baseProbs.getOrDefault("legendary", 0.0);

        double base = baseProbs.getOrDefault("legendary", 0.0);
        double t = Math.min(1.0, (double) uses / pityData.usesToMax);
        return base + (pityData.maxLegendaryChance - base) * t;
    }

    public static int getMaxUses(String coinKey) {
        var pityData = getPityData(coinKey);
        return pityData != null ? pityData.usesToMax : 1;
    }

    public static int getRarityDelayTicks(String rarity) {
        return switch (rarity.toLowerCase(LOCALE)) {
            case "uncommon" -> 20;
            case "rare" -> 35;
            case "ultrarare" -> 80;
            case "legendary" -> 70;
            case "bonus" -> 40;
            default -> 10;
        };
    }

    // ===== HELPERS =====

    public static void dropFromFront(World world, BlockPos pos, Direction facing, ItemStack stack) {
        if (world.isClient) return;
        double x = pos.getX() + 0.5 + facing.getOffsetX() * 0.7;
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5 + facing.getOffsetZ() * 0.7;

        ItemEntity entity = new ItemEntity(world, x, y, z, stack.copy());
        double speed = 0.15;
        entity.setVelocity(facing.getOffsetX() * speed, 0.1, facing.getOffsetZ() * speed);
        world.spawnEntity(entity);
    }

    private static MinecraftServer requireServer(World world) {
        return Objects.requireNonNull(world.getServer(), "[CasinoRocket] Server not found!");
    }

    // ===== STATS / COMMAND HELPERS =====

    public static Text getPlayerStatsText(Object targetOrName, MinecraftServer server) {
        GachaDataStorage data = GachaDataStorage.get(server);
        GachaStats stats = null;
        String playerName = null;

        // === Resolve player or name ===
        if (targetOrName instanceof ServerPlayerEntity player) {
            stats = data.playerStats.get(player.getUuid());
            playerName = (stats != null && stats.getPlayerName() != null)
                    ? stats.getPlayerName()
                    : player.getName().getString();
        } else if (targetOrName instanceof String name) {
            for (GachaStats s : data.playerStats.values()) {
                if (s.getPlayerName() != null && s.getPlayerName().equalsIgnoreCase(name)) {
                    stats = s;
                    playerName = name;
                    break;
                }
            }
        }

        if (stats == null || playerName == null) {
            return Text.literal("No recorded stats for player.").formatted(Formatting.GRAY);
        }

        // === Build formatted text ===
        MutableText text = Text.literal("\n")
                .append(Text.literal("Gacha Machine Stats for ").formatted(Formatting.BOLD))
                .append(Text.literal(playerName).formatted(Formatting.GOLD, Formatting.BOLD))
                .append(Text.literal(":\n").formatted(Formatting.BOLD))
                .append(Text.literal("Total Coins Used: ").formatted(Formatting.YELLOW))
                .append(Text.literal(stats.getTotalCoinsUsed() + "\n\n").formatted(Formatting.YELLOW));

        // === Coins Used by Type ===
        text.append(Text.literal("Coins Inserted:\n").formatted(Formatting.YELLOW));

        Map<String, Integer> coins = new LinkedHashMap<>();
        coins.put("Copper", stats.copperUsed);
        coins.put("Iron", stats.ironUsed);
        coins.put("Gold", stats.goldUsed);
        coins.put("Diamond", stats.diamondUsed);

        int coinIndex = 0;
        int coinSize = coins.size();
        for (var e : coins.entrySet()) {
            String coin = e.getKey();
            int count = e.getValue();

            text.append(Text.literal("• ").formatted(Formatting.YELLOW))
                    .append(Text.literal(coin + ": ").formatted(TextUtils.coinColor(coin)))
                    .append(Text.literal(String.valueOf(count)).formatted(Formatting.YELLOW));

            if (++coinIndex < coinSize) {
                text.append(Text.literal("\n"));
            }
        }

        // === Rarity Stats ===
        text.append(Text.literal("\n\nGachapon Rarity Obtained:\n").formatted(Formatting.YELLOW));

        var entries = stats.getRarityCounts().entrySet();
        int index = 0;
        int size = entries.size();

        for (var e : entries) {
            String rarity = e.getKey();
            int count = e.getValue();

            text.append(Text.literal("• ").formatted(Formatting.YELLOW))
                    .append(Text.literal(capitalize(rarity) + ": ").formatted(TextUtils.rarityColor(rarity)))
                    .append(Text.literal(String.valueOf(count)).formatted(Formatting.YELLOW));

            if (++index < size) {
                text.append(Text.literal("\n"));
            }
        }

        return text;
    }

    public static Text getLeaderboardText(MinecraftServer server, String category, String key) {
        GachaDataStorage data = GachaDataStorage.get(server);
        data.playerStats.values().removeIf(Objects::isNull);

        final String input = key.toLowerCase(Locale.ROOT);
        boolean isRarity = category.equalsIgnoreCase("rarity");
        boolean isCoin = category.equalsIgnoreCase("coins");

        // === Type validation ===
        if (!isRarity && !isCoin) {
            return Text.literal("Invalid leaderboard type. Use 'rarity' or 'coins'.").formatted(Formatting.RED);
        }

        // === Dynamic sort ===
        Comparator<Map.Entry<UUID, GachaStats>> comparator = Comparator.comparingInt(
                (Map.Entry<UUID, GachaStats> e) -> isRarity ? e.getValue().getRarityCount(input) : e.getValue().getCoinCount(input)
                ).reversed();

        // === Top 10 ===
        List<Map.Entry<UUID, GachaStats>> sorted = data.playerStats.entrySet().stream()
                .sorted(comparator)
                .limit(10)
                .toList();

        if (sorted.isEmpty()) {
            return Text.literal("No entries for " + category + " '" + key + "'.").formatted(Formatting.GRAY);
        }

        // === Head ===
        Formatting titleColor = isRarity ? TextUtils.rarityColor(input) : TextUtils.coinColor(input);
        String titleLabel = (isRarity ? "Gacha Wins (" : "Coins Used (") + key.toUpperCase(Locale.ROOT) + ")";

        MutableText out = Text.literal("\n")
                .append(Text.literal("Top 10 - " + titleLabel).formatted(titleColor, Formatting.BOLD))
                .append(Text.literal("\n"));

        // === Body ===
        int rank = 1;
        for (Map.Entry<UUID, GachaStats> e : sorted) {
            UUID id = e.getKey();
            GachaStats stats = e.getValue();
            String name = stats.getPlayerName() != null ? stats.getPlayerName() : shortUuid(id);

            int count = isRarity ? stats.getRarityCount(input) : stats.getCoinCount(input);

            out.append(Text.literal(rank + ". ").formatted(Formatting.YELLOW))
                    .append(Text.literal(name).formatted(TextUtils.rankColors(rank)))
                    .append(Text.literal(" - ").formatted(Formatting.GRAY))
                    .append(Text.literal(String.valueOf(count)).formatted(Formatting.YELLOW));

            if (rank++ < sorted.size()) {
                out.append(Text.literal("\n"));
            }
        }

        return out;
    }

    public static Text clearAllGachaData(MinecraftServer server, boolean confirm, @Nullable ServerPlayerEntity sender) {
        UUID id = sender != null ? sender.getUuid() : UUID.randomUUID();
        long now = server.getOverworld().getTime();
        long window = 20 * 30;

        if (!confirm) {
            CLEAN_CONFIRMATION.put(id, now);
            return Text.literal("⚠ Are you sure you want to delete ALL Gacha data?\n")
                    .append(Text.literal("Type again within 30 seconds:\n").formatted(Formatting.GRAY))
                    .append(Text.literal("'/casinorocket gachapon machines cleandata confirm'").formatted(Formatting.RED, Formatting.BOLD));
        }

        Long lastRequest = CLEAN_CONFIRMATION.get(id);
        if (lastRequest == null) {
            return Text.literal("You must run '/casinorocket gachapon machines cleandata' first.").formatted(Formatting.RED);
        }
        if (now - lastRequest > window) {
            return Text.literal("Confirmation expired. Type again '/casinorocket gachapon machines cleandata'").formatted(Formatting.GRAY);
        }

        CLEAN_CONFIRMATION.remove(id);

        GachaDataStorage data = GachaDataStorage.get(server);
        int playerCount = data.playerStats.size();
        int pityCount = data.pityTracker.size();

        data.playerStats.clear();
        data.pityTracker.clear();
        data.markDirty();

        CasinoRocket.LOGGER.warn("[CasinoRocket] Cleared Gacha data: {} player stats, {} pity entries removed.", playerCount, pityCount);

        return Text.literal("All Gacha data cleared successfully.\n")
                .append(Text.literal(playerCount + " player stats and " + pityCount + " pity entries removed.").formatted(Formatting.GRAY));
    }

    public static Text getMachineRatesText(@Nullable ServerPlayerEntity player, String coinKey, boolean includePity) {
        Map<String, Double> base = CasinoRocket.CONFIG.gachaMachines.normalizedProbabilities(coinKey);
        if (base == null || base.isEmpty()) {
            return Text.literal("No rates available for coin '" + coinKey + "'.").formatted(Formatting.RED);
        }

        Map<String, Double> rates = new LinkedHashMap<>(base);

        if (includePity && player != null && CasinoRocket.CONFIG.gachaMachines.pity.enable) {
            rates = previewPityAdjusted(base, coinKey, player);
        }

        MutableText result = Text.literal("");
        String title = includePity
                ? "Rates with Pity (" + coinKey.toUpperCase(Locale.ROOT) + ")"
                : "Base Rates (" + coinKey.toUpperCase(Locale.ROOT) + ")";
        result.append(Text.literal(title).formatted(Formatting.UNDERLINE)).append("\n");

        boolean first = true;
        for (var e : rates.entrySet()) {
            if (!first) result.append(Text.literal(", "));
            first = false;

            String rarity = capitalize(e.getKey());
            double percentage = e.getValue() * 100.0;
            double rounded = Math.round(percentage * 100.0) / 100.0;

            Formatting color = TextUtils.percentagesColor(rounded);

            result.append(Text.literal(rarity + ": ")
                    .append(Text.literal(String.format("%.2f%%", rounded)).formatted(color)));
        }

        return result;
    }

    public static Map<String, Double> previewPityAdjusted(Map<String, Double> probs, String coinKey, ServerPlayerEntity player) {
        GachaMachinesConfig.PityConfig.CoinPity pityData = getPityData(coinKey);
        if (pityData == null || !CasinoRocket.CONFIG.gachaMachines.pity.enable || player == null) return probs;

        GachaDataStorage data = GachaDataStorage.get(requireServer(player.getWorld()));
        int uses = data.pityTracker.getOrDefault(player.getUuid(), Map.of()).getOrDefault(coinKey, 0);

        double baseLegendary = probs.getOrDefault("legendary", 0.0);
        double t = Math.min(1.0, (double) uses / pityData.usesToMax);
        double newLegendary = baseLegendary + (pityData.maxLegendaryChance - baseLegendary) * t;

        if (newLegendary <= baseLegendary) return probs;

        Map<String, Double> adjusted = new LinkedHashMap<>(probs);
        double diff = newLegendary - baseLegendary;
        double totalOther = 1.0 - baseLegendary;

        for (String key : adjusted.keySet()) {
            if (!key.equals("legendary")) {
                adjusted.put(key, adjusted.get(key) * (1.0 - diff) / totalOther);
            }
        }
        adjusted.put("legendary", newLegendary);

        return adjusted;
    }

    public static Set<String> getRarityKeys() {
        return Set.of("common", "uncommon", "rare", "ultrarare", "legendary", "bonus");
    }

    public static Set<String> getCoinKeys() {
        return Set.of("copper", "iron", "gold", "diamond");
    }

    private static String capitalize(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static String shortUuid(UUID id) {
        String s = id.toString();
        return "Player-" + s.substring(0, 8);
    }

}