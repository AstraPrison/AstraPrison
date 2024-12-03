package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.fabled.astra.locale.LocaleManager;
import dev.fabled.astra.locale.impl.ErrorMessageKeys;
import dev.fabled.astra.locale.impl.OmniToolMessageKeys;
import dev.fabled.astra.omnitool.OmniTool;
import dev.fabled.astra.permissions.AstraPermission;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class OmniToolCommand extends BrigadierCommand {

    private final @NotNull List<String> helpConsole;

    public OmniToolCommand() {
        super(
                "omnitool",
                "The omni-tool admin command!",
                "omnitooladmin"
        );

        helpConsole = List.of(
                "AstraPrison Omni-Tool Commands:",
                "| omnitool - Shows this information!",
                "| omnitool give - Give a player a new omni-tool!"
        );
    }

    @Override
    @NotNull LiteralCommandNode<CommandSourceStack> node() {
        return base().build();
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> base() {
        return Commands.literal(name)
                .requires(permissionRequirement(AstraPermission.OMNI_TOOL))
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        AstraLog.log(helpConsole);
                        return 0;
                    }

                    LocaleManager.sendMessage(player, OmniToolMessageKeys.HELP);
                    return 0;
                })
                .then(give());
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> give() {
        return Commands.literal("give")
                .requires(permissionRequirement(AstraPermission.OMNI_TOOL_GIVE))
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.ERROR, "Select a player!");
                        return 0;
                    }

                    player.getInventory().addItem(OmniTool.getDefaultOmniTool());
                    LocaleManager.sendMessage(player, OmniToolMessageKeys.GIVE_SELF);
                    return 0;
                })
                .then(givePlayer());
    }

    private @NotNull RequiredArgumentBuilder<CommandSourceStack, String> givePlayer() {
        return Commands.argument("player", StringArgumentType.word())
                .suggests(BrigadierCommand::suggestOnlinePlayers)
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    final String targetName = StringArgumentType.getString(context, "player");
                    givePlayerOmniTool(sender, targetName, false);
                    return 0;
                })
                .then(givePlayerSilent());
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> givePlayerSilent() {
        return Commands.literal("-s")
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    final String targetName = StringArgumentType.getString(context, "player");
                    givePlayerOmniTool(sender, targetName, true);
                    return 0;
                });
    }

    private void givePlayerOmniTool(final @NotNull CommandSender sender, final String targetName, final boolean silent) {
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

        target.getInventory().addItem(OmniTool.getDefaultOmniTool());
        if (isPlayer) {
            LocaleManager.sendMessage(player, OmniToolMessageKeys.GIVE_OTHER, "{PLAYER}", target.getName());
        }

        if (silent) {
            return;
        }

        LocaleManager.sendMessage(target, OmniToolMessageKeys.GIVE_RECEIVED);
        if (!isPlayer) {
            AstraLog.log(AstraLogLevel.SUCCESS, "You gave " + target.getName() + " a new omni-tool!");
        }
    }

}
