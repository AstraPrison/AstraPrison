package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.console.ConsoleLang;
import dev.fabled.astra.lang.impl.ErrorLang;
import dev.fabled.astra.lang.impl.OmniToolLang;
import dev.fabled.astra.omnitool.OmniTool;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OmniToolCommand extends BrigadierCommand {

    public OmniToolCommand() {
        super(
                "omnitool",
                null,
                "astra.omnitool",
                "The omnitool admin command!",
                "/omnitool");
    }

    @Override
    @NotNull CommandNode<CommandSourceStack> buildCommandNode() {
        return literal(name).executes(this::help)
                .then(literal("give").executes(this::give)
                        .then(arg("player", StringArgumentType.word())
                                .executes(this::givePlayer)
                                .suggests(this::suggestOnlinePlayers)
                        )
                )
                .build();
    }

    @SuppressWarnings("SameReturnValue")
    private int help(@NotNull final CommandContext<CommandSourceStack> context) {
        if (!(getSender(context) instanceof Player player)) {
            AstraLog.log(ConsoleLang.OMNI_TOOL_ADMIN_HELP);
            return 0;
        }

        LocaleManager.send(player, OmniToolLang.ADMIN_HELP);
        return 0;
    }

    @SuppressWarnings("SameReturnValue")
    private int give(@NotNull final CommandContext<CommandSourceStack> context) {
        if (!(getSender(context) instanceof Player player)) {
            AstraLog.log(AstraLogLevel.ERROR, ConsoleLang.SELECT_PLAYER);
            return 0;
        }

        LocaleManager.send(player, ErrorLang.SELECT_PLAYER);
        return 0;
    }

    private int givePlayer(@NotNull final CommandContext<CommandSourceStack> context) {
        final String playerName = StringArgumentType.getString(context, "player");
        final Player target = Bukkit.getPlayer(playerName);

        if (!(getSender(context) instanceof Player player)) {
            if (target == null) {
                AstraLog.log(AstraLogLevel.ERROR, ConsoleLang.INVALID_PLAYER + "'" + playerName + "'");
                return -1;
            }

            OmniTool.give(target);
            AstraLog.log(AstraLogLevel.SUCCESS, ConsoleLang.OMNI_TOOL_GIVE + target.getName() + "!");
            return 0;
        }

        if (target == null) {
            LocaleManager.send(player, ErrorLang.INVALID_PLAYER, "{PLAYER}", playerName);
            return -1;
        }

        OmniTool.give(target);
        LocaleManager.send(player, OmniToolLang.ADMIN_GIVE, "{PLAYER}", target.getName());
        return 0;
    }

}
