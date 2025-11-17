package net.andrespr.casinorocket.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.andrespr.casinorocket.data.GachaDataStorage;
import net.andrespr.casinorocket.data.GachaStats;
import net.andrespr.casinorocket.util.gacha.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class GachaponCommands {

    public static LiteralArgumentBuilder<ServerCommandSource> buildSubcommand() {
        return CommandManager.literal("gachapon")
                .then(CommandManager.literal("rates")
                        .then(CommandManager.literal("item")
                                .then(CommandManager.argument("pool", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            GachaponUtils.getPools().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(GachaponCommands::executeRatesForItem)
                                )
                        )
                        .then(CommandManager.literal("pokemon")
                                .then(CommandManager.argument("pool", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            PokemonGachaponUtils.getPools().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(GachaponCommands::executeRatesForPokemon)
                                )
                        )
                        .then(CommandManager.literal("machine")
                                .then(CommandManager.literal("base")
                                        .then(CommandManager.argument("coin", StringArgumentType.string())
                                                .suggests((context, builder) -> {
                                                    GachaMachinesUtils.getCoinKeys().forEach(builder::suggest);
                                                    return builder.buildFuture();
                                                })
                                                .executes(ctx -> executeRatesForMachine(ctx, false))
                                        )
                                )
                                .then(CommandManager.literal("pity")
                                        .then(CommandManager.argument("player", StringArgumentType.string())
                                                .suggests((context, builder) -> {
                                                    MinecraftServer server = context.getSource().getServer();
                                                    for (ServerPlayerEntity online : server.getPlayerManager().getPlayerList()) {
                                                        builder.suggest(online.getName().getString());
                                                    }
                                                    return builder.buildFuture();
                                                })
                                                .then(CommandManager.argument("coin", StringArgumentType.string())
                                                        .suggests((context, builder) -> {
                                                            GachaMachinesUtils.getCoinKeys().forEach(builder::suggest);
                                                            return builder.buildFuture();
                                                        })
                                                        .executes(ctx -> executeRatesForMachine(ctx, true))
                                                )
                                        )
                                )
                        )
                ).then(CommandManager.literal("stats")
                        .then(CommandManager.argument("player", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    MinecraftServer server = context.getSource().getServer();
                                    GachaDataStorage data = GachaDataStorage.get(server);

                                    for (ServerPlayerEntity online : server.getPlayerManager().getPlayerList()) {
                                        builder.suggest(online.getName().getString());
                                    }

                                    for (GachaStats stats : data.playerStats.values()) {
                                        if (stats.getPlayerName() != null)
                                            builder.suggest(stats.getPlayerName());
                                    }

                                    return builder.buildFuture();
                                })
                                .executes(GachaponCommands::executeForMachineStats)
                        )
                ).then(CommandManager.literal("leaderboard")
                        .then(CommandManager.literal("rarity")
                                .then(CommandManager.argument("rarity", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            GachaMachinesUtils.getRarityKeys().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> executeForLeaderboard(ctx, "rarity"))
                                )
                        )
                        .then(CommandManager.literal("coins")
                                .then(CommandManager.argument("coin", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            GachaMachinesUtils.getCoinKeys().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> executeForLeaderboard(ctx, "coins"))
                                )
                        )
                ).then(CommandManager.literal("cleandata")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(GachaponCommands::executeCleanData)
                        .then(CommandManager.literal("confirm")
                                .executes(GachaponCommands::confirmCleanData)
                        )
                );
    }

    private static int executeRatesForItem(CommandContext<ServerCommandSource> context) {
        String poolKey = StringArgumentType.getString(context, "pool");
        ServerPlayerEntity player = getPlayer(context);
        if (player == null) return 0;
        player.sendMessage(GachaponUtils.getPoolPercentages(poolKey), false);
        return 1;
    }

    private static int executeRatesForPokemon(CommandContext<ServerCommandSource> context) {
        String poolKey = StringArgumentType.getString(context, "pool");
        ServerPlayerEntity player = getPlayer(context);
        if (player == null) return 0;
        player.sendMessage(PokemonGachaponUtils.getPoolPercentages(poolKey), false);
        return 1;
    }

    private static int executeRatesForMachine(CommandContext<ServerCommandSource> context, boolean includePity) {
        MinecraftServer server = context.getSource().getServer();
        ServerPlayerEntity sender = getPlayer(context);
        if (sender == null) return 0;

        String coinKey;
        ServerPlayerEntity target = null;
        if (includePity) {
            String name = StringArgumentType.getString(context, "player");
            coinKey = StringArgumentType.getString(context, "coin");
            target = server.getPlayerManager().getPlayer(name);

            if (target == null) {
                sender.sendMessage(Text.literal("Player '" + name + "' is not online.").formatted(Formatting.RED), false);
                return 0;
            }

        } else {
            coinKey = StringArgumentType.getString(context, "coin");
        }

        Text msg = GachaMachinesUtils.getMachineRatesText(target, coinKey, includePity);
        sender.sendMessage(msg, false);
        return 1;
    }

    private static int executeForMachineStats(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "player");
        MinecraftServer server = context.getSource().getServer();
        ServerPlayerEntity sender = getPlayer(context);
        if (sender == null) return 0;

        ServerPlayerEntity target = server.getPlayerManager().getPlayer(name);

        Text statsText;
        if (target != null) {
            statsText = GachaMachinesUtils.getPlayerStatsText(target, server);
        } else {
            statsText = GachaMachinesUtils.getPlayerStatsText(name, server);
        }

        sender.sendMessage(statsText, false);
        return 1;
    }


    private static int executeForLeaderboard(CommandContext<ServerCommandSource> context, String category) {
        String key = StringArgumentType.getString(context, category.equals("rarity") ? "rarity" : "coin");
        ServerPlayerEntity sender = getPlayer(context);
        if (sender == null) return 0;
        sender.sendMessage(GachaMachinesUtils.getLeaderboardText(context.getSource().getServer(), category, key), false);
        return 1;
    }

    private static int executeCleanData(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity sender = getPlayer(context);
        if (sender == null) return 0;
        Text response = GachaMachinesUtils.clearAllGachaData(Objects.requireNonNull(sender.getServer()), false, sender);
        sender.sendMessage(response, false);
        return 1;
    }

    private static int confirmCleanData(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity sender = getPlayer(context);
        if (sender == null) return 0;
        Text response = GachaMachinesUtils.clearAllGachaData(Objects.requireNonNull(sender.getServer()), true, sender);
        sender.sendMessage(response, false);
        return 1;
    }

    private static @Nullable ServerPlayerEntity getPlayer(CommandContext<ServerCommandSource> context) {
        try {
            return context.getSource().getPlayer();
        } catch (Exception e) {
            return null;
        }
    }

}