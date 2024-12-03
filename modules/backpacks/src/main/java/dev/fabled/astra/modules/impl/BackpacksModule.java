package dev.fabled.astra.modules.impl;

import dev.fabled.astra.Astra;
import dev.fabled.astra.backpacks.BackpackManager;
import dev.fabled.astra.modules.AstraModule;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public final class BackpacksModule extends AstraModule {

    public static final @NotNull String ID;

    static {
        ID = "backpacks";
    }

    private static BackpacksModule instance;

    public static @NotNull BackpacksModule getInstance() {
        if (instance == null) {
            instance = new BackpacksModule();
        }

        return instance;
    }

    private YamlConfig backpacksYml;
    private NamespacedKey backpackKey;
    private NamespacedKey backpackContentsKey;
    private BackpackManager backpackManager;

    private BackpacksModule() {
        super(ID);
    }

    @Override
    public void onLoad() {
        backpacksYml = new YamlConfig("modules/backpacks/backpacks.yml");
        backpackKey = new NamespacedKey(Astra.getPlugin(), "astra-backpack");
        backpackContentsKey = new NamespacedKey(Astra.getPlugin(), "astra-backpack-contents");
        backpackManager = new BackpackManager();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {
        backpacksYml.save();
        backpacksYml.reload();
    }

    public YamlConfig getBackpacksYml() {
        return backpacksYml;
    }

    public BackpackManager getBackpackManager() {
        return backpackManager;
    }

    public NamespacedKey getBackpackKey() {
        return backpackKey;
    }

    public NamespacedKey getBackpackContentsKey() {
        return backpackContentsKey;
    }

}
