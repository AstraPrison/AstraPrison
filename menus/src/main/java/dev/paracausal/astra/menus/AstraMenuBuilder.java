package dev.paracausal.astra.menus;

import dev.paracausal.astra.utilities.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

public class AstraMenuBuilder {

    public static final String DEFAULT_TITLE;
    public static final int DEFAULT_SIZE;

    static {
        DEFAULT_TITLE = "Astra Menu";
        DEFAULT_SIZE = 54;
    }

    public static AstraMenuBuilder fromConfig(@NotNull final YamlConfig config) {
        return new AstraMenuBuilder()
                .setTitle(config.options().getString("title", DEFAULT_TITLE))
                .setSize(config.options().getInt("size", DEFAULT_SIZE));
    }

    private String title;
    private int size;

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

    public AstraMenu build() {
        return new AstraMenu(title, size);
    }

}
