package dev.fabled.astra.commands;

import com.mojang.brigadier.tree.CommandNode;
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
        return literal(name)
                .executes(c -> {
                    if (!(getSender(c) instanceof Player player)) {
                        return -1;
                    }
                    
                    return 0;
                })
                .build();
    }

}
