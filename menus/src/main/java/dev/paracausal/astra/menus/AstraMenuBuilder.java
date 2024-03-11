package dev.paracausal.astra.menus;

import dev.paracausal.astra.menus.items.MenuItem;
import dev.paracausal.astra.utilities.configuration.YamlConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AstraMenuBuilder {

    public static final String DEFAULT_TITLE;
    public static final int DEFAULT_SIZE;

    static {
        DEFAULT_TITLE = "Astra Menu";
        DEFAULT_SIZE = 54;
    }

    private @NotNull String title;
    private int size;
    private @NotNull Map<String, MenuItem> items;

    public AstraMenuBuilder(@NotNull final YamlConfig config) {
        title = config.options().getString("title", DEFAULT_TITLE);
        setSize(config.options().getInt("size", DEFAULT_SIZE));
        items = new HashMap<>();

        final ConfigurationSection contents = config.options().getConfigurationSection("contents");
        if (contents == null) {
            return;
        }

        contents.getKeys(false).forEach(id ->
            items.put(id, new MenuItem(config, "contents." + id).setID(id))
        );
    }

    public AstraMenuBuilder() {
        title = DEFAULT_TITLE;
        size = DEFAULT_SIZE;
        items = new HashMap<>();
    }



    public AstraMenuBuilder setTitle(@NotNull final String title) {
        this.title = title;
        return this;
    }



    public AstraMenuBuilder setSize(int size) {
        size = Math.max(1, size);
        size = Math.min(54, size);

        if (size % 9 != 0) {
            if (size <= 6) {
                this.size = size * 9;
                return this;
            }

            size = 9;
        }

        this.size = size;
        return this;
    }



    public AstraMenuBuilder setItems(@Nullable final Map<String, MenuItem> items) {
        if (items == null) {
            this.items = new HashMap<>();
        }

        else {
            this.items = new HashMap<>(items);
        }

        return this;
    }

    public AstraMenuBuilder addItems(@NotNull final Map<String, MenuItem> items) {
        this.items.putAll(items);
        return this;
    }

    public AstraMenuBuilder addItems(@NotNull final MenuItem... item) {
        for (final MenuItem i : item) {
            this.items.put(i.getID(), i);
        }

        return this;
    }



    public AstraMenu build() {
        return new AstraMenu(title, size, items);
    }

}
