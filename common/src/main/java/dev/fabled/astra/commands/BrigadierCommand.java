package dev.fabled.astra.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class BrigadierCommand {

    public final String name;
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

    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> literal(@NotNull final String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public static @NotNull CommandSender getSender(@NotNull final CommandContext<CommandSourceStack> context) {
        return context.getSource().getBukkitSender();
    }

    static @NotNull <T> RequiredArgumentBuilder<CommandSourceStack, T> arg(
            @NotNull final String name,
            @NotNull final ArgumentType<T> argumentType
    ) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    public abstract @NotNull CommandNode<CommandSourceStack> buildCommandNode();

    public CompletableFuture<Suggestions> suggestPlayers(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        List<String> playerNames = context.getSource().getServer().getPlayerList().getPlayers().stream()
                .map(ServerPlayer::getGameProfile)
                .map(GameProfile::getName)
                .collect(Collectors.toList());
        return net.minecraft.commands.SharedSuggestionProvider.suggest(playerNames, builder);
    }

}
