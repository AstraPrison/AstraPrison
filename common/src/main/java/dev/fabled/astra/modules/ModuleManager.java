package dev.fabled.astra.modules;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {

    private final Map<String, AstraModule> modules;

    public ModuleManager() {
        modules = new HashMap<>();
    }

    void register(@NotNull final AstraModule module) {
        modules.put(module.id, module);
    }

    public @Nullable AstraModule getModule(@NotNull final String id) {
        return modules.getOrDefault(id, null);
    }

    public void onLoad() {
        modules.values().forEach(AstraModule::onLoad);
    }

    public void onEnable() {
        modules.values().forEach(AstraModule::onEnable);
    }

    public void onReload() {
        modules.values().forEach(AstraModule::onReload);
    }

    public void onDisable() {
        modules.values().forEach(AstraModule::onDisable);
    }

}
