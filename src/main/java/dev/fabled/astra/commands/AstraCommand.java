package dev.fabled.astra.commands;

import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.Astra;
import dev.fabled.astra.AstraPlugin;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.logger.AstraLog;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AstraCommand extends BrigadierCommand {

    private final AstraPlugin plugin;

    public AstraCommand(AstraPlugin plugin) {
        super(
                "astra",
                new String[]{"astraprison"},
                "astra.admin",
                "The main AstraPrison admin command!",
                "/astra"
        );
        this.plugin = plugin;
    }

    @Override
    @NotNull
    CommandNode<CommandSourceStack> buildCommandNode() {
        return literal(name)
                .executes(c -> {
                    if (!(getSender(c) instanceof Player player)) {
                        AstraLog.log(
                                "AstraPrison v" + Astra.getPlugin().getPluginMeta().getVersion(),
                                "Developed by Mantice and DrDivx2k",
                                "Type 'astra help' for more commands!"
                        );
                        return -1;
                    }

                    player.sendMessage(MiniColor.parse("AstraPrison v" + Astra.getPlugin().getPluginMeta().getVersion()));
                    player.sendMessage(MiniColor.parse("Developed by Mantice and DrDivx2k"));
                    player.sendMessage(MiniColor.parse("<gray>Type <white>/astra help<gray> for more commands!"));
                    return 0;
                })
                .then(literal("reload")
                        .executes(context -> {
                            CommandSender sender = getSender(context);
                            if (sender instanceof Player player) {
                                if (player.hasPermission("astra.reload")) {
                                    plugin.onReload();
                                    player.sendMessage("Astra plugin reloaded!");
                                } else {
                                    player.sendMessage("You don't have permission to use this command!");
                                }
                            } else {
                                plugin.onReload();
                                sender.sendMessage("Astra plugin reloaded!");
                            }
                            return 1;
                        }))
                .build();
    }
}
