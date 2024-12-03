package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.fabled.astra.locale.LocaleManager;
import dev.fabled.astra.locale.impl.ErrorMessageKeys;
import dev.fabled.astra.locale.impl.MineWandMessageKeys;
import dev.fabled.astra.mines.wand.MineWand;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public final class MineWandCommand extends BrigadierCommand {

    public MineWandCommand() {
        super(
                "minewand",
                "The admin command for the mine wand!"
        );
    }

    @Override
    @NotNull LiteralCommandNode<CommandSourceStack> node() {
        return base().build();
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> base() {
        return Commands.literal(name)
                .requires(permissionRequirement("astra.admin"))
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.ERROR, "Select a player!");
                        return 0;
                    }

                    player.getInventory().addItem(MineWand.getInstance().getMineWand());
                    LocaleManager.sendMessage(player, MineWandMessageKeys.GIVE_SELF);
                    return 0;
                })
                .then(player());
    }

    private @NotNull RequiredArgumentBuilder<CommandSourceStack, String> player() {
        return Commands.argument("player", StringArgumentType.word())
                .suggests(BrigadierCommand::suggestOnlinePlayers)
                .executes(context -> {
                    final String targetName = StringArgumentType.getString(context, "player");
                    givePlayer(context.getSource().getSender(), targetName, false);
                    return 0;
                })
                .then(playerSilent());
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> playerSilent() {
        return Commands.literal("-s")
                .executes(context -> {
                    final String targetName = StringArgumentType.getString(context, "player");
                    givePlayer(context.getSource().getSender(), targetName, true);
                    return 0;
                });
    }

    private void givePlayer(final @NotNull CommandSender sender, final @NotNull String targetName, final boolean silent) {
        final Player player = sender instanceof Player p ? p : null;
        final boolean isPlayer = player != null;

        final Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            if (isPlayer) {
                LocaleManager.sendMessage(player, ErrorMessageKeys.INVALID_PLAYER, "{PLAYER}", targetName);
                return;
            }

            AstraLog.log(AstraLogLevel.ERROR, "Invalid player: " + targetName);
            return;
        }

        target.getInventory().addItem(MineWand.getInstance().getMineWand());
        if (isPlayer) {
            LocaleManager.sendMessage(player, MineWandMessageKeys.GIVE_OTHER, "{PLAYER}", target.getName());
        }

        if (silent) {
            return;
        }

        LocaleManager.sendMessage(target, MineWandMessageKeys.GIVE_RECEIVED);
        if (!isPlayer) {
            AstraLog.log(AstraLogLevel.SUCCESS, "You gave " + target.getName() + " a mine wand!");
        }
    }

}
