package dev.paracausal.astra;

import dev.paracausal.astra.commands.CommandRegistry;
import dev.paracausal.astra.utilities.configuration.YamlConfig;

public interface AstraUtility {

    YamlConfig getConfigYml();
    CommandRegistry getCommandRegistry();

}
