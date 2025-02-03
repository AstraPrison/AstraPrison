package dev.fabled.astra.api.actions.impl;

import dev.fabled.astra.api.actions.AbstractItemClickAction;
import dev.fabled.astra.api.actions.ItemClickActionArgs;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerCommandClickAction extends AbstractItemClickAction {

    public PlayerCommandClickAction() {
        super("player");
    }

    @Override
    public void run(@NotNull Player player, @NotNull ItemClickActionArgs arguments) {
        Bukkit.dispatchCommand(
                player,
                PapiUtils.parse(arguments.getCommandLine(), player)
        );
    }

}
