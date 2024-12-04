package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.fabled.astra.commands.arguments.MaterialArgumentType;
import dev.fabled.astra.locale.LocaleManager;
import dev.fabled.astra.locale.impl.SellPriceMessageKeys;
import dev.fabled.astra.permissions.AstraPermission;
import dev.fabled.astra.sell.SellPriceManager;
import dev.fabled.astra.utils.MaterialList;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class SellPriceCommand extends BrigadierCommand {

    public SellPriceCommand() {
        super("sellprice", "Set or check the sell price of a block!");
    }

    @Override
    @NotNull LiteralCommandNode<CommandSourceStack> node() {
        return base().build();
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> base() {
        return Commands.literal(name)
                .requires(permissionRequirement(AstraPermission.SELL_PRICE))
                .executes(context -> {
                    if (!(context.getSource().getSender() instanceof Player player)) {
                        AstraLog.log(
                                "Sell Price Commands:",
                                "| 'sellprice set' - Set the sell price for a block!",
                                "| 'sellprice check' - Check the current sell price for a block!"
                        );
                        return 0;
                    }

                    LocaleManager.sendMessage(player, SellPriceMessageKeys.HELP);
                    return 0;
                })
                .then(set())
                .then(check());
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> set() {
        return Commands.literal("set")
                .requires(permissionRequirement(AstraPermission.SELL_PRICE_SET))
                .executes(context -> {
                    if (!(context.getSource().getSender() instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.ERROR, "Please input a price!");
                        return 0;
                    }

                    LocaleManager.sendMessage(player, SellPriceMessageKeys.INPUT_PRICE);
                    return 0;
                })
                .then(setPrice());
    }

    private @NotNull RequiredArgumentBuilder<CommandSourceStack, Double> setPrice() {
        return Commands.argument("price", DoubleArgumentType.doubleArg(0.01d))
                .executes(context -> {
                    if (!(context.getSource().getSender() instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.ERROR, "Please select a block!");
                        return 0;
                    }

                    final Material materialInHand = player.getInventory().getItemInMainHand().getType();
                    if (materialInHand.isAir() || !materialInHand.isBlock()) {
                        LocaleManager.sendMessage(player, SellPriceMessageKeys.INPUT_BLOCK);
                        return 0;
                    }

                    final double price = DoubleArgumentType.getDouble(context, "price");
                    SellPriceManager.getInstance().setPrice(materialInHand, price);

                    LocaleManager.sendMessage(
                            player, SellPriceMessageKeys.SET,
                            "{BLOCK}", materialInHand.name(),
                            "{PRICE}", String.valueOf(price)
                    );
                    return 0;
                })
                .then(setPriceBlock());
    }

    private @NotNull RequiredArgumentBuilder<CommandSourceStack, Material> setPriceBlock() {
        return Commands.argument("block", MaterialArgumentType.block())
                .suggests(MaterialList::suggestBlocks)
                .executes(context -> {
                    final double price = DoubleArgumentType.getDouble(context, "price");
                    final Material material = MaterialArgumentType.getMaterial(context, "block");
                    SellPriceManager.getInstance().setPrice(material, price);

                    if (!(context.getSource().getSender() instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.SUCCESS, "Set the sell price to " + price + " for " + material.name() + "!");
                        return 0;
                    }

                    LocaleManager.sendMessage(
                            player, SellPriceMessageKeys.SET,
                            "{BLOCK}", material.name(),
                            "{PRICE}", String.valueOf(price)
                    );
                   return 0;
                });
    }

    private @NotNull LiteralArgumentBuilder<CommandSourceStack> check() {
        return Commands.literal("check")
                .requires(permissionRequirement(AstraPermission.SELL_PRICE_CHECK))
                .executes(context -> {
                    if (!(context.getSource().getSender() instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.ERROR, "Please select a block!");
                        return 0;
                    }

                    final Material materialInHand = player.getInventory().getItemInMainHand().getType();
                    if (materialInHand.isAir() || !materialInHand.isBlock()) {
                        LocaleManager.sendMessage(player, SellPriceMessageKeys.INPUT_BLOCK);
                        return 0;
                    }

                    double price = SellPriceManager.getInstance().getPrice(materialInHand);
                    LocaleManager.sendMessage(
                            player, SellPriceMessageKeys.PRICE,
                            "{BLOCK}", materialInHand.name(),
                            "{PRICE}", String.valueOf(price)
                    );
                    return 0;
                })
                .then(checkBlock());
    }

    private @NotNull RequiredArgumentBuilder<CommandSourceStack, Material> checkBlock() {
        return Commands.argument("block", MaterialArgumentType.block())
                .suggests(MaterialList::suggestBlocks)
                .executes(context -> {
                    final Material material = MaterialArgumentType.getMaterial(context, "block");
                    final double price = SellPriceManager.getInstance().getPrice(material);

                    if (!(context.getSource().getSender() instanceof Player player)) {
                        AstraLog.log(AstraLogLevel.SUCCESS, "The price of " + material.name() + " is $" + price + "!");
                        return 0;
                    }

                    LocaleManager.sendMessage(
                            player, SellPriceMessageKeys.PRICE,
                            "{BLOCK}", material.name(),
                            "{PRICE}", String.valueOf(price)
                    );
                    return 0;
                });
    }

}
