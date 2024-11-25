package dev.fabled.astra;

import dev.fabled.astra.commands.AstraCommand;
import dev.fabled.astra.commands.BrigadierCommand;
import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.modules.ModuleManager;
import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.nms.NMSFactory;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.dependencies.HdbUtils;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

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
        moduleManager = new ModuleManager();
        moduleManager.register(OmniToolModule.getInstance());
        moduleManager.onLoad();
    }

    @Override
    public void onEnable() {
        if (!NMSFactory.onEnable()) {
            AstraLog.log(AstraLogLevel.ERROR, "You are using an unsupported version!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        moduleManager.onEnable();

        final List<BrigadierCommand> commands = List.of(
                new AstraCommand()
        );

        final CommandManager commandManager = CommandManager.getInstance();
        commands.forEach(commandManager::register);

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
