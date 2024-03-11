package dev.paracausal.astra.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.paracausal.astra.Astra;
import dev.paracausal.astra.utilities.dependencies.Dependencies;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileReader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class CommandRegistry {

    private final Map<String, AstraCommand> commands;

    public CommandRegistry() {
        commands = new HashMap<>();
    }

    public void onEnable() {
        commands.forEach(this::register);
    }

    public void add(@NotNull final AstraCommand command) {
        commands.put(command.getName(), command);
    }

    public void add(@NotNull final String commandName, @NotNull final AstraCommand command) {
        commands.put(commandName, command);
    }

    public void register(@NotNull final String commandName, @NotNull final AstraCommand command) {
        final CommandMap map = Bukkit.getCommandMap();
        map.register("astra", command);

        if (!CommodoreProvider.isSupported()) {
            return;
        }

        final InputStream stream = Astra.getPlugin().getResource("commodore/" + command.getName() + ".commodore");
        if (stream == null) {
            return;
        }

        final Commodore commodore = Dependencies.getCommodore();

        final LiteralCommandNode<?> node;
        try { node = CommodoreFileReader.INSTANCE.parse(stream); }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            final Class<?> clazz = node.getClass();
            final Field literalField = clazz.getDeclaredField("literal");
            final Field literalLowerField = clazz.getDeclaredField("literalLowerCase");
            literalField.setAccessible(true);
            literalLowerField.setAccessible(true);

            literalField.set(node, commandName);
            literalLowerField.set(node, commandName.toLowerCase());

            literalField.setAccessible(false);
            literalLowerField.setAccessible(false);
        }

        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        commodore.register(command, node);
    }

    public void register(@NotNull final AstraCommand command) {
        register(command.getName(), command);
    }

}
