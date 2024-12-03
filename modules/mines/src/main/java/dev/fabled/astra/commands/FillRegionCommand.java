package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.fabled.astra.mines.wand.MineWandSelection;
import dev.fabled.astra.modules.impl.MinesModule;
import dev.fabled.astra.nms.AbstractFakeBlockHandler;
import dev.fabled.astra.nms.NMSFactory;
import dev.fabled.astra.nms.regions.shapes.CuboidRegion;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.WeightedList;
import dev.fabled.astra.utils.blocks.BlockMaterial;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public final class FillRegionCommand extends BrigadierCommand {

    private final @NotNull MineWandSelection selection;

    public FillRegionCommand() {
        super(
                "fillregion",
                "The admin command to test with fake blocks!"
        );

        selection = MinesModule.getInstance().getMineWandSelection();
    }

    @Override
    @NotNull LiteralCommandNode<CommandSourceStack> node() {
        return base().build();
    }

    private boolean checkSelection(final @NotNull Player player) {
        final int value = selection.hasValidSelection(player);
        boolean result = false;

        switch (value) {
            case 1 -> player.sendMessage(MiniColor.CHAT.deserialize("<red>You are missing corner one!"));
            case 2 -> player.sendMessage(MiniColor.CHAT.deserialize("<red>You are missing corner two!"));
            case 3 -> player.sendMessage(MiniColor.CHAT.deserialize("<red>Your corners are in different worlds!"));
            default -> result = true;
        }

        return result;
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> base() {
        return Commands.literal(name)
                .requires(permissionRequirement("astra.admin"))
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.ERROR, "Please run this command as a player!");
                        return 0;
                    }

                    if (!checkSelection(player)) {
                        return 0;
                    }

                    player.sendMessage(MiniColor.CHAT.deserialize("<red>Please select materials to fill the mine with!"));
                    return 0;
                })
                .then(materials());
    }

    private @NotNull RequiredArgumentBuilder<CommandSourceStack, String> materials() {
        return Commands.argument("materials", StringArgumentType.greedyString())
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.ERROR, "Please run this command as a player!");
                        return 0;
                    }

                    if (!checkSelection(player)) {
                        return 0;
                    }

                    final String input = StringArgumentType.getString(context, "materials");
                    final String[] split = input.split(" ");

                    final WeightedList<BlockMaterial> list = new WeightedList<>();

                    for (final String s : split) {
                        double chance = 100.0d;
                        BlockMaterial material;

                        if (s.contains(":")) {
                            final String[] chanceSplit = s.split(":");

                            try { material = new BlockMaterial(Material.valueOf(chanceSplit[0].toUpperCase())); }
                            catch (IllegalArgumentException e) {
                                continue;
                            }

                            if (chanceSplit.length > 1) {
                                try { chance = Double.parseDouble(chanceSplit[1]); }
                                catch (NumberFormatException ignored) {}
                            }
                        }

                        else {
                            try { material = new BlockMaterial(Material.valueOf(s.toUpperCase())); }
                            catch (IllegalArgumentException e) {
                                continue;
                            }
                        }

                        list.add(material, chance);
                    }

                    final AbstractFakeBlockHandler fakeBlockHandler = NMSFactory.getNMSHandler().getFakeBlockHandler();
                    final Location cornerOne = selection.getCornerOne(player);
                    final Location cornerTwo = selection.getCornerTwo(player);
                    assert cornerOne != null;
                    assert cornerTwo != null;

                    fakeBlockHandler.fillRegion(player, list, new CuboidRegion(cornerOne, cornerTwo));
                    player.sendMessage(MiniColor.CHAT.deserialize("<green>Cuboid region has been filled!"));
                    return 0;
                });
    }

}
