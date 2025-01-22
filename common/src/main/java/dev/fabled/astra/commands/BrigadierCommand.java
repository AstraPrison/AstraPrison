package dev.fabled.astra.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.fabled.astra.permissions.AstraPermission;
import dev.fabled.astra.utils.ListUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@SuppressWarnings("ALL")
public abstract class BrigadierCommand {

    protected final @NotNull String name;
    private final @Nullable String description;
    private final @NotNull List<String> aliases;

    public BrigadierCommand(
            final @NotNull String name,
            final @Nullable String description,
            final @NotNull String... aliases
    ) {
        this.name = name;
        this.description = description;
        this.aliases = List.of(aliases);
    }

    public final @Nullable String getDescription() {
        return description;
    }

    public final @NotNull List<String> getAliases() {
        return new ArrayList<>(aliases);
    }

    abstract @NotNull LiteralCommandNode<CommandSourceStack> node();

    protected static @NotNull CompletableFuture<Suggestions> suggestOnlinePlayers(
            final @NotNull CommandContext<CommandSourceStack> context,
            final @NotNull SuggestionsBuilder builder
    ) {
        return SharedSuggestionProvider.suggest(ListUtils.onlinePlayerNames(), builder);
    }

    protected static @NotNull Predicate<CommandSourceStack> permissionRequirement(final @NotNull String permission) {
        return commandSourceStack -> {
            if (!(commandSourceStack.getSender() instanceof Player player)) {
                return true;
            }

            return player.hasPermission(permission);
        };
    }

    protected static @NotNull Predicate<CommandSourceStack> permissionRequirement(final @NotNull AstraPermission permission) {
        return permissionRequirement(permission.get());
    }

}
