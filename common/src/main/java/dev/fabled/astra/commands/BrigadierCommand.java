package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BrigadierCommand {

    final String name;
    final String[] aliases;
    final String permission;
    final String description;
    final String usage;

    final CommandNode<CommandSourceStack> node;

    public BrigadierCommand(
            @NotNull final String name,
            @Nullable final String[] aliases,
            @Nullable final String permission,
            @Nullable final String description,
            @Nullable final String usage
    ) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.description = description;
        this.usage = usage;
        this.node = buildCommandNode();
    }

    abstract @NotNull CommandNode<CommandSourceStack> buildCommandNode();

    static @NotNull LiteralArgumentBuilder<CommandSourceStack> literal(@NotNull final String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    static @NotNull <T> RequiredArgumentBuilder<CommandSourceStack, T> arg(
            @NotNull final String name,
            @NotNull final ArgumentType<T> argumentType
    ) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    static @NotNull CommandSender getSender(@NotNull final CommandContext<CommandSourceStack> context) {
        return context.getSource().getBukkitSender();
    }

}
