package dev.paracausal.astra.commands;

import dev.paracausal.astra.Astra;
import dev.paracausal.astra.commands.annotations.*;
import dev.paracausal.astra.exceptions.InvalidCommandException;
import dev.paracausal.astra.logger.AstraLog;
import dev.paracausal.astra.logger.AstraLogLevel;
import dev.paracausal.astra.utilities.MiniColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(
        commandName = "astra",
        aliases = {"astraprison", "astraprisons"},
        permission = "astra.admin",
        usage = "/astra help",
        description = "The admin command for Astra!"
)
public class AdminCommand extends AstraCommand {

    public AdminCommand() throws InvalidCommandException {
        super();
    }

    @CommandBase
    @ConsoleCommand
    public void base() {
        AstraLog.log(
                "Astra v" + Astra.getPlugin().getDescription().getVersion(),
                "Developed by Mantice",
                "Type 'astra help' for more commands!"
        );
    }

    @CommandBase
    @PlayerCommand
    public void base(@NotNull final Player player) {
        player.sendMessage("Astra v" + Astra.getPlugin().getDescription().getVersion());
        player.sendMessage("Developed by Mantice!");
        player.sendMessage(MiniColor.ALL.deserialize("<gray>Type <white>/astra help <gray>for more commands!"));
    }

    @SubCommand(name = "help")
    @ConsoleCommand
    public void help() {
        AstraLog.log(
                "Astra Commands:",
                "| 'astra' - Shows plugin information!",
                "| 'astra help' - Shows this information!",
                "| 'astra reload' - Reloads plugin configuration!"
        );
    }

    @SubCommand(name = "help")
    @PlayerCommand("astra.admin.help")
    public void help(@NotNull final Player player) {
        player.sendMessage(MiniColor.ALL.deserialize("<b><aqua>Astra</b> <white>Commands:"));
        player.sendMessage(MiniColor.ALL.deserialize("<dark_gray>| <white>/astra <dark_gray>- <gray>Shows plugin information!"));
        player.sendMessage(MiniColor.ALL.deserialize("<dark_gray>| <white>/astra help <dark_gray>- <gray>Shows this information!"));
        player.sendMessage(MiniColor.ALL.deserialize("<dark_gray>| <white>/astra reload <dark_gray>- <gray>Reloads plugin configuration!"));
    }

    @SubCommand(name = "reload", aliases = {"rl"})
    @ConsoleCommand
    public void reload() {
        final long start = System.currentTimeMillis();
        final long end = System.currentTimeMillis() - start;
        AstraLog.log(AstraLogLevel.SUCCESS, "Reloaded in " + end + "ms!");
    }

    @SubCommand(name = "reload", aliases = {"rl"})
    @PlayerCommand("astra.admin.reload")
    public void reload(@NotNull final Player player) {
        final long start = System.currentTimeMillis();
        final long end = System.currentTimeMillis() - start;
        player.sendMessage(MiniColor.ALL.deserialize("<green>Reloaded in <white>" + end + "ms<green>!"));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull Player player, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (player.hasPermission("astra.admin.help")) {
                completions.add("help");
            }
            if (player.hasPermission("astra.admin.reload")) {
                completions.add("reload");
            }
        }

        return completions;
    }

}
