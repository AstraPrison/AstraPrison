package dev.fabled.astra.sell;

import dev.fabled.astra.utils.configuration.JsonConfig;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SellPriceManager {

    private static SellPriceManager instance;

    public static SellPriceManager getInstance() {
        if (instance == null) {
            instance = new SellPriceManager();
        }

        return instance;
    }

    private final @NotNull Map<Material, Double> prices;
    private final @NotNull JsonConfig sellPricesJson;

    private SellPriceManager() {
        prices = new HashMap<>();
        sellPricesJson = new JsonConfig("modules/sell/sell-prices.json");
        onLoad();
    }

    private void onLoad() {
        final Set<String> materials = sellPricesJson.options().getSet("materials");

        materials.forEach(string -> {
            final Material material;
            try { material = Material.valueOf(string); }
            catch (IllegalArgumentException e) {
                return;
            }

            double price = sellPricesJson.options().getDouble("materials." + string, 0.0d);
            if (price <= 0.0d) {
                return;
            }

            prices.put(material, price);
        });
    }

    public void onDisable() {
        sellPricesJson.options().clear();

        prices.forEach((material, price) -> {
            sellPricesJson.options().set("materials." + material.name(), price);
        });

        sellPricesJson.saveChanges(true);
    }

    public void setPrice(final @NotNull Material material, final double price) {
        if (material.isAir() || !material.isBlock()) {
            return;
        }

        if (price <= 0.0d) {
            prices.remove(material);
            return;
        }

        prices.put(material, price);
    }

    public void removePrice(final @NotNull Material material) {
        prices.remove(material);
    }

    public double getPrice(final @NotNull Material material) {
        return prices.getOrDefault(material, 0.0d);
    }

}
