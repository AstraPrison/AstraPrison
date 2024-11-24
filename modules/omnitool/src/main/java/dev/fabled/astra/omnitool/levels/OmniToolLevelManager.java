package dev.fabled.astra.omnitool.levels;

import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.utils.configuration.YamlConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.TreeMap;

public class OmniToolLevelManager {

    private final @NotNull TreeMap<Integer, OmniToolLevel> levels;

    public OmniToolLevelManager() {
        levels = new TreeMap<>();
        onReload();
    }

    public void onReload() {
        levels.clear();
        final YamlConfig config = OmniToolModule.instance.getLevelsYml();

        levels.put(0, new OmniToolLevel(0));
        final ConfigurationSection section = config.options().getConfigurationSection("levels");

        if (section == null) {
            return;
        }

        section.getKeys(false).forEach(key -> {
            final int level;
            try { level = Integer.parseInt(key); }
            catch (NumberFormatException e) {
                return;
            }

            levels.putIfAbsent(level, new OmniToolLevel(level));
        });
    }

    public @NotNull OmniToolLevel getLevel(int level) {
        level = Math.max(0, level);
        return levels.get(levels.floorKey(level));
    }

}
