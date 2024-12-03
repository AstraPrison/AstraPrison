package dev.fabled.astra.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.fabled.astra.utils.blocks.BlockMaterial;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class BlockListUtils {

    private static final @NotNull List<BlockMaterial> materials;

    static {
        materials = new ArrayList<>();
        for (final Material material : Material.values()) {
            try { materials.add(new BlockMaterial(material)); }
            catch (IllegalArgumentException ignore) {}
        }
    }

    public static @NotNull List<BlockMaterial> getMaterials() {
        return new ArrayList<>(materials);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static @NotNull CompletableFuture<Suggestions> suggestBlocks(
            final @NotNull CommandContext<CommandSourceStack> context,
            final @NotNull SuggestionsBuilder builder
    ) {
        return SharedSuggestionProvider.suggest(getMaterials().stream().map(mat -> mat.getMaterial().name()).toList(), builder);
    }

}
