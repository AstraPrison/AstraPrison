package dev.fabled.astra.commands;

import dev.fabled.astra.Astra;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public final class CommandManager {

    private static CommandManager instance;

    public static @NotNull CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }

        return instance;
    }

    private final LifecycleEventManager<Plugin> manager;

    private CommandManager() {
        manager = Astra.getPlugin().getLifecycleManager();
    }

    public void register(final @NotNull BrigadierCommand command) {
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event ->
                event.registrar().register(command.node(), command.getDescription(), command.getAliases())
        );
    }

}
