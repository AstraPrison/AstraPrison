package dev.fabled.astra.lang.impl;

import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.annotations.LangKey;
import dev.fabled.astra.lang.interfaces.LangKeys;
import org.bukkit.entity.Player;

public class OmniToolLang implements LangKeys {

    @LangKey
    public static final String
            ADMIN_HELP = "omni-tool.admin.help",
            ADMIN_GIVE = "omni-tool.admin.give",
            RECEIVED_OMNITOOL = "omni-tool.received";

    public static final String ADMIN_ENCHANT = "Enchanted {PLAYER}'s tool with {ENCHANTMENT} level {LEVEL}.";

    public static void sendAdminGiveMessage(Player player, String targetName) {
        LocaleManager.send(player, ADMIN_GIVE.replace("{PLAYER}", targetName));
    }

    public static void sendAdminEnchantMessage(Player player, String targetName, String enchantment, int level) {
        LocaleManager.send(player, ADMIN_ENCHANT
                .replace("{PLAYER}", targetName)
                .replace("{ENCHANTMENT}", enchantment)
                .replace("{LEVEL}", String.valueOf(level)));
    }
}
