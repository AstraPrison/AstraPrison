package dev.fabled.astra.api.actions.impl;

import dev.fabled.astra.api.actions.AbstractItemClickAction;
import dev.fabled.astra.api.actions.ItemClickActionArgs;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MessageClickAction extends AbstractItemClickAction {

    public MessageClickAction() {
        super("message");
    }

    @Override
    public void run(@NotNull Player player, @NotNull ItemClickActionArgs arguments) {
        player.sendMessage(MiniColor.CHAT.deserialize(
                PapiUtils.parse(arguments.getCommandLine(), player)
        ));
    }

}
