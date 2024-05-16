package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.omnitool.OmniTool;
import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.MiniColor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
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
    @NotNull
    CommandNode<CommandSourceStack> buildCommandNode() {
        return literal(name)
                .executes(context -> {
                    if (!(getSender(context) instanceof Player player)) {
                        return 0;
                    }

                    player.sendMessage("OmniTool Admin Commands:");
                    return 0;
                })

                .then(literal("give")
                        .executes(context -> {
                            if (!(getSender(context) instanceof Player player)) {
                                return 0;
                            }

                            player.sendMessage(MiniColor.parse("<red>Select a player!"));
                            return 0;
                        })

                        .then(arg("player", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                        ListUtils.playerNames(), builder
                                ))

                                .executes(context -> {
                                    if (!(getSender(context) instanceof Player player)) {
                                        return 0;
                                    }

                                    final String playerName = StringArgumentType.getString(context, "player");
                                    final Player target = Bukkit.getPlayer(playerName);
                                    if (target == null) {
                                        player.sendMessage(MiniColor.parse("<red>Invalid player: '<white>" + playerName + "<red>'"));
                                        return 0;
                                    }

                                    OmniTool.give(target);
                                    player.sendMessage(MiniColor.parse("<green>Gave the omni-tool to <white>" + target.getName() + "<green>!"));
                                    return 0;
                                })))
                .build();
    }

}
