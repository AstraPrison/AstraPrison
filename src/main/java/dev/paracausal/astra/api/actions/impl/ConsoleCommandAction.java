package dev.paracausal.astra.api.actions.impl;

import dev.paracausal.astra.api.actions.Action;
import dev.paracausal.astra.api.actions.ActionArgument;
import dev.paracausal.astra.utilities.dependencies.PapiUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public final class ConsoleCommandAction extends Action {

    public ConsoleCommandAction() {
        super("console");
    }

    @Override
    public void run(@NotNull OfflinePlayer player, @NotNull ActionArgument argument) {
        String command = PapiUtils.parse(player, argument.getCommandLine());

        String playerName = player.getName();
        if (playerName != null) {
            command = command.replace("{PLAYER}", playerName);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

}
