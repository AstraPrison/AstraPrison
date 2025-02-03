package dev.fabled.astra.menus.requirements;

import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class ItemClickRequirements {

    private final Set<ClickRequirementGroup> requirementGroups;

    public ItemClickRequirements(final @NotNull YamlConfig config, final @NotNull String key) throws NullPointerException {
        requirementGroups = new HashSet<>();

        final ConfigurationSection section = config.options().getConfigurationSection(key);
        if (section == null) {
            throw new NullPointerException("Click type has no groups!");
        }

        section.getKeys(false).forEach(groupId -> {
            try { requirementGroups.add(new ClickRequirementGroup(config, key + "." + groupId)); }
            catch (NullPointerException ignored) {}
        });
    }

    public boolean check(final @NotNull Player player) {
        for (final ClickRequirementGroup group : requirementGroups) {
            if (group.check(player)) {
                return true;
            }
        }

        return false;
    }

}
