package dev.fabled.astra.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public final class MaterialList {

    private static final @NotNull Set<Material> MATERIALS;
    private static final @NotNull Set<String> MATERIAL_STRINGS;
    private static final @NotNull Set<Material> BLOCKS;
    private static final @NotNull Set<String> BLOCK_STRINGS;

    static {
        MATERIALS = new HashSet<>();
        MATERIAL_STRINGS = new HashSet<>();
        BLOCKS = new HashSet<>();
        BLOCK_STRINGS = new HashSet<>();

        for (final Material material : Material.values()) {
            if (material.isAir()) {
                continue;
            }

            MATERIALS.add(material);
            MATERIAL_STRINGS.add(material.name());

            if (material.isBlock()) {
                BLOCKS.add(material);
                BLOCK_STRINGS.add(material.name());
            }
        }
    }

    public static @NotNull Set<Material> getMaterials() {
        return new HashSet<>(MATERIALS);
    }

    public static @NotNull Set<Material> getBlocks() {
        return new HashSet<>(BLOCKS);
    }

    public static @NotNull CompletableFuture<Suggestions> suggestMaterials(
            final @NotNull CommandContext<CommandSourceStack> context,
            final @NotNull SuggestionsBuilder builder
    ) {
        return SharedSuggestionProvider.suggest(MATERIAL_STRINGS, builder);
    }

    public static @NotNull CompletableFuture<Suggestions> suggestBlocks(
            final @NotNull CommandContext<CommandSourceStack> context,
            final @NotNull SuggestionsBuilder builder
    ) {
        return SharedSuggestionProvider.suggest(BLOCK_STRINGS, builder);
    }

}
