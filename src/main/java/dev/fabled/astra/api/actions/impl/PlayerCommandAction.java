package dev.fabled.astra.api.actions.impl;

import dev.fabled.astra.api.actions.ActionArgument;
import dev.fabled.astra.api.actions.AstraAction;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandAction extends AstraAction {

    public static final @NotNull String ID;

    static {
        ID = "player";
    }

    public PlayerCommandAction() {
        super(ID);
    }

    @Override
    public void run(final @NotNull Player player, final @NotNull ActionArgument argument) {
        final String commandLine = PapiUtils.parse(player, argument.getCommandLine())
                .replace("{PLAYER}", player.getName());

        Bukkit.dispatchCommand(player, commandLine);
    }

}
