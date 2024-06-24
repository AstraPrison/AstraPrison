package dev.fabled.astra.utils;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataWriter {

    public static final String FILE = "plugins/Astra/data/playerdata.json";

    public static boolean doesPlayerExist(@NotNull final String uuid) {
        try {
            File file = new File(FILE);
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();

                if (jsonObject.has("players")) {
                    JsonArray playersArray = jsonObject.getAsJsonArray("players");

                    for (JsonElement element : playersArray) {
                        JsonObject player = element.getAsJsonObject();
                        String playerUUID = player.get("uuid").getAsString();

                        if (playerUUID.equals(uuid)) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void writePlayerToFile(@NotNull final String name, @NotNull final UUID uuid, int tokens, double money,
                                         int omniToolLevel1, int omniToolLevel2, int omniToolLevel3, int omniToolLevel4, int brokenBlocks) {
        try {
            JsonObject jsonObject;
            File file = new File(FILE);
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();
            } else {
                jsonObject = new JsonObject();
            }

            JsonArray playersArray;
            if (jsonObject.has("players")) {
                playersArray = jsonObject.getAsJsonArray("players");
            } else {
                playersArray = new JsonArray();
                jsonObject.add("players", playersArray);
            }

            JsonObject newPlayer = new JsonObject();
            newPlayer.addProperty("name", name);
            newPlayer.addProperty("uuid", uuid.toString());
            newPlayer.addProperty("tokens", tokens);
            newPlayer.addProperty("money", money);
            newPlayer.addProperty("gems", 0);
            newPlayer.addProperty("blocks", 0);

            JsonObject omniToolLevels = new JsonObject();
            omniToolLevels.addProperty("tool1", omniToolLevel1);
            omniToolLevels.addProperty("tool2", omniToolLevel2);
            omniToolLevels.addProperty("tool3", omniToolLevel3);
            omniToolLevels.addProperty("tool4", omniToolLevel4);
            newPlayer.add("omnitoolLevels", omniToolLevels);

            newPlayer.addProperty("brokenBlocks", brokenBlocks);

            playersArray.add(newPlayer);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonOutput = gson.toJson(jsonObject);

            FileWriter writer = new FileWriter(file);
            writer.write(jsonOutput);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deletePlayerFromFile(@NotNull final String uuid) {
        try {
            File file = new File(FILE);
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();

                if (jsonObject.has("players")) {
                    JsonArray playersArray = jsonObject.getAsJsonArray("players");

                    JsonArray updatedPlayersArray = new JsonArray();

                    for (JsonElement element : playersArray) {
                        JsonObject player = element.getAsJsonObject();
                        String playerUUID = player.get("uuid").getAsString();

                        if (!playerUUID.equals(uuid)) {
                            updatedPlayersArray.add(player);
                        }
                    }

                    jsonObject.add("players", updatedPlayersArray);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    FileWriter writer = new FileWriter(file);
                    writer.write(gson.toJson(jsonObject));
                    writer.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void incrementBrokenBlocks(@NotNull final String uuid) {
        try {
            File file = new File(FILE);
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();

                if (jsonObject.has("players")) {
                    JsonArray playersArray = jsonObject.getAsJsonArray("players");

                    for (JsonElement element : playersArray) {
                        JsonObject player = element.getAsJsonObject();
                        String playerUUID = player.get("uuid").getAsString();

                        if (playerUUID.equals(uuid)) {
                            int currentBrokenBlocks = player.get("brokenBlocks").getAsInt();
                            player.addProperty("brokenBlocks", currentBrokenBlocks + 1);

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            FileWriter writer = new FileWriter(file);
                            writer.write(gson.toJson(jsonObject));
                            writer.close();
                            return;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
