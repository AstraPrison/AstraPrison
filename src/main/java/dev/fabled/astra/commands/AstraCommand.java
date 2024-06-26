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

    public AstraCommand() {
        super(
                "astra",
                new String[]{"astraprison"},
                "astra.admin",
                "The main AstraPrison admin command!",
                "/astra"
        );
    }

    @Override
    public @NotNull CommandNode<CommandSourceStack> buildCommandNode() {
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
                            if (!(getSender(context) instanceof Player player)) {
                                final long start = System.currentTimeMillis();
                                AstraPlugin.getInstance().onReload();
                                final long end = System.currentTimeMillis() - start;

                                AstraLog.log(AstraLogLevel.SUCCESS, "Reloaded in " + end + "ms!");
                                return 0;
                            }

                            if (!player.hasPermission("astra.admin.reload")) {
                                LocaleManager.send(player, ErrorLang.NO_PERMISSION, "{PERMISSION}", "astra.admin.reload");
                                return 0;
                            }

                            final long start = System.currentTimeMillis();
                            AstraPlugin.getInstance().onReload();
                            final long end = System.currentTimeMillis() - start;

                            LocaleManager.send(player, AstraAdminLang.RELOADED, "{MS}", String.valueOf(end));
                            return 0;
                        })
                )
                .build();
    }
}
