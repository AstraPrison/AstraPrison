package dev.paracausal.astra.api.actions.impl;

import dev.paracausal.astra.api.actions.Action;
import dev.paracausal.astra.api.actions.ActionArgument;
import dev.paracausal.astra.utilities.MiniColor;
import dev.paracausal.astra.utilities.dependencies.PapiUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MessageAction extends Action {

    public MessageAction() {
        super("message");
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

        final String message = PapiUtils.parse(player, argument.getCommandLine());
        target.sendMessage(MiniColor.ALL.deserialize(message));
    }

}
