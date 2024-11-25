package dev.fabled.astra.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.fabled.astra.AstraPrison;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
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
                "| astra reload - Reloads plugin configuration!"
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
                        AstraLog.log(pluginInfoConsole);
                        return 0;
                    }

                    player.sendMessage(MiniColor.CHAT.deserialize(String.join("\n", pluginInfoPlayer)));
                    return 0;
                })
                .then(help())
                .then(reload());
    }

    private LiteralArgumentBuilder<CommandSourceStack> help() {
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

    private LiteralArgumentBuilder<CommandSourceStack> reload() {
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

}
