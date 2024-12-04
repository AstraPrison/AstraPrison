package dev.fabled.astra.permissions;

import dev.fabled.astra.locale.LocaleManager;
import dev.fabled.astra.locale.impl.ErrorMessageKeys;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public enum AstraPermission {

    ASTRA_STAR(
            "astra.*",
            "Gives all Astra permissions",
            "astra.admin.*", "astra.mines.*", "astra.omnitool.*", "astra.sellprice.*"
    ),

    ADMIN_STAR(
            "astra.admin.*",
            "Gives all Astra admin permissions",
            "astra.admin.help", "astra.admin.reload", "astra.admin.menu.other"
    ),

    ADMIN("astra.admin", "Permission for /astra"),
    ADMIN_HELP("astra.admin.help", "Permission for /astra help", "astra.admin"),
    ADMIN_RELOAD("astra.admin.reload", "Permission for /astra reload", "astra.admin"),
    ADMIN_MENU("astra.admin.menu", "Permission for /astra menu", "astra.admin"),
    ADMIN_MENU_OTHER(
            "astra.admin.menu.other",
            "Permission for /astra menu <player>",
            "astra.admin.menu"
    ),

    OMNI_TOOL_STAR(
            "astra.omnitool.*",
            "Gives all omnitool permissions",
            "astra.omnitool.give"
    ),
    OMNI_TOOL("astra.omnitool", "Permission for /omnitool"),
    OMNI_TOOL_GIVE("astra.omnitool.give", "Permission for /omnitool give", "astra.omnitool"),

    MINES_STAR(
            "astra.mines.*",
            "Gives all mine permissions!",
            "astra.mines.wand.*"
    ),

    MINE_WAND_STAR(
            "astra.mines.wand.*",
            "Gives all mine wand permissions!",
            "astra.mines.wand.other", "astra.mines.wand.use"
    ),
    MINE_WAND("astra.mines.wand", "Permission for /minewand"),
    MINE_WAND_OTHER("astra.mines.wand.other", "Permission for /minewand <player>", "astra.mines.wand"),
    MINE_WAND_USE("astra.mine.wand.use", "Permission to use the mine wand"),

    SELL_PRICE_STAR(
            "astra.sellprice.*",
            "Gives all sell price permissions!",
            "astra.sellprice.set", "astra.sellprice.check"
    ),
    SELL_PRICE("astra.sellprice", "Permission for /sellprice"),
    SELL_PRICE_SET("astra.sellprice.set", "Permission for /sellprice set", "astra.sellprice"),
    SELL_PRICE_CHECK("astra.sellprice.check", "Permission for /sellprice check", "astra.sellprice");

    private final @NotNull String name;
    private final @NotNull String description;
    private final @NotNull PermissionDefault permissionDefault;
    private final @NotNull List<String> children;

    AstraPermission(
            final @NotNull String name,
            final @NotNull String description,
            final @NotNull PermissionDefault permissionDefault,
            final @NotNull List<String> children
    ) {
        this.name = name;
        this.description = description;
        this.permissionDefault = permissionDefault;
        this.children = children;
    }

    AstraPermission(
            final @NotNull String name,
            final @NotNull String description,
            final @NotNull PermissionDefault permissionDefault,
            final @NotNull String... children
    ) {
        this(name, description, permissionDefault, List.of(children));
    }

    AstraPermission(
            final @NotNull String name,
            final @NotNull String description,
            final @NotNull String... children
    ) {
        this(name, description, PermissionDefault.OP, List.of(children));
    }

    AstraPermission(
            final @NotNull String name,
            final @NotNull String description,
            final @NotNull PermissionDefault permissionDefault
    ) {
        this(name, description, permissionDefault, Collections.emptyList());
    }

    AstraPermission(final @NotNull String name, final @NotNull String description) {
        this(name, description, PermissionDefault.FALSE, Collections.emptyList());
    }

    AstraPermission(final @NotNull String permission) {
        this(permission, "", PermissionDefault.FALSE, Collections.emptyList());
    }

    public boolean hasPermission(final @NotNull Player player, final boolean sendMessage) {
        final boolean hasPermission = player.hasPermission(name);

        if (!hasPermission && sendMessage) {
            LocaleManager.sendMessage(player, ErrorMessageKeys.NO_PERMISSION, "{PERMISSION}", name);
        }

        return hasPermission;
    }

    public boolean hasPermission(final @NotNull Player player) {
        return hasPermission(player, false);
    }

    public @NotNull String get() {
        return name;
    }

    private static boolean loaded;

    static {
        loaded = false;
    }

    public static void onLoad() {
        if (loaded) {
            return;
        }

        loaded = true;
        final PluginManager pluginManager = Bukkit.getPluginManager();
        final Map<String, Boolean> children = new HashMap<>();

        for (final AstraPermission permission : AstraPermission.values()) {
            children.clear();
            permission.children.forEach(child -> children.put(child, true));

            try {
                pluginManager.addPermission(new Permission(
                        permission.name,
                        permission.description,
                        permission.permissionDefault,
                        children
                ));
            }
            catch (IllegalArgumentException e) {
                AstraLog.log(AstraLogLevel.ERROR, permission.name + " is already registered!");
                AstraLog.log(e);
                continue;
            }
        }
    }

}
