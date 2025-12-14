package net.andrespr.casinorocket.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public final class CasinoRocketCommands {

    private CasinoRocketCommands() {}

    // Registering global casinorocket commands
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {

        LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal("casinorocket");

        // Registering SpawnCasinoWorkersCommand
        root.then(SpawnWorkersCommand.buildSubcommand(registryAccess));
        // Registering SetSuitCommand
        root.then(SetSuitCommand.buildSubcommand());
        // Registering GachaponCommands
        root.then(GachaponCommands.buildSubcommand());
        // Registering SlotsCommands
        root.then(SlotMachineCommands.buildSubcommand());

        dispatcher.register(root);
    }

}