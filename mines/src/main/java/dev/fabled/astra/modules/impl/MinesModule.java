package dev.fabled.astra.modules.impl;

import dev.fabled.astra.Astra;
import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.commands.MineAdminCommand;
import dev.fabled.astra.mines.MineManager;
import dev.fabled.astra.modules.AstraModule;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MinesModule extends AstraModule {

    public static @NotNull final String ID;

    static {
        ID = "mines";
    }

    public static @Nullable MinesModule getInstance() {
        final AstraModule module = Astra.getUtilities().getModuleManager().getModule(ID);
        if (module == null) {
            return null;
        }

        return (MinesModule) module;
    }

    private MineManager mineManager;

    private final List<Listener> listeners = List.of(
    );

    public MinesModule() {
        super(ID);
    }

    @Override
    public void onEnable() {
        mineManager = new MineManager();

        final JavaPlugin plugin = Astra.getPlugin();
        listeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));

        final CommandManager commandManager = Astra.getUtilities().getCommandManager();
        commandManager.register(new MineAdminCommand());

        AstraLog.log(AstraLogLevel.SUCCESS, ID + " module enabled!");
    }

    public MineManager getMineManager() {
        return mineManager;
    }

}
