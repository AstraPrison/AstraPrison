package dev.fabled.astra.commands;

import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CommandManager {

    private final Commands commands;

    public CommandManager() {
        commands = MinecraftServer.getServer().getCommands();
    }

    public void register(@NotNull final BrigadierCommand command) {
        final var node = command.node;
        if (node == null) {
            return;
        }

        commands.getDispatcher().getRoot().addChild(node);
        final var wrapper = new VanillaCommandWrapper(commands, node);

        if (command.aliases != null) {
            wrapper.setAliases(List.of(command.aliases));
        }

        if (command.permission != null && !command.permission.isEmpty()) {
            wrapper.setPermission(command.permission);
        }

        if (command.description != null && !command.description.isEmpty()) {
            wrapper.setDescription(command.description);
        }

        if (command.usage != null && !command.usage.isEmpty()) {
            wrapper.setUsage(command.usage);
        }

        Bukkit.getCommandMap().register("astra", wrapper);
    }

}
