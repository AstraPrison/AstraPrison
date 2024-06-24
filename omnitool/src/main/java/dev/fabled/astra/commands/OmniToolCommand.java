package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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
    @NotNull
    public CommandNode<CommandSourceStack> buildCommandNode() {
        return literal(name).executes(this::help)
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("giveOmniTool")
                        .then(arg("player", StringArgumentType.word())
                                .suggests(this::suggestPlayers)
                                .executes(this::giveOmniTool)))
                .build();
    }

    private int help(@NotNull final CommandContext<CommandSourceStack> context) {
        if (!(getSender(context) instanceof Player player)) {
            AstraLog.log(ConsoleLang.OMNI_TOOL_ADMIN_HELP);
            return 0;
        }

        LocaleManager.send(player, OmniToolLang.ADMIN_HELP);
        return 0;
    }

    private int giveOmniTool(@NotNull final CommandContext<CommandSourceStack> context) {
        final String playerName = StringArgumentType.getString(context, "player");
        final Player target = Bukkit.getPlayer(playerName);

        if (target == null) {
            if (!(getSender(context) instanceof Player player)) {
                AstraLog.log(AstraLogLevel.ERROR, ConsoleLang.INVALID_PLAYER + "'" + playerName + "'");
                return -1;
            }
            LocaleManager.send((Player) getSender(context), ErrorLang.INVALID_PLAYER, "{PLAYER}", playerName);
            return -1;
        }

        ItemStack omniTool = createOmniToolForPlayer(target);
        target.getInventory().addItem(omniTool);
        target.sendMessage("OmniTool given!");
        resetXPongive(target);
        return 0;
    }

    public void resetXPongive(Player player) {
        player.setExp(0);
        player.setLevel(0);
    }
}
