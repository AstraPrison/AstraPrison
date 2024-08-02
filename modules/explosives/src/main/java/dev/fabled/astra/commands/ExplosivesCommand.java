package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.items.BombItem;
import dev.fabled.astra.items.DrillsItem;
import dev.fabled.astra.utils.MiniColor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ExplosivesCommand extends BrigadierCommand {

    public ExplosivesCommand() {
        super(
                "specials",
                new String[]{"explosive", "bombs", "drills"},
                "astra.admin.specials",
                "The admin command for bomb and drill management!",
                "/mineadmin help"
        );
    }

    private static int executeDrillCommand(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer serverPlayer = EntityArgument.getPlayer(context, "player");
            Player player = Bukkit.getPlayer(serverPlayer.getUUID());
            String item = StringArgumentType.getString(context, "item");

            if (player == null || !player.isOnline()) {
                System.out.println("Player not found or offline.");
                return 0;
            }

            ItemStack drillItem;
            if ("normalDrill".equalsIgnoreCase(item)) {
                drillItem = DrillsItem.createNormalDrill();
            } else if ("bigDrill".equalsIgnoreCase(item)) {
                drillItem = DrillsItem.createBigDrill();
            } else if ("ultraDrill".equalsIgnoreCase(item)) {
                drillItem = DrillsItem.createUltraDrill();
            } else {
                player.sendMessage(MiniColor.parse("<red>Please use one of the following: normalDrill, bigDrill, ultraDrill"));
                return 0;
            }

            player.getInventory().addItem(drillItem);
            player.sendMessage(MiniColor.parse("<green>" + player.getName() + " received the " + item + "!"));
            return 1;

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    private static int executeBombCommand(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer serverPlayer = EntityArgument.getPlayer(context, "player");
            Player player = Bukkit.getPlayer(serverPlayer.getUUID());
            String item = StringArgumentType.getString(context, "item");

            if (player == null) {
                System.out.println("Player not found or offline.");
                return 0;
            }

            ItemStack drillItem;
            if ("normalBomb".equalsIgnoreCase(item)) {
                drillItem = BombItem.createNormalBomb();
            } else if ("bigBomb".equalsIgnoreCase(item)) {
                drillItem = BombItem.createBigBomb();
            } else if ("ultraBomb".equalsIgnoreCase(item)) {
                drillItem = BombItem.createUltraBomb();
            } else {
                player.sendMessage(MiniColor.parse("<red>Please use one of the following: normalDrill, bigDrill, ultraDrill"));
                return 0;
            }

            player.getInventory().addItem(drillItem);
            player.sendMessage(MiniColor.parse("<green>" + player.getName() + " received the " + item + "!"));
            return 1;

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @NotNull
    public CommandNode<CommandSourceStack> buildCommandNode() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal(name)
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("givedrills")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("item", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            builder.suggest("normalDrill");
                                            builder.suggest("bigDrill");
                                            builder.suggest("ultraDrill");
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> executeDrillCommand(context)
                                        ))))
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("givebombs")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("item", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            builder.suggest("normalBomb");
                                            builder.suggest("bigBomb");
                                            builder.suggest("ultraBomb");
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> executeBombCommand(context)
                                        ))))
                .build();
    }
}
