package dev.fabled.astra;

import com.github.retrooper.packetevents.PacketEvents;
import dev.fabled.astra.commands.AstraCommand;
import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.commands.OmniToolCommand;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.impl.AstraAdminLang;
import dev.fabled.astra.lang.impl.ErrorLang;
import dev.fabled.astra.lang.interfaces.LangKeys;
import dev.fabled.astra.listeners.*;
import dev.fabled.astra.modules.ModuleManager;
import dev.fabled.astra.modules.impl.MinesModule;
import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AstraPlugin extends JavaPlugin implements AstraUtilities {

    private YamlConfig configYml;
    private LocaleManager localeManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;


    private final List<LangKeys> lang = List.of(
            new AstraAdminLang(),
            new ErrorLang()
    );

    private final List<Listener> listeners = List.of(
            new MenuListener(),
            new MineWandListener(),
            new PacketInteractListener(this),
            new PacketAdapter(),
            new MinePanelListener()
    );

    @Override
    public void onLoad() {
        Astra.onLoad(this);
        configYml = new YamlConfig("config");
        AstraLog.onLoad();

        LocaleManager.onLoad();
        lang.forEach(LocaleManager.getInstance()::registerLanguageKeys);


        moduleManager = new ModuleManager();
        new MinesModule();
        new OmniToolModule();
        moduleManager.onLoad();
    }

    @Override
    public void onEnable() {
        commandManager = new CommandManager();
        commandManager.register(new AstraCommand(this));
        commandManager.register(new OmniToolCommand());
        moduleManager.onEnable();

        listeners.forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        //Packetevents
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(true)
                .bStats(true);
        PacketEvents.getAPI().load();

        //PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsListener());
        //PacketEvents.getAPI().getEventManager().registerListener(new PacketInteractListener());
        PacketEvents.getAPI().init();


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
                "Developed by Mantice and DrDivx2k",
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
