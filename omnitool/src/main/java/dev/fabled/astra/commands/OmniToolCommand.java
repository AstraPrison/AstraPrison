package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.console.ConsoleLang;
import dev.fabled.astra.lang.impl.ErrorLang;
import dev.fabled.astra.lang.impl.OmniToolLang;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static dev.fabled.astra.omnitool.OmniToolItem.createOmniToolForPlayer;

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
    public @NotNull CommandNode<CommandSourceStack> buildCommandNode() {
        return literal(name).executes(this::help)
                .then(literal("give")
                        .requires(permissionRequirement("astra.omnitool.give"))
                        .then(arg("player", StringArgumentType.word())
                                .suggests(this::suggestOnlinePlayers)
                                .executes(this::giveOmniTool)))
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
    private int giveOmniTool(@NotNull final CommandContext<CommandSourceStack> context) {
        final String playerName = StringArgumentType.getString(context, "player");
        final Player target = Bukkit.getPlayer(playerName);

        if (target == null) {
            if (!(getSender(context) instanceof Player player)) {
                AstraLog.log(AstraLogLevel.ERROR, ConsoleLang.INVALID_PLAYER + "'" + playerName + "'");
                return 0;
            }

            LocaleManager.send(player, ErrorLang.INVALID_PLAYER, "{PLAYER}", playerName);
            return 0;
        }

        ItemStack omniTool = createOmniToolForPlayer(target);
        target.getInventory().addItem(omniTool);
        target.sendMessage("OmniTool given!");
        resetXPOnGive(target);
        return 0;
    }

    private void resetXPOnGive(Player player) {
        player.setExp(0);
        player.setLevel(0);
    }

}
