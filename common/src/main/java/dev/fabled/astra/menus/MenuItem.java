package dev.fabled.astra.menus;

import dev.fabled.astra.Astra;
import dev.fabled.astra.menus.actions.ItemClickActions;
import dev.fabled.astra.menus.requirements.ItemClickRequirements;
import dev.fabled.astra.utils.SlotUtils;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.items.ItemBuilder;
import dev.fabled.astra.utils.items.pdc.StringPDC;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MenuItem extends ItemBuilder {

    private final @NotNull String id;
    private @Nullable Set<Integer> slots;
    private @Nullable Map<MenuClickType, ItemClickRequirements> itemClickRequirements;
    private @Nullable Map<MenuClickType, ItemClickActions> itemClickActions;

    public MenuItem(final @NotNull String id, final @NotNull String material) {
        super(material);
        this.id = id;
    }

    public MenuItem(final @NotNull YamlConfig config, @NotNull String key, final @NotNull String id) {
        super(config, key);
        this.id = id;
        if (!key.endsWith(".")) {
            key += ".";
        }

        slots = SlotUtils.fromConfig(config, key + "slots");

        itemClickRequirements = new HashMap<>();
        itemClickActions = new HashMap<>();
        for (final MenuClickType clickType : MenuClickType.values()) {
            if (config.options().contains(key + "click-requirements." + clickType.getKey())) {
                try {
                    final ItemClickRequirements clickRequirements = new ItemClickRequirements(
                            config, key + "click-requirements." + clickType.getKey()
                    );

                    itemClickRequirements.put(clickType, clickRequirements);
                }
                catch (NullPointerException ignored) {}
            }

            if (!config.options().contains(key + "click-actions." + clickType.getKey())) {
                continue;
            }

            final ItemClickActions actions;
            try { actions = new ItemClickActions(config, key + "click-actions." + clickType.getKey()); }
            catch (NullPointerException e) {
                continue;
            }

            itemClickActions.put(clickType, actions);
        }

        addPDC(new StringPDC(Astra.getAstraMenuItemKey(), id));
    }

    public final @NotNull String getId() {
        return id;
    }

    public @NotNull MenuItem setSlots(final @Nullable Set<Integer> slots) {
        if (slots == null) {
            this.slots = null;
            return this;
        }

        this.slots = new HashSet<>(slots);
        return this;
    }

    public @NotNull MenuItem setSlots(final @NotNull Integer... slots) {
        return setSlots(Set.of(slots));
    }

    public @NotNull MenuItem addSlots(final @NotNull Set<Integer> slots) {
        if (this.slots == null) {
            this.slots = new HashSet<>(slots);
            return this;
        }

        this.slots.addAll(slots);
        return this;
    }

    public @NotNull MenuItem addSlots(final @NotNull Integer... slots) {
        return addSlots(Set.of(slots));
    }

    public @NotNull Set<Integer> getSlots() {
        if (slots == null) {
            return new HashSet<>();
        }

        return new HashSet<>(slots);
    }

    public @NotNull Map<MenuClickType, ItemClickActions> getItemClickActions() {
        if (itemClickActions == null) {
            return new HashMap<>();
        }

        return new HashMap<>(itemClickActions);
    }

    public @Nullable ItemClickActions getItemClickActions(final @NotNull MenuClickType clickType) {
        if (itemClickActions == null) {
            return null;
        }

        return itemClickActions.getOrDefault(clickType, null);
    }

    public @NotNull MenuItem addClickActions(final @NotNull MenuClickType clickType, final @NotNull List<String> actions) {
        if (itemClickActions == null) {
            itemClickActions = new HashMap<>();
        }

        itemClickActions.putIfAbsent(clickType, new ItemClickActions());
        itemClickActions.get(clickType).addClickActions(actions);
        return this;
    }

    public @NotNull MenuItem addClickActions(final @NotNull MenuClickType clickType, final @NotNull String... actions) {
        return addClickActions(clickType, List.of(actions));
    }

    public @NotNull Map<MenuClickType, ItemClickRequirements> getItemClickRequirements() {
        if (itemClickRequirements == null) {
            return new HashMap<>();
        }

        return new HashMap<>(itemClickRequirements);
    }

    public @Nullable ItemClickRequirements getItemClickRequirements(final @NotNull MenuClickType clickType) {
        if (itemClickRequirements == null) {
            return null;
        }

        return itemClickRequirements.getOrDefault(clickType, null);
    }

}
