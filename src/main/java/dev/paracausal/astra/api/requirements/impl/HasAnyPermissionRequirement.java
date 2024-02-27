package dev.paracausal.astra.api.requirements.impl;

import dev.paracausal.astra.api.requirements.Requirement;
import dev.paracausal.astra.api.requirements.RequirementOptions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HasAnyPermissionRequirement extends Requirement {

    public HasAnyPermissionRequirement() {
        super("has any permission", "any permission");
    }

    @Override
    public boolean check(@NotNull Player player, RequirementOptions options) {
        final List<String> permissions = options.getStringList("permissions", new ArrayList<>());
        if (permissions.isEmpty()) {
            return false;
        }

        final boolean inverted = options.getBoolean("inverted", false);

        for (final String perm : permissions) {
            final boolean allowed = player.hasPermission(perm);
            if (inverted && !allowed) {
                return true;
            }

            else if (allowed) {
                return true;
            }
        }

        return false;
    }

}
