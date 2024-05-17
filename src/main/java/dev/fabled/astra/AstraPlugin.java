package dev.fabled.astra;

import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.commands.AstraCommand;
import dev.fabled.astra.lang.interfaces.MessageKeys;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.impl.ErrorLang;
import dev.fabled.astra.listeners.MenuListener;
import dev.fabled.astra.modules.ModuleManager;
import dev.fabled.astra.modules.impl.MinesModule;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AstraPlugin extends JavaPlugin implements AstraUtilities {

    private YamlConfig configYml;
    private LocaleManager localeManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;

    private final List<MessageKeys> lang = List.of(
            new ErrorLang()
    );

    private final List<Listener> listeners = List.of(
            new MenuListener()
    );

    @Override
    public void onLoad() {
        Astra.onLoad(this);
        configYml = new YamlConfig("config");
        AstraLog.onLoad();

        localeManager = new LocaleManager();
        lang.forEach(localeManager::registerKeys);

        moduleManager = new ModuleManager();
        new MinesModule();
        moduleManager.onLoad();
    }

    @Override
    public void onEnable() {
        commandManager = new CommandManager();
        moduleManager.onEnable();

        listeners.forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
        commandManager.register(new AstraCommand());

        AstraLog.divider();
        AstraLog.log("",
                "+++++++  ++++++  +++++++  +++++++  +++++++",
                "+|   ++  +|         ++    +|   ++  +|   ++",
                "+++++++  ++++++     ++    +++++++  +++++++",
                "+|   ++      ++     ++    +| ++    +|   ++",
                "+|   ++  ++++++     ++    +|   ++  +|   ++",
                ""
        );
        AstraLog.log(AstraLogLevel.SUCCESS, "AstraPrison enabled!");
        AstraLog.log(
                "Version: " + getPluginMeta().getVersion(),
                "Developed by Mantice",
                ""
        );
        AstraLog.divider();
    }

    public void onReload() {
        configYml.save();
        configYml.reload();
        AstraLog.onReload();
        moduleManager.onReload();
    }

    @Override
    public void onDisable() {
        moduleManager.onDisable();
        AstraLog.log(AstraLogLevel.SUCCESS, "AstraPrison disabled!");
    }

    @Override
    public YamlConfig getConfigYml() {
        return configYml;
    }

    @Override
    public LocaleManager getLocaleManager() {
        return localeManager;
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

}
