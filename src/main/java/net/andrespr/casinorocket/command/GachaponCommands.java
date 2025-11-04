package net.andrespr.casinorocket.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.andrespr.casinorocket.util.gacha.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public final class GachaponCommands {

    public static LiteralArgumentBuilder<ServerCommandSource> buildSubcommand() {
        return CommandManager.literal("gachapon")
                .then(CommandManager.literal("item")
                        .then(CommandManager.literal("rates")
                                .then(CommandManager.argument("pool", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            GachaponUtils.getPools().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(GachaponCommands::executeForItem)
                                )
                        )
                ).then(CommandManager.literal("pokemon")
                        .then(CommandManager.literal("rates")
                                .then(CommandManager.argument("pool", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            PokemonGachaponUtils.getPools().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(GachaponCommands::executeForPokemon)
                                )
                        )
                ).then(CommandManager.literal("machines")
                        .then(CommandManager.literal("stats")
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
                        )
                        .then(CommandManager.literal("leaderboard")
                                .then(CommandManager.literal("global")
                                        .then(CommandManager.argument("rarity", StringArgumentType.string())
                                                .suggests((context, builder) -> {
                                                    GachaMachinesUtils.getRarityKeys().forEach(builder::suggest);
                                                    return builder.buildFuture();
                                                })
                                                .executes(GachaponCommands::executeForMachineLeaderboard)
                                        )
                                )
                        )
                );
    }

    private static int executeForItem(CommandContext<ServerCommandSource> context) {
        String poolKey = StringArgumentType.getString(context, "pool");
        ServerPlayerEntity player = getPlayer(context);
        if (player == null) return 0;
        player.sendMessage(GachaponUtils.getPoolPercentages(poolKey), false);
        return 1;
    }

    private static int executeForPokemon(CommandContext<ServerCommandSource> context) {
        String poolKey = StringArgumentType.getString(context, "pool");
        ServerPlayerEntity player = getPlayer(context);
        if (player == null) return 0;
        player.sendMessage(PokemonGachaponUtils.getPoolPercentages(poolKey), false);
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
            statsText = GachaMachinesUtils.getPlayerStatsText(target);
        } else {
            statsText = GachaMachinesUtils.getPlayerStatsTextByName(name, server);
        }

        sender.sendMessage(statsText, false);
        return 1;
    }


    private static int executeForMachineLeaderboard(CommandContext<ServerCommandSource> context) {
        String rarity = StringArgumentType.getString(context, "rarity");
        ServerPlayerEntity sender = getPlayer(context);
        if (sender == null) return 0;
        sender.sendMessage(GachaMachinesUtils.getLeaderboardText(context.getSource().getServer(), rarity), false);
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