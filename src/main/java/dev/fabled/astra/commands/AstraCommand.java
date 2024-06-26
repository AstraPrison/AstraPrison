package dev.fabled.astra.commands;


import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.Astra;
import dev.fabled.astra.AstraPlugin;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.console.ConsoleLang;
import dev.fabled.astra.lang.impl.AstraAdminLang;
import dev.fabled.astra.lang.impl.ErrorLang;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import net.minecraft.commands.CommandSourceStack;
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
                .executes(context -> {
                    if (!(getSender(context) instanceof Player player)) {
                        AstraLog.log(
                                "AstraPrison v" + Astra.getPlugin().getPluginMeta().getVersion(),
                                "Developed by Mantice and DrDivx2k",
                                "Type 'astra help' for more commands!"
                        );
                        return -1;
                    }

                    player.sendMessage(MiniColor.parse("<aqua>AstraPrison v" + Astra.getPlugin().getPluginMeta().getVersion()));
                    player.sendMessage(MiniColor.parse("Developed by Mantice and DrDivx2k"));
                    player.sendMessage(MiniColor.parse("<gray>Type <white>/astra help<gray> for more commands!"));
                    return 0;
                })

                .then(literal("help")
                        .requires(permissionRequirement("astra.admin.help"))
                        .executes(context -> {
                            if (!(getSender(context) instanceof Player player)) {
                                AstraLog.log(ConsoleLang.ADMIN_HELP);
                                return 0;
                            }

                            if (!player.hasPermission("astra.admin.help")) {
                                LocaleManager.send(player, ErrorLang.NO_PERMISSION, "{PERMISSION}", "astra.admin.help");
                                return 0;
                            }

                            LocaleManager.send(player, AstraAdminLang.HELP);
                            return 0;
                        })
                )

                .then(literal("reload")
                        .requires(permissionRequirement("astra.admin.reload"))
                        .executes(context -> {
                            if (!(getSender(context) instanceof Player player)) {
                                final long start = System.currentTimeMillis();
                                AstraPlugin.getInstance().onReload();
                                final long end = System.currentTimeMillis() - start;

                                AstraLog.log(AstraLogLevel.SUCCESS, ConsoleLang.ADMIN_RELOADED.replace("{MS}", String.valueOf(end)));
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
