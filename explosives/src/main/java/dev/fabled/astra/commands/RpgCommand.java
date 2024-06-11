package dev.fabled.astra.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.fabled.astra.Astra;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RpgCommand extends BrigadierCommand {

    private static final File configFile = new File("plugins/Astra/explosives/rpgconfig.yml");
    private static final FileConfiguration config;

    static {
        config = YamlConfiguration.loadConfiguration(configFile);
        config.options().copyDefaults(true);

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RpgCommand() {
        super(
                "rpg",
                new String[]{"explosive", "bombs", "drills"},
                "astra.admin.rpg",
                "The admin command for bomb and drill management!",
                "/mineadmin help"
        );
    }

    @Override
    public CommandNode<CommandSourceStack> buildCommandNode() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal(name)
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("item", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    for (String rarity : getRarityList()) {
                                        builder.suggest(rarity);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> givePumpkinLauncher(context)
                                )))
                .build();
    }

    private int givePumpkinLauncher(CommandContext<CommandSourceStack> context) {
        ServerPlayer serverPlayer = null;
        try {
            serverPlayer = EntityArgument.getPlayer(context, "player");
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        Player targetPlayer = Bukkit.getPlayer(serverPlayer.getUUID());

        String rarity = StringArgumentType.getString(context, "item").toLowerCase();
        if (config.contains("thresholds." + rarity)) {
            ItemStack pumpkinLauncher = createPumpkinLauncher(rarity);
            targetPlayer.getInventory().addItem(pumpkinLauncher);
            targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("givemessage") + pumpkinLauncher.getItemMeta().getDisplayName()));
        } else {
            targetPlayer.sendMessage("Invalid rarity. Please choose from: " + String.join(", ", getRarityList()));
        }

        return 1;
    }

    private ItemStack createPumpkinLauncher(String rarity) {
        ItemStack pumpkinLauncher = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta meta = pumpkinLauncher.getItemMeta();

        int uses;

        ConfigurationSection raritySection = config.getConfigurationSection("thresholds." + rarity);
        if (raritySection != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', raritySection.getString("displayName")));
            uses = config.getInt("thresholds." + rarity + ".uses");
            int customModelData = raritySection.getInt("customModelData");
            meta.getPersistentDataContainer().set(new NamespacedKey(Astra.getPlugin(), "customModelData"), PersistentDataType.INTEGER, customModelData);

            List<String> lore = new ArrayList<>();
            List<String> configLore = config.getStringList("lore");
            for (String line : configLore) {
                line = line.replace("{uses}", String.valueOf(uses));
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            meta.setLore(lore);

            pumpkinLauncher.setItemMeta(meta);

            return pumpkinLauncher;
        } else {
            throw new IllegalArgumentException("Invalid rarity.");
        }
    }

    private List<String> getRarityList() {
        ConfigurationSection raritySection = config.getConfigurationSection("thresholds");
        if (raritySection != null) {
            return new ArrayList<>(raritySection.getKeys(false));
        }
        return new ArrayList<>();
    }
}
