package dev.fabled.astra;

import dev.fabled.astra.commands.CommandManager;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.modules.ModuleManager;
import dev.fabled.astra.utils.configuration.YamlConfig;

public interface AstraUtilities {

    YamlConfig getConfigYml();

    LocaleManager getLocaleManager();

    CommandManager getCommandManager();
    ModuleManager getModuleManager();

}
