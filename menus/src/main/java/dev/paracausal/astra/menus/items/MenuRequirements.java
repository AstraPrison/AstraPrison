package dev.paracausal.astra.menus.items;

import dev.paracausal.astra.Astra;
import dev.paracausal.astra.api.requirements.Requirement;
import dev.paracausal.astra.api.requirements.RequirementManager;
import dev.paracausal.astra.utilities.ListUtils;
import dev.paracausal.astra.utilities.configuration.YamlConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuRequirements {

    private @NotNull final Map<String, MenuRequirement> requirements;

    /**
     * Create menu requirements from a menu config item's path!
     * @param config The menu's YamlConfig
     * @param path The item's path<br>Example: 'contents.{id}'
     */
    public MenuRequirements(
            @NotNull final YamlConfig config,
            @NotNull String path
    ) {
        this.requirements = new HashMap<>();
        if (!path.endsWith(".")) path += ".";

        final ConfigurationSection requirementSection = config.options().getConfigurationSection(path);
        if (requirementSection == null) {
            return;
        }

        final String p = path + ".";
        final RequirementManager manager = Astra.getAPI().getRequirementManager();

        requirementSection.getKeys(false).forEach(id -> {
            final String type = config.options().getString(p + id + ".type", null);
            if (type == null) {
                return;
            }

            if (!manager.exists(type)) {
                return;
            }

            final Requirement requirement = manager.getRequirement(type);
            if (requirement == null) {
                return;
            }

            final List<String> commandLines = ListUtils.fromConfig(config, p + "actions");
            requirements.put(id, new MenuRequirement(id, requirement, commandLines));
        });
    }

    /**
     * Create menu requirements from a pre-made map of requirements!
     * @param requirements the map (of key String and value MenuRequirement) to use
     */
    public MenuRequirements(@NotNull final Map<String, MenuRequirement> requirements) {
        this.requirements = new HashMap<>(requirements);
    }

    public @NotNull Map<String, MenuRequirement> getRequirements() {
        return new HashMap<>(requirements);
    }

}
