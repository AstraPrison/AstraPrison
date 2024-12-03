package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.fabled.astra.AstraPrison;
import dev.fabled.astra.menus.AstraMenu;
import dev.fabled.astra.menus.MenuManager;
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
public class AstraCommand extends BrigadierCommand {

    private final @NotNull List<String> pluginInfoPlayer;
    private final @NotNull List<String> pluginInfoConsole;
    private final @NotNull List<String> helpPlayer;
    private final @NotNull List<String> helpConsole;

    public AstraCommand() {
        super(
                "astra",
                "The main admin command for AstraPrison!",
                "astraprison"
        );

        final String version = AstraPrison.getInstance().getPluginMeta().getVersion();

        pluginInfoPlayer = List.of(
                "<b><aqua>Astra<dark_aqua>Prison<reset> v" + version,
                "Developed by Mantice!",
                "<gray>Type <white>/astra help<gray> for more commands!"
        );

        pluginInfoConsole = List.of(
                "AstraPrison v" + version,
                "Developed by Mantice!",
                "Type 'astra help' for more commands!"
        );

        helpPlayer = List.of(
                "<b><aqua>Astra<dark_aqua>Prison<reset> Commands:",
                "<dark_gray>| <white>/astra <dark_gray>- <gray>Shows plugin information!",
                "<dark_gray>| <white>/astra help <dark_gray>- <gray>Shows this information!",
                "<dark_gray>| <white>/astra reload <dark_gray>- <gray>Reloads plugin configuration!"
        );

        helpConsole = List.of(
                "AstraPrison Commands:",
                "| astra - Shows plugin information!",
                "| astra help - Shows this information!",
                "| astra reload - Reloads plugin configuration!",
                "| astra menu - Open any menu from its ID!"
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
                        AstraLog.log(pluginInfoConsole);
                        return 0;
                    }

                    player.sendMessage(MiniColor.CHAT.deserialize(String.join("\n", pluginInfoPlayer)));
                    return 0;
                })
                .then(help())
                .then(reload())
                .then(menu());
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> help() {
        return Commands.literal("help")
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        AstraLog.log(helpConsole);
                        return 0;
                    }

                    player.sendMessage(MiniColor.CHAT.deserialize(String.join("\n", helpPlayer)));
                    return 0;
                });
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> reload() {
        return Commands.literal("reload")
                .executes(context -> {
                    final long start = System.currentTimeMillis();
                    AstraPrison.getInstance().onReload();
                    final long end = System.currentTimeMillis() - start;

                    final CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.SUCCESS, "Successfully reloaded in " + end + "ms!");
                        return 0;
                    }

                    player.sendMessage(MiniColor.CHAT.deserialize("<green>Successfully reloaded in <white>" + end + "ms<green>!"));
                    return 0;
                });
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> menu() {
        return Commands.literal("menu")
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.ERROR, "Select a menu to open!");
                        return 0;
                    }

                    player.sendMessage(MiniColor.CHAT.deserialize("<red>Select a menu to open!"));
                    return 0;
                })
                .then(menuId());
    }

    // TODO add menu ID suggestion
    private @NotNull RequiredArgumentBuilder<CommandSourceStack, String> menuId() {
        return Commands.argument("id", StringArgumentType.word())
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    final Player player = sender instanceof Player p ? p : null;
                    final boolean isPlayer = player != null;

                    final String menuId = StringArgumentType.getString(context, "id");
                    final MenuManager menuManager = MenuManager.getInstance();
                    final AstraMenu menu = menuManager.getMenu(menuId);

                    if (menu == null) {
                        if (isPlayer) {
                            player.sendMessage(MiniColor.CHAT.deserialize("<red>Invalid menu:<white> " + menuId));
                            return 0;
                        }

                        AstraLog.log(AstraLogLevel.ERROR, "Invalid menu: " + menuId);
                        return 0;
                    }

                    if (!isPlayer) {
                        AstraLog.log(AstraLogLevel.ERROR, "Select a player to open the menu for!");
                        return 0;
                    }

                    player.openInventory(menu.getInventory(player));
                    return 0;
                })
                .then(menuIdPlayer());
    }

    private @NotNull RequiredArgumentBuilder<CommandSourceStack, String> menuIdPlayer() {
        return Commands.argument("player", StringArgumentType.word())
                .suggests(BrigadierCommand::suggestOnlinePlayers)
                .executes(context -> {
                    openMenuForPlayer(context, false);
                    return 0;
                })
                .then(menuIdPlayerSilent());
    }

    private @NotNull RequiredArgumentBuilder<CommandSourceStack, String> menuIdPlayerSilent() {
        return Commands.argument("-s", StringArgumentType.word())
                .suggests(BrigadierCommand::suggestOnlinePlayers)
                .executes(context -> {
                    openMenuForPlayer(context, true);
                    return 0;
                });
    }

    private void openMenuForPlayer(final @NotNull CommandContext<CommandSourceStack> context, final boolean silent) {
        final CommandSender sender = context.getSource().getSender();
        final Player player = sender instanceof Player p ? p : null;
        final boolean isPlayer = player != null;

        final String menuId = StringArgumentType.getString(context, "id").toLowerCase();
        final MenuManager menuManager = MenuManager.getInstance();
        final AstraMenu menu = menuManager.getMenu(menuId);

        if (menu == null) {
            if (isPlayer) {
                player.sendMessage(MiniColor.CHAT.deserialize("<red>Invalid menu:<white> " + menuId));
                return;
            }

            AstraLog.log(AstraLogLevel.ERROR, "Invalid menu: " + menuId);
            return;
        }

        final String targetName = StringArgumentType.getString(context, "player");
        final Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            if (isPlayer) {
                player.sendMessage(MiniColor.CHAT.deserialize("<red>Invalid player:<white> " + targetName));
                return;
            }

            AstraLog.log(AstraLogLevel.ERROR, "Invalid player: " + targetName);
            return;
        }

        target.openInventory(menu.getInventory(target));
        if (isPlayer) {
            player.sendMessage(MiniColor.CHAT.deserialize("<green>You opened the<white> " + menuId + "<green> menu for<white> " + target.getName() + "<green>!"));
            return;
        }

        if (!silent) {
            AstraLog.log(AstraLogLevel.SUCCESS, "Opened the " + menuId + " menu for " + target.getName() + "!");
        }
    }

}
