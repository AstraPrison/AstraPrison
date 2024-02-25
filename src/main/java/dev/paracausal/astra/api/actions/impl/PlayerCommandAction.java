package dev.paracausal.astra.api.actions.impl;

import dev.paracausal.astra.api.actions.Action;
import dev.paracausal.astra.api.actions.ActionArgument;
import dev.paracausal.astra.utilities.dependencies.PapiUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerCommandAction extends Action {

    public PlayerCommandAction() {
        super("player");
    }

    @Override
    public void run(@NotNull OfflinePlayer player, @NotNull ActionArgument argument) {
        if (!player.isOnline()) {
            return;
        }

        final Player target = player.getPlayer();
        if (target == null) {
            return;
        }

        String command = PapiUtils.parse(player, argument.getCommandLine());
        command = command.replace("{PLAYER}", target.getName());

        Bukkit.dispatchCommand(target, command);
    }

}
