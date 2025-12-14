package net.andrespr.casinorocket.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.andrespr.casinorocket.games.slot.SlotUtils;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public final class SlotMachineCommands {

    public static LiteralArgumentBuilder<ServerCommandSource> buildSubcommand() {
        return CommandManager.literal("slots")
                .then(CommandManager.literal("leaderboard")
                        .then(CommandManager.literal("highest_win")
                                .executes(ctx -> executeLeaderboard(ctx, "highest_win")))
                        .then(CommandManager.literal("total_win")
                                .executes(ctx -> executeLeaderboard(ctx, "total_win")))
                        .then(CommandManager.literal("total_lost")
                                .executes(ctx -> executeLeaderboard(ctx, "total_lost")))
                );
    }

    private static int executeLeaderboard(CommandContext<ServerCommandSource> context, String key) {
        ServerPlayerEntity sender = getPlayer(context);
        if (sender == null) return 0;

        Text msg = SlotUtils.getLeaderboardText(context.getSource().getServer(), key);
        sender.sendMessage(msg, false);
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