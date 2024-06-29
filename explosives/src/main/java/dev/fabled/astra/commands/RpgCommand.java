package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.Astra;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.impl.ErrorLang;
import dev.fabled.astra.lang.impl.ExplosivesLang;
import dev.fabled.astra.modules.impl.ExplosivesModule;
import dev.fabled.astra.utils.ListUtils;
import dev.fabled.astra.utils.MiniColor;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.logger.AstraLog;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RpgCommand extends BrigadierCommand {

    private final YamlConfig config;

    public RpgCommand() {
        super(
                "rpg",
                new String[]{"explosive", "bombs", "drills"},
                "astra.admin.rpg",
                "The admin command for bomb and drill management!",
                "/rpg"
        );

        config = ExplosivesModule.getInstance().getRPGYml();
    }

    @Override
    public @NotNull CommandNode<CommandSourceStack> buildCommandNode() {
        return literal(name)
                .executes(context -> {
                    if (!(getSender(context) instanceof Player player)) {
                        AstraLog.log(
                                "Admin Explosive Commands:",
                                "| 'rpg' - Shows this information!",
                                "| 'rpg give' - Give an rpg to a player!"
                        );
                        return 0;
                    }

                    LocaleManager.send(player, ExplosivesLang.SELECT_ITEM);
                    return 0;
                })
                .then(literal("give")
                        .requires(permissionRequirement("astra.admin.rpg.give"))
                        .executes(context -> {
                            if (!(getSender(context) instanceof Player player)) {
                                AstraLog.log("Please select a player!");
                                return 0;
                            }

                            LocaleManager.send(player, ErrorLang.SELECT_PLAYER);
                            return 0;
                        })
                                .then(arg("player", EntityArgument.player())
                                        .suggests(this::suggestOnlinePlayers)
                                        .executes(context -> {
                                            if (!(getSender(context) instanceof Player player)) {
                                                AstraLog.log("Please select an item to give!");
                                                return 0;
                                            }

                                            LocaleManager.send(player, ExplosivesLang.SELECT_ITEM);
                                            return 0;
                                        })

                                        .then(arg("item", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    for (final String rarity : getRarityList()) {
                                                        builder.suggest(rarity);
                                                    }

                                                    return builder.buildFuture();
                                                })
                                                .executes(this::givePumpkinLauncher)
                                        )
                                )
                )
                .build();
    }

    @SuppressWarnings("SameReturnValue")
    private int givePumpkinLauncher(CommandContext<CommandSourceStack> context) {
        final ServerPlayer serverPlayer;

        try { serverPlayer = EntityArgument.getPlayer(context, "player"); }
        catch (CommandSyntaxException e) {
            AstraLog.log(e);
            return 0;
        }

        final Player target = Bukkit.getPlayer(serverPlayer.getUUID());
        if (target == null) {
            return 0;
        }

        final String rarity = StringArgumentType.getString(context, "item").toLowerCase();

        if (!config.options().contains("tiers." + rarity)) {
            target.sendMessage("Invalid rarity. Please choose from: " + String.join(", ", getRarityList()));
            return 0;
        }

        final ItemStack pumpkinLauncher = createPumpkinLauncher(rarity);
        target.getInventory().addItem(pumpkinLauncher);
        LocaleManager.send(target, ExplosivesLang.RECEIVED, "{ITEM}", pumpkinLauncher.getItemMeta().getDisplayName());
        return 0;
    }

    private ItemStack createPumpkinLauncher(String rarity) {
        ItemStack pumpkinLauncher = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta meta = pumpkinLauncher.getItemMeta();

        int uses;

        ConfigurationSection raritySection = config.options().getConfigurationSection("thresholds." + rarity);
        if (raritySection != null) {
            meta.displayName(MiniColor.INVENTORY.deserialize(raritySection.getString("displayName", "")));
            uses = config.options().getInt("thresholds." + rarity + ".uses");
            int customModelData = raritySection.getInt("customModelData");
            meta.getPersistentDataContainer().set(new NamespacedKey(Astra.getPlugin(), "customModelData"), PersistentDataType.INTEGER, customModelData);

            List<Component> configLore = MiniColor.INVENTORY.deserialize(ListUtils.fromConfig(config, "lore"));
            meta.lore(configLore);

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            pumpkinLauncher.setItemMeta(meta);

            return pumpkinLauncher;
        } else {
            throw new IllegalArgumentException("Invalid rarity.");
        }
    }

    private List<String> getRarityList() {
        ConfigurationSection raritySection = config.options().getConfigurationSection("thresholds");
        if (raritySection != null) {
            return new ArrayList<>(raritySection.getKeys(false));
        }
        return new ArrayList<>();
    }
}
