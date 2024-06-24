package commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.commands.BrigadierCommand;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.console.ConsoleLang;
import dev.fabled.astra.lang.impl.ErrorLang;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import items.ArmorItem;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ArmorCommand extends BrigadierCommand {

    public ArmorCommand() {
        super(
                "armor",
                null,
                "astra.armor",
                "The armor admin command!",
                "/armor");
    }

    @Override
    @NotNull
    public CommandNode<CommandSourceStack> buildCommandNode() {
        return literal(name).executes(this::help)
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("giveArmor")
                        .then(arg("player", StringArgumentType.word())
                                .suggests(this::suggestPlayers)
                                .then(arg("category", StringArgumentType.word())
                                        .suggests(this::suggestCategories)
                                        .then(arg("armor", StringArgumentType.word())
                                                .suggests(this::suggestArmorTypes)
                                                .executes(this::giveArmor)))))
                .build();
    }

    private int help(@NotNull final CommandContext<CommandSourceStack> context) {
        if (!(getSender(context) instanceof Player player)) {
            AstraLog.log(ConsoleLang.ARMOR_ADMIN_HELP);
            return 0;
        }
        player.sendMessage("Usage: /armor giveArmor <player> <category> <armor>");
        return 0;
    }

    private int giveArmor(@NotNull final CommandContext<CommandSourceStack> context) {
        final String playerName = StringArgumentType.getString(context, "player");
        final String category = StringArgumentType.getString(context, "category");
        final String armorType = StringArgumentType.getString(context, "armor");
        final Player target = Bukkit.getPlayer(playerName);

        if (target == null) {
            if (!(getSender(context) instanceof Player player)) {
                AstraLog.log(AstraLogLevel.ERROR, ConsoleLang.INVALID_PLAYER + "'" + playerName + "'");
                return -1;
            }
            LocaleManager.send((Player) getSender(context), ErrorLang.INVALID_PLAYER, "{PLAYER}", playerName);
            return -1;
        }

        ItemStack armorItem = ArmorItem.createArmorItem(category.toLowerCase(), armorType.toLowerCase());
        if (armorItem == null) {
            LocaleManager.send((Player) getSender(context), ErrorLang.INVALID_ARMOR_TYPE, "{ARMOR}", armorType);
            return -1;
        }

        target.getInventory().addItem(armorItem);
        target.sendMessage("Armor item given!");
        return 0;
    }

    private CompletableFuture<Suggestions> suggestArmorTypes(final CommandContext<CommandSourceStack> context, final SuggestionsBuilder builder) {
        final String[] armorTypes = {"helmet", "chestplate", "leggings", "boots"};
        for (String armorType : armorTypes) {
            if (armorType.startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(armorType);
            }
        }
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestCategories(final CommandContext<CommandSourceStack> context, final SuggestionsBuilder builder) {
        final String[] categories = {"token", "money", "prestige", "keys"};
        for (String category : categories) {
            if (category.startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(category);
            }
        }
        return builder.buildFuture();
    }

    public CompletableFuture<Suggestions> suggestPlayers(final CommandContext<CommandSourceStack> context, final SuggestionsBuilder builder) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(player.getName());
            }
        }
        return builder.buildFuture();
    }
}
