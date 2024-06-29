package dev.fabled.astra.modules.impl;

import dev.fabled.astra.Astra;
import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.commands.MineAdminCommand;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.impl.MineAdminLang;
import dev.fabled.astra.lang.interfaces.LangKeys;
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

    public static final String ID = "mines";
    private static MinesModule instance;

    public static @NotNull MinesModule getInstance() {
        if (instance == null) {
            final AstraModule module = Astra.getUtilities().getModuleManager().getModule(ID);
            instance = module == null
                    ? new MinesModule()
                    : (MinesModule) module;
        }

        return instance;
    }

    private final List<LangKeys> lang = List.of(
            new MineAdminLang()
    );

    private MineManager mineManager;

    private final List<Listener> listeners = List.of(
    );

    public MinesModule() {
        super(ID);
    }

    @Override
    public void onLoad() {
        lang.forEach(LocaleManager.getInstance()::registerLanguageKeys);
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
