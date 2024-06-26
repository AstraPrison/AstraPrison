package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.impl.MineAdminLang;
import dev.fabled.astra.menus.MinePanel;
import dev.fabled.astra.mines.generator.MineGenerator;
import dev.fabled.astra.mines.wand.MineWand;
import dev.fabled.astra.utils.MineData;
import dev.fabled.astra.utils.MineReader;
import dev.fabled.astra.utils.MineWriter;
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

                    LocaleManager.send(player, MineAdminLang.HELP);
                    return 0;
                })

                .then(literal("wand")
                        .executes(context -> {
                            if (!(getSender(context) instanceof Player player)) {
                                return 0;
                            }

                            MineWand.give(player);
                            LocaleManager.send(player, MineAdminLang.WAND_GIVEN);
                            return 0;
                        }))

                .then(literal("generate")
                        .then(arg("mine", StringArgumentType.word())
                                .suggests(new MineSuggestionProvider())
                                .executes(context -> {
                                    if (!(getSender(context) instanceof Player player)) {
                                        return 0;
                                    }

                                    String mineName = StringArgumentType.getString(context, "mine");
                                    MineData mineData = MineReader.readMineData(FILE, mineName);

                                    if (mineData == null) {
                                        LocaleManager.send(player, MineAdminLang.GENERATE_MINE_NOT_FOUND, "{NAME}", mineName);
                                        return 0;
                                    }

                                    Collection<BlockState> blockChanges = MineGenerator.getBlocks(player.getUniqueId(), mineName);
                                    player.sendBlockChanges(blockChanges);
                                    LocaleManager.send(player, MineAdminLang.GENERATE_SUCCESS, "{NAME}", mineName);

                                    return 0;
                                })))

                .then(literal("panel")
                        .executes(context -> {
                            if (!(getSender(context) instanceof Player player)) {
                                return 0;
                            }
                            player.openInventory(new MinePanel().getInventory());
                            return 0;
                        }))

                .then(literal("create")
                        .then(arg("name", StringArgumentType.word())
                                .executes(context -> {
                                    if (!(getSender(context) instanceof Player player)) {
                                        return 0;
                                    }

                                    final UUID uuid = player.getUniqueId();
                                    if (!MineWand.hasPositionOne(player)) {
                                        LocaleManager.send(player, MineAdminLang.CREATE_SELECT_CORNER_ONE);
                                        return 0;
                                    }

                                    if (!MineWand.hasPositionTwo(player)) {
                                        LocaleManager.send(player, MineAdminLang.CREATE_SELECT_CORNER_TWO);
                                        return 0;
                                    }

                                    String mineName = StringArgumentType.getString(context, "name");
                                    final Location cornerOne = MineWand.getPositionOne(player);
                                    final Location cornerTwo = MineWand.getPositionTwo(player);

                                    if (mineName.isEmpty()) {
                                        mineName = MineWand.generateMineName();
                                        LocaleManager.send(player, MineAdminLang.CREATE_NO_NAME_PROVIDED, "{DEFAULT}", mineName);;
                                    }

                                    if (MineWand.isMineNameTaken(mineName)) {
                                        final String generated = MineWand.generateMineName();
                                        LocaleManager.send(player, MineAdminLang.CREATE_MINE_NAME_TAKEN, "{NAME}", mineName, "{DEFAULT}", generated);
                                        mineName = generated;
                                    }

                                    MineWriter.writeMineToFile(cornerOne, cornerTwo, mineName);
                                    LocaleManager.send(player, MineAdminLang.CREATE_SUCCESS, "{NAME}", mineName);

                                    MineWand.POSITION_ONE.remove(uuid);
                                    MineWand.POSITION_TWO.remove(uuid);
                                    return 0;
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
