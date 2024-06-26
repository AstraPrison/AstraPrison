package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.impl.ErrorLang;
import dev.fabled.astra.utils.ListUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

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

    @NotNull LiteralArgumentBuilder<CommandSourceStack> literal(@NotNull final String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    @NotNull <T> RequiredArgumentBuilder<CommandSourceStack, T> arg(
            @NotNull final String name,
            @NotNull final ArgumentType<T> argumentType
    ) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    @NotNull CommandSender getSender(@NotNull final CommandContext<CommandSourceStack> context) {
        return context.getSource().getBukkitSender();
    }

    @NotNull CompletableFuture<Suggestions> suggestOnlinePlayers(
            @NotNull final CommandContext<CommandSourceStack> context,
            @NotNull final SuggestionsBuilder builder
    ) {
        return SharedSuggestionProvider.suggest(ListUtils.playerNames(), builder);
    }

    /**
     * Performs the permission check using the permission
     * @param permission {@link String}
     * @return {@link Boolean} true if they have the permission, false otherwise
     */
    @NotNull Predicate<CommandSourceStack> permissionRequirement(@NotNull final String permission) {
        return commandSourceStack -> {
            if (!(commandSourceStack.getBukkitSender() instanceof Player player)) {
                return true;
            }

            return player.hasPermission(permission);
        };
    }

}
