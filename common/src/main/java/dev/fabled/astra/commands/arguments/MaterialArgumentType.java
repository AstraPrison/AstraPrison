package dev.fabled.astra.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class MaterialArgumentType implements ArgumentType<Material> {

    private static final @NotNull Collection<String> EXAMPLES;

    static {
        EXAMPLES = List.of("STONE", "COBBLESTONE", "STONE_BRICKS");
    }

    public static MaterialArgumentType block() {
        return new MaterialArgumentType(Type.BLOCK);
    }

    public static MaterialArgumentType material() {
        return new MaterialArgumentType(Type.MATERIAL);
    }

    public static @NotNull Material getMaterial(final @NotNull CommandContext<CommandSourceStack> context, final @NotNull String name) {
        return context.getArgument(name, Material.class);
    }

    private final @NotNull Type type;

    private MaterialArgumentType(final @NotNull Type type) {
        this.type = type;
    }

    @Override
    public Material parse(StringReader reader) throws CommandSyntaxException {
        final String string = reader.readUnquotedString().toUpperCase();
        final String message = type == Type.BLOCK ? string + " is not a block!" : string + " is not a material!";

        final Material material;
        try { material = Material.valueOf(string); }
        catch (IllegalArgumentException e) {
            throw commandSyntaxException(message);
        }

        if (material.isAir()) {
            throw commandSyntaxException(message);
        }

        if (type == Type.BLOCK && !material.isBlock()) {
            throw commandSyntaxException(message);
        }

        return material;
    }

    private CommandSyntaxException commandSyntaxException(final @NotNull String message) {
        return new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument(), () -> message);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public enum Type {
        MATERIAL,
        BLOCK;
    }

}
