package dev.fabled.astra.api.requirements.impl;

import dev.fabled.astra.api.requirements.AbstractItemClickRequirement;
import dev.fabled.astra.api.requirements.ClickRequirementOptions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PermissionClickRequirement extends AbstractItemClickRequirement {

    public PermissionClickRequirement() {
        super("has permission");
    }

    @Override
    public @NotNull Boolean check(@NotNull Player player, @NotNull ClickRequirementOptions options) {
        final String permission = options.getString("permission", null);
        if (permission == null) {
            return false;
        }

        if (permission.startsWith("!")) {
            return !player.hasPermission(permission.substring(1));
        }

        return player.hasPermission(permission);
    }

}
