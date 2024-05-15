package dev.fabled.astra.commands;

import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.mines.wand.MineWand;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MineAdminCommand extends BrigadierCommand {

    public MineAdminCommand() {
        super(
                "mineadmin",
                new String[]{"minesadmin", "adminmine", "adminmines"},
                "astra.admin.mine",
                "The admin command for mine management!",
                "/mineadmin help"
        );
    }

    @Override
    @NotNull CommandNode<CommandSourceStack> buildCommandNode() {
        return literal(name)
                .executes(context -> {
                    if (!(getSender(context) instanceof Player player)) {
                        return 0;
                    }

                    player.sendMessage("Mine Admin Base Command");
                    return 0;
                })
                .then(literal("wand")
                        .executes(context -> {
                            if (!(getSender(context) instanceof Player player)) {
                                return 0;
                            }

                            MineWand.give(player);
                            player.sendMessage("Mine wand given!");
                            return 0;
                        }))
                .build();
    }

}
