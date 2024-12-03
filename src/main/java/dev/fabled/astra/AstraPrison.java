package dev.fabled.astra;

import dev.fabled.astra.commands.*;
import dev.fabled.astra.listeners.AstraListener;
import dev.fabled.astra.listeners.AstraMenuListener;
import dev.fabled.astra.listeners.PacketRegistrationListener;
import dev.fabled.astra.locale.LocaleManager;
import dev.fabled.astra.menus.MenuManager;
import dev.fabled.astra.modules.ModuleManager;
import dev.fabled.astra.modules.impl.MinesModule;
import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.nms.NMSFactory;
import dev.fabled.astra.permissions.AstraPermission;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.dependencies.HdbUtils;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public final class AstraPrison extends JavaPlugin implements AstraPlugin {

    private static AstraPrison instance;

    public static AstraPrison getInstance() {
        return instance;
    }

    private YamlConfig configYml;
    private ModuleManager moduleManager;

    @Override
    public void onLoad() {
        instance = this;
        Astra.onLoad(this);
        PapiUtils.onLoad();
        HdbUtils.onLoad();
        configYml = new YamlConfig("config");
        AstraLog.onLoad();
        LocaleManager.getInstance();
        AstraPermission.onLoad();
        MenuManager.getInstance();
        moduleManager = new ModuleManager();
        moduleManager.register(OmniToolModule.getInstance());
        moduleManager.register(MinesModule.getInstance());
        moduleManager.onLoad();
    }

    @Override
    public void onEnable() {
        if (!NMSFactory.onEnable()) {
            AstraLog.log(AstraLogLevel.ERROR, "You are using an unsupported version!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        final List<AstraListener> listeners = List.of(
                new PacketRegistrationListener(),
                new AstraMenuListener()
        );
        listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        final List<BrigadierCommand> commands = List.of(
                new AstraCommand()
        );
        final CommandManager commandManager = CommandManager.getInstance();
        commands.forEach(commandManager::register);

        moduleManager.onEnable();

        AstraLog.divider();
        AstraLog.log(AstraLogLevel.SUCCESS, "AstraPrison enabled!");
        AstraLog.log(
                "Version: " + getPluginMeta().getVersion(),
                "Developed by Mantice"
        );
        AstraLog.divider();
    }

    @Override
    public void onDisable() {
        moduleManager.onDisable();
        AstraLog.log(AstraLogLevel.SUCCESS, "AstraPrison v" + getPluginMeta().getVersion() + " disabled!");
    }

    public void onReload() {
        PapiUtils.onReload();
        HdbUtils.onReload();
        configYml.save();
        configYml.reload();
        AstraLog.onReload();
        LocaleManager.getInstance().onReload();
        MenuManager.getInstance().onReload();
        moduleManager.onReload();
    }

    @Override
    public YamlConfig getConfigYml() {
        return configYml;
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

}
