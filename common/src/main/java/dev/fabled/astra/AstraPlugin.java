package dev.fabled.astra;

import dev.fabled.astra.modules.ModuleManager;
import dev.fabled.astra.utils.configuration.YamlConfig;

public interface AstraPlugin {

    YamlConfig getConfigYml();
    ModuleManager getModuleManager();

}
