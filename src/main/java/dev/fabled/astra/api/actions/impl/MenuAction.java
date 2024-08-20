package dev.fabled.astra.api.actions.impl;

import dev.fabled.astra.AstraPlugin;
import dev.fabled.astra.api.actions.ActionArgument;
import dev.fabled.astra.api.actions.AstraAction;
import dev.fabled.astra.menus.AstraMenu;
import dev.fabled.astra.menus.MenuManager;
import dev.fabled.astra.utils.dependencies.PapiUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuAction extends AstraAction {

    public static final @NotNull String ID;

    static {
        ID = "menu";
    }

    private final MenuManager menuManager;

    public MenuAction() {
        super(ID);
        menuManager = AstraPlugin.getInstance().getMenuManager();
    }

    @Override
    public void run(final @NotNull Player player, final @NotNull ActionArgument argument) {
        final String[] args = argument.getArguments();
        if (args.length < 1) {
            return;
        }

        final String menuId = PapiUtils.parse(player, args[0]);
        final AstraMenu menu = menuManager.getMenu(menuId);
        if (menu == null) {
            return;
        }

        player.closeInventory();
        player.openInventory(menu.getInventory(player));
    }
}
