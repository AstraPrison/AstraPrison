package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
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
import org.bukkit.enchantments.Enchantment;
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
                .then(literal("enchant")
                        .then(arg("player", StringArgumentType.word())
                                .executes(this::missingEnchantArgs)
                                .then(arg("enchantment", StringArgumentType.word())
                                        .executes(this::missingEnchantArgs)
                                        .then(arg("level", IntegerArgumentType.integer())
                                                .executes(this::enchant)
                                        )
                                )
                                .suggests(this::suggestPlayers)
                        )
                )
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
        target.sendMessage("Mine wand given!");
        return 0;
    }

    private int enchant(@NotNull final CommandContext<CommandSourceStack> context) {
        final String playerName = StringArgumentType.getString(context, "player");
        final String enchantmentName = StringArgumentType.getString(context, "enchantment");
        final int level = IntegerArgumentType.getInteger(context, "level");
        final Player target = Bukkit.getPlayer(playerName);

        if (!(getSender(context) instanceof Player player)) {
            if (target == null) {
                AstraLog.log(AstraLogLevel.ERROR, ConsoleLang.INVALID_PLAYER + "'" + playerName + "'");
                return -1;
            }

            Enchantment enchantment = Enchantment.getByName(enchantmentName.toUpperCase());
            if (enchantment == null) {
                AstraLog.log(AstraLogLevel.ERROR, ConsoleLang.INVALID_ENCHANTMENT + "'" + enchantmentName + "'");
                return -1;
            }

            applyEnchantment(target, enchantment, level);
            AstraLog.log(AstraLogLevel.SUCCESS, ConsoleLang.OMNI_TOOL_ENCHANT + target.getName() + "!");
            return 0;
        }

        if (target == null) {
            LocaleManager.send(player, ErrorLang.INVALID_PLAYER, "{PLAYER}", playerName);
            return -1;
        }

        Enchantment enchantment = Enchantment.getByName(enchantmentName.toUpperCase());
        if (enchantment == null) {
            LocaleManager.send(player, ErrorLang.INVALID_ENCHANTMENT, "{ENCHANTMENT}", enchantmentName);
            return -1;
        }

        applyEnchantment(target, enchantment, level);
        LocaleManager.send(player, OmniToolLang.ADMIN_ENCHANT, "{PLAYER}", target.getName(), "{ENCHANTMENT}", enchantmentName, "{LEVEL}", String.valueOf(level));
        return 0;
    }

    private void applyEnchantment(Player player, Enchantment enchantment, int level) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand != null) {
            itemInHand.addUnsafeEnchantment(enchantment, level);
        }
    }

    private int missingEnchantArgs(@NotNull final CommandContext<CommandSourceStack> context) {
        if (!(getSender(context) instanceof Player player)) {
            AstraLog.log(AstraLogLevel.ERROR, ConsoleLang.MISSING_ENCHANT_ARGS);
            return 0;
        }

        LocaleManager.send(player, ErrorLang.MISSING_ENCHANT_ARGS);
        return 0;
    }

}
