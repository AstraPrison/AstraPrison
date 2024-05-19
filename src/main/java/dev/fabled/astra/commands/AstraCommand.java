package dev.fabled.astra.commands;

import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.Astra;
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
                .build();
    }

}
