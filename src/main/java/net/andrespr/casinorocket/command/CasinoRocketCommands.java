package net.andrespr.casinorocket.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public final class CasinoRocketCommands {

    private CasinoRocketCommands() {}

    // Registering global casinorocket commands
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {

        var root = CommandManager.literal("casinorocket")
                .requires(source -> source.hasPermissionLevel(2));

        // Registering SpawnCasinoWorkersCommand
        root.then(SpawnWorkersCommand.buildSubcommand(registryAccess));
        // Registering SetSuitCommand
        root.then(SetSuitCommand.buildSubcommand());

        dispatcher.register(root);
    }

}