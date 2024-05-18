package dev.fabled.astra.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.Astra;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.console.ConsoleLang;
import dev.fabled.astra.lang.impl.AstraAdminLang;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.logger.AstraLog;
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
    @NotNull CommandNode<CommandSourceStack> buildCommandNode() {
        return literal(name).executes(this::base)
                .then(literal("help").executes(this::help))
                .build();
    }

    @SuppressWarnings({"SameReturnValue", "UnstableApiUsage"})
    private int base(@NotNull final CommandContext<CommandSourceStack> context) {
        if (!(getSender(context) instanceof Player player)) {
            AstraLog.log(
                    "AstraPrison v" + Astra.getPlugin().getPluginMeta().getVersion(),
                    "Developed by Mantice and DrDivx2k",
                    "Type 'astra help' for more commands!"
            );
            return 0;
        }

        player.sendMessage(MiniColor.parse("AstraPrison v" + Astra.getPlugin().getPluginMeta().getVersion()));
        player.sendMessage(MiniColor.parse("Developed by Mantice and DrDivx2k"));
        player.sendMessage(MiniColor.parse("<gray>Type <white>/astra help<gray> for more commands!"));
        return 0;
    }

    /*
            TODO (Later On)
            Replace player help command with a GUI that lists all modules and their commands
     */
    @SuppressWarnings("SameReturnValue")
    private int help(@NotNull final CommandContext<CommandSourceStack> context) {
        if (!(getSender(context) instanceof Player player)) {
            AstraLog.log(ConsoleLang.ADMIN_HELP);
            return 0;
        }

        LocaleManager.send(player, AstraAdminLang.ADMIN_HELP);
        return 0;
    }

}
