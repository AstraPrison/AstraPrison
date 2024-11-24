package dev.fabled.astra;

import dev.fabled.astra.modules.ModuleManager;
import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.dependencies.HdbUtils;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import org.bukkit.plugin.java.JavaPlugin;

public final class AstraPrison extends JavaPlugin implements AstraPlugin {

    private static AstraPlugin instance;

    public static AstraPlugin getInstance() {
        return instance;
    }

    private YamlConfig configYml;
    private ModuleManager moduleManager;

    @Override
    public void onLoad() {
        instance = this;
        Astra.onLoad(this);
        AstraLog.onLoad();
        PapiUtils.onLoad();
        configYml = new YamlConfig("config");
        moduleManager = new ModuleManager();
        moduleManager.register(OmniToolModule.getInstance());
        moduleManager.onLoad();
    }

    @Override
    public void onEnable() {
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
