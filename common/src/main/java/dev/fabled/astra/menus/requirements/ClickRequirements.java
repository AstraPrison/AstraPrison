package dev.fabled.astra.menus.requirements;

import dev.fabled.astra.api.requirements.AbstractItemClickRequirement;
import dev.fabled.astra.api.requirements.ClickRequirementManagerImpl;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class ClickRequirements {

    final @NotNull Set<RequirementActions> requirementOptions;

    public ClickRequirements() {
        requirementOptions = new HashSet<>();
    }

    public void addClickRequirementOption(final @NotNull YamlConfig config, final @NotNull String key)  throws NullPointerException {
        final ConfigurationSection section = config.options().getConfigurationSection(key);
        if (section == null) {
            throw new NullPointerException("No options in this requirement!");
        }

        section.getKeys(false).forEach(requirementId ->
                requirementOptions.add(new RequirementActions(config, key))
        );
    }

    public boolean isEmpty() {
        return requirementOptions.isEmpty();
    }

    public boolean check(final @NotNull Player player, final @NotNull String requirementId) {
        final AbstractItemClickRequirement requirement = ClickRequirementManagerImpl.getInstance().getRequirement(requirementId);
        if (requirement == null) {
            return true;
        }

        for (final RequirementActions requirementActions : requirementOptions) {
            if (!requirement.check(player, requirementActions.requirementOptions)) {
                requirementActions.clickActions.run(player);
                return false;
            }
        }

        return true;
    }

}
