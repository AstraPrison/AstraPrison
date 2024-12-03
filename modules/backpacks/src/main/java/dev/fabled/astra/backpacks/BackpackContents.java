package dev.fabled.astra.backpacks;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BackpackContents {

    private int totalItems;
    private final Map<Material, BackpackContent> materials;

    public BackpackContents() {
        totalItems = 0;
        materials = new HashMap<>();
    }

    public void addMaterial(final @NotNull Material material, final int amount) {
        if (!materials.containsKey(material)) {
            materials.put(material, new BackpackContent(amount));
            totalItems += amount;
            return;
        }

        materials.get(material).add(amount);
        totalItems += amount;
    }

    public void removeMaterial(final @NotNull Material material, final int amount) {
        if (!materials.containsKey(material)) {
            return;
        }

        final BackpackContent content = materials.get(material);
        int currentAmount = content.get();
        int newAmount = content.remove(amount);

        if (newAmount <= 0) {
            totalItems -= currentAmount;
            materials.remove(material);
            return;
        }

        totalItems -= amount;
    }

    public final @NotNull Map<Material, Integer> getMaterials() {
        return materials.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().get()
        ));
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void removeAll() {
        materials.clear();
        totalItems = 0;
    }

}
