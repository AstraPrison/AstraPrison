package dev.fabled.astra.menus.requirements;

import dev.fabled.astra.api.requirements.ClickRequirementManagerImpl;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ClickRequirementGroup {

    final @NotNull Map<String, ClickRequirements> requirements;

    public ClickRequirementGroup(final @NotNull YamlConfig config, final @NotNull String key) throws NullPointerException {
        final ConfigurationSection section = config.options().getConfigurationSection(key);
        if (section == null) {
            throw new NullPointerException("Group has no section!");
        }

        requirements = new HashMap<>();
        final ClickRequirementManagerImpl clickRequirementManager = ClickRequirementManagerImpl.getInstance();

        section.getKeys(false).forEach(requirement -> {
            final String requirementType = config.options().getString(key + "." + requirement + ".type", null);
            if (requirementType == null || !clickRequirementManager.exists(requirementType)) {
                return;
            }

            requirements.putIfAbsent(requirementType, new ClickRequirements());
            try { requirements.get(requirementType).addClickRequirementOption(config, key + "." + requirement); }
            catch (NullPointerException ignored) {}
        });

        final Set<String> toRemove = new HashSet<>();
        requirements.forEach((requirementType, options) -> {
            if (options.isEmpty()) {
                toRemove.add(requirementType);
            }
        });

        toRemove.forEach(requirements::remove);

        if (requirements.isEmpty()) {
            throw new NullPointerException("Group has no configured requirements");
        }
    }

    public boolean check(final @NotNull Player player) {
        for (final Map.Entry<String, ClickRequirements> entry : requirements.entrySet()) {
            if (!entry.getValue().check(player, entry.getKey())) {
                return false;
            }
        }

        return true;
    }

}
