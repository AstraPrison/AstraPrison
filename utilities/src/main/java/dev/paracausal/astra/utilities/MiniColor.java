package dev.paracausal.astra.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum MiniColor {

    /**
     * For use in the in-game chat
     */
    ALL(MiniMessage.miniMessage()),

    /**
     * For use in an inventory title, item display name or lore
     */
    TEXT(
            StandardTags.color(),
            StandardTags.gradient(),
            StandardTags.rainbow(),
            StandardTags.decorations(),
            StandardTags.translatable(),
            StandardTags.font()
    ),

    /**
     * For use in console
     */
    COLOR(
            StandardTags.color(),
            StandardTags.gradient(),
            StandardTags.rainbow()
    );

    private final MiniMessage miniMessage;

    MiniColor(@NotNull final MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    MiniColor(@NotNull final TagResolver... tags) {
        miniMessage = MiniMessage.builder().tags(
                TagResolver.builder().resolvers(tags).build()
        ).build();
    }

    public @NotNull Component deserialize(@NotNull final String input) {
        return miniMessage.deserialize(input).decoration(TextDecoration.ITALIC, false);
    }

    public @NotNull List<Component> deserialize(@NotNull final List<String> input) {
        final List<Component> deserialized = new ArrayList<>();
        input.forEach(string -> deserialized.add(deserialize(string)));
        return deserialized;
    }

    public @NotNull String serialize(@NotNull final Component component) {
        return miniMessage.serialize(component);
    }

    public @NotNull List<String> serialize(@NotNull final List<Component> input) {
        final List<String> serialized = new ArrayList<>();
        input.forEach(component -> serialized.add(serialize(component)));
        return serialized;
    }

    public @NotNull String strip(@NotNull final String string) {
        return miniMessage.stripTags(string);
    }

    public @NotNull List<String> strip(@NotNull final List<String> input) {
        final List<String> stripped = new ArrayList<>();
        input.forEach(string -> stripped.add(strip(string)));
        return stripped;
    }

}
