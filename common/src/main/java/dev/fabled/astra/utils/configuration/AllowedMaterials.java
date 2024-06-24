package dev.fabled.astra.utils.configuration;

import com.google.gson.*;
import org.bukkit.Material;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllowedMaterials {
    private final List<Material> materials;

    public AllowedMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public static AllowedMaterials loadFromJson(String filePath) {
        List<Material> materials = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            createDefaultJsonFile(filePath);
        }

        try (FileReader reader = new FileReader(filePath)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                String materialName = element.getAsString();
                Material material = Material.getMaterial(materialName);
                if (material != null) {
                    materials.add(material);
                } else {
                    System.err.println("Unknown material: " + materialName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new AllowedMaterials(materials);
    }

    public static void createDefaultJsonFile(String filePath) {
        List<String> defaultMaterials = getDefaultMaterialList();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(defaultMaterials);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getDefaultMaterialList() {
        List<String> defaultMaterials = new ArrayList<>();
        defaultMaterials.add("STONE");
        defaultMaterials.add("COBBLESTONE");
        defaultMaterials.add("DIRT");
        defaultMaterials.add(Material.WHITE_TERRACOTTA.name());
        return defaultMaterials;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public boolean isAllowed(Material material) {
        return materials.contains(material);
    }
}
