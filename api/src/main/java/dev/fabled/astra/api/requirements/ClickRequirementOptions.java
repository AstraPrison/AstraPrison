package dev.fabled.astra.api.requirements;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Used to get specific options from the config for a specific requirement
 */
public interface ClickRequirementOptions {

    /**
     * Check if the requirement options contains a certain key
     * @param key {@link String} key to check
     * @return {@link Boolean} <code>true</code> if it is in the config, <code>false</code> if not
     */
    @NotNull Boolean contains(final @NotNull String key);



    /**
     * Get an {@link Object} from the requirement in the config
     * @param key {@link String} key for this object
     * @param def The default {@link Object} to return if the config does not contain the key
     * @return {@link Object} or <code>def</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getObject(String)
     */
    @Contract("_, !null -> !null")
    @Nullable Object getObject(final @NotNull String key, final @Nullable Object def);

    /**
     * Get an {@link Object} from the requirement in the config
     * @param key {@link String} key for this object
     * @return {@link Object} or <code>null</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getObject(String, Object)
     */
    default @Nullable Object getObject(final @NotNull String key) {
        return getObject(key, null);
    }



    /**
     * Get a {@link String} from the requirement in the config
     * @param key {@link String} key for this string
     * @param def The default {@link String} to return if the config does not contain the key
     * @return {@link String} or <code>def</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getString(String)
     */
    @Contract("_, !null -> !null")
    @Nullable String getString(final @NotNull String key, final @Nullable String def);

    /**
     * Get a {@link String} from the requirement in the config
     * @param key {@link String} key for this string
     * @return {@link String} or <code>null</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getString(String, String)
     */
    default @Nullable String getString(final @NotNull String key) {
        return getString(key, null);
    }



    /**
     * Get a {@link Boolean} from the requirement in the config
     * @param key {@link String} key for this boolean
     * @param def The default {@link Boolean} to return if the config does not contain the key
     * @return {@link Boolean} or <code>def</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getBoolean(String)
     */
    @NotNull Boolean getBoolean(final @NotNull String key, final @NotNull Boolean def);

    /**
     * Get a {@link Boolean} from the requirement in the config
     * @param key {@link String} key for this boolean
     * @return {@link Boolean} or <code>false</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getBoolean(String, Boolean)
     */
    default @NotNull Boolean getBoolean(final @NotNull String key) {
        return getBoolean(key, false);
    }



    /**
     * Get an {@link Integer} from the requirement in the config
     * @param key {@link String} key for this integer
     * @param def The default {@link Integer} to return if the config does not contain the key
     * @return {@link Integer} or <code>def</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getInt(String)
     */
    @NotNull Integer getInt(final @NotNull String key, final @NotNull Integer def);

    /**
     * Get an {@link Integer} from the requirement in the config
     * @param key {@link String} key for this integer
     * @return {@link Integer} or <code>0</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getInt(String, Integer)
     */
    default @NotNull Integer getInt(final @NotNull String key) {
        return getInt(key, 0);
    }



    /**
     * Get a {@link Double} from the requirement in the config
     * @param key {@link String} key for this double
     * @param def The default {@link Double} to return if the config does not contain the key
     * @return {@link Double} or <code>def</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getDouble(String)
     */
    @NotNull Double getDouble(final @NotNull String key, final @NotNull Double def);

    /**
     * Get a {@link Double} from the requirement in the config
     * @param key {@link String} key for this double
     * @return {@link Double} or <code>0</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getDouble(String, Double)
     */
    default Double getDouble(final @NotNull String key) {
        return getDouble(key, 0.0d);
    }



    /**
     * Get a {@link List}<{@link String}> from the requirement in the config
     * @param key {@link String} key for this string list
     * @param def The default {@link List}<{@link String}> to return if the config does not contain the key
     * @return {@link List}<{@link String}> or <code>def</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getStringList(String)
     */
    @Contract("_, !null -> !null")
    @Nullable List<String> getStringList(final @NotNull String key, final @Nullable List<String> def);

    /**
     * Get a {@link List}<{@link String}> from the requirement in the config
     * @param key {@link String} key for this string list
     * @return {@link List}<{@link String}> or <code>{@link Collections#emptyList}</code> if the <code>key</code> is not in the config
     * @see ClickRequirementOptions#getStringList(String, List)
     */
    default @NotNull List<String> getStringList(final @NotNull String key) {
        return getStringList(key, Collections.emptyList());
    }

}
