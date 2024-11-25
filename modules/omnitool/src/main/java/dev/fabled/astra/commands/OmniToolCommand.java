package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.fabled.astra.omnitool.OmniTool;
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

    private final @NotNull List<String> helpPlayer;
    private final @NotNull List<String> helpConsole;

    public OmniToolCommand() {
        super(
                "omnitool",
                "The omni-tool admin command!",
                "omnitooladmin"
        );

        helpPlayer = List.of(
                "<b><aqua>Astra<dark_aqua>Prison<reset> Omni-Tool Commands:",
                "<dark_gray>| <white>/omnitool <dark_gray>- <gray>Shows this information!",
                "<dark_gray>| <white>/astra give <dark_gray>- <gray>Give a player a new omni-tool!"
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

    private LiteralArgumentBuilder<CommandSourceStack> base() {
        return Commands.literal(name)
                .requires(permissionRequirement("astra.admin"))
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        AstraLog.log(helpConsole);
                        return 0;
                    }

                    player.sendMessage(MiniColor.CHAT.deserialize(String.join("\n", helpPlayer)));
                    return 0;
                })
                .then(give());
    }

    private LiteralArgumentBuilder<CommandSourceStack> give() {
        return Commands.literal("give")
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.ERROR, "Select a player!");
                        return 0;
                    }

                    player.getInventory().addItem(OmniTool.getDefaultOmniTool());
                    player.sendMessage(MiniColor.CHAT.deserialize("<green>You got a new Omni-Tool!"));
                    return 0;
                })
                .then(givePlayer());
    }

    private RequiredArgumentBuilder<CommandSourceStack, String> givePlayer() {
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

    private LiteralArgumentBuilder<CommandSourceStack> givePlayerSilent() {
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
                player.sendMessage(MiniColor.CHAT.deserialize("<red>Invalid player: " + targetName));
                return;
            }

            AstraLog.log(AstraLogLevel.ERROR, "Invalid player: " + targetName);
            return;
        }

        target.getInventory().addItem(OmniTool.getDefaultOmniTool());
        if (isPlayer) {
            player.sendMessage(MiniColor.CHAT.deserialize("<green>You gave <white> " + target.getName() + "<green> a new omni-tool!"));
        }

        if (silent) {
            return;
        }

        target.sendMessage(MiniColor.CHAT.deserialize("<green>You were given a new omni-tool!"));
        if (!isPlayer) {
            AstraLog.log(AstraLogLevel.SUCCESS, "You gave " + target.getName() + " a new omni-tool!");
        }
    }

}
