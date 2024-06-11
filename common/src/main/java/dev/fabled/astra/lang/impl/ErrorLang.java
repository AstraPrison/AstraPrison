package dev.fabled.astra.lang.impl;

import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.annotations.LangKey;
import dev.fabled.astra.lang.interfaces.LangKeys;
import org.bukkit.entity.Player;

public class ErrorLang implements LangKeys {

    @LangKey
    public static final String
            NO_PERMISSION = "errors.no-permission",
            SELECT_PLAYER = "errors.select-player",
            INVALID_PLAYER = "errors.invalid-player";

    public static final String INVALID_ENCHANTMENT = "Enchantment '{ENCHANTMENT}' is not valid.";
    public static final String MISSING_ENCHANT_ARGS = "Missing arguments for enchant command.";
    public static final String GIVE_COMMAND_MISSING_ARGUMENT = "Missing arguments for omnitool command.";

    // Method to send the message with placeholder replacement
    public static void sendInvalidPlayerMessage(Player player, String playerName) {
        LocaleManager.send(player, INVALID_PLAYER.replace("{PLAYER}", playerName));
    }

    public static void sendInvalidEnchantmentMessage(Player player, String enchantment) {
        LocaleManager.send(player, INVALID_ENCHANTMENT.replace("{ENCHANTMENT}", enchantment));
    }

    public static void sendMissingEnchantArgsMessage(Player player) {
        LocaleManager.send(player, MISSING_ENCHANT_ARGS);
    }
}
