package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.menus.MinePanel;
import dev.fabled.astra.mines.generator.MineGenerator;
import dev.fabled.astra.mines.wand.MineWand;
import dev.fabled.astra.utils.MineData;
import dev.fabled.astra.utils.MineReader;
import dev.fabled.astra.utils.MineWriter;
import dev.fabled.astra.utils.MiniColor;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static dev.fabled.astra.utils.MineWriter.FILE;


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

    private static int execute(CommandContext<Object> context) {
        String mineName = StringArgumentType.getString(context, "mineName");
        MineData mineData = MineReader.readMineData("path/to/your/file.json", mineName);
        if (mineData != null) {
            System.out.println("Mine data found for: " + mineName);
        } else {
            System.out.println("No mine data found for: " + mineName);
        }
        return 1;
    }

    @Override
    public @NotNull CommandNode<CommandSourceStack> buildCommandNode() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal(name)
                .executes(context -> {
                    if (!(getSender(context) instanceof Player player)) {
                        return 0;
                    }

                    player.sendMessage("Mine Admin Base Command");
                    return 0;
                })
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("wand")
                        .executes(context -> {
                            if (!(getSender(context) instanceof Player player)) {
                                return 0;
                            }

                            MineWand.give(player);
                            player.sendMessage("Mine wand given!");
                            return 0;
                        }))
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("generate")
                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("minename", StringArgumentType.word())
                                .suggests(new MineSuggestionProvider())
                                .executes(context -> {
                                    if (!(getSender(context) instanceof Player player)) {
                                        return 0;
                                    }

                                    String mineName = StringArgumentType.getString(context, "minename");
                                    MineData mineData = MineReader.readMineData(FILE, mineName);

                                    if (mineData == null) {
                                        player.sendMessage("Mine not found!");
                                        return 0;
                                    }

                                    Collection<BlockState> blockChanges = MineGenerator.getBlocks(player.getUniqueId(), mineName);
                                    player.sendBlockChanges(blockChanges);
                                    player.sendMessage("Mine generated!");

                                    return 1;
                                })))
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("panel")
                        .executes(context -> {
                            if (!(getSender(context) instanceof Player player)) {
                                return 0;
                            }
                            player.openInventory(new MinePanel().getInventory());
                            return 0;
                        }))
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("create")
                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("name", StringArgumentType.word())
                                .executes(context -> {
                                    if (!(getSender(context) instanceof Player player)) {
                                        return 0;
                                    }

                                    UUID playerId = player.getUniqueId();
                                    if (!MineWand.hasPositionOne(player) || !MineWand.hasPositionTwo(player)) {
                                        player.sendMessage(MiniColor.parse("<red>Please set both positions before creating the mine!"));
                                        return 0;
                                    }

                                    String mineName = StringArgumentType.getString(context, "name");
                                    Location pos1 = MineWand.getPositionOne(player);
                                    Location pos2 = MineWand.getPositionTwo(player);

                                    if (mineName.isEmpty()) {
                                        mineName = MineWand.generateMineName();
                                        player.sendMessage(MiniColor.parse("<yellow>No name provided. Using default name: " + mineName));
                                    } else if (MineWand.isMineNameTaken(mineName)) {
                                        player.sendMessage(MiniColor.parse("<red>The mine name \"" + mineName + "\" is already taken. Using default name."));
                                        mineName = MineWand.generateMineName();
                                    }

                                    MineWriter.writeMineToFile(pos1, pos2, mineName);
                                    player.sendMessage(MiniColor.parse("<green>The mine with the name \"" + mineName + "\" has been successfully created!"));

                                    MineWand.POSITION_ONE.remove(playerId);
                                    MineWand.POSITION_TWO.remove(playerId);

                                    return 1;
                                }))
                )
                .build();
    }

    public static class MineSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
            List<String> mineNames = MineReader.getMineNames(FILE);
            mineNames.forEach(builder::suggest);
            return builder.buildFuture();
        }
    }
}
