package dev.fabled.astra.api.requirements.impl;

import dev.fabled.astra.api.requirements.AbstractItemClickRequirement;
import dev.fabled.astra.api.requirements.ClickRequirementOptions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class IsOnlineClickRequirement extends AbstractItemClickRequirement {

    public IsOnlineClickRequirement() {
        super("is online");
    }

    @Override
    public @NotNull Boolean check(@NotNull Player player, @NotNull ClickRequirementOptions options) {
        final String targetInput = options.getString("player", null);
        if (targetInput == null) {
            return false;
        }

        final OfflinePlayer target;
        if (targetInput.length() > 16) {
            final UUID uuid;
            try { uuid = UUID.fromString(targetInput); }
            catch (IllegalArgumentException e) {
                return false;
            }

            target = Bukkit.getOfflinePlayer(uuid);
        }

        else {
            target = Bukkit.getOfflinePlayer(targetInput);
        }

        return target.isOnline();
    }

}
