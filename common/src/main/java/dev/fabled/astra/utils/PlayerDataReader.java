package dev.fabled.astra.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PlayerDataReader {

    public static final String FILE = "plugins/Astra/data/playerdata.json";

    public static JsonObject getPlayerData(@NotNull final String uuid) {
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
                            return player;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPlayerName(@NotNull final String uuid) {
        JsonObject playerData = getPlayerData(uuid);
        if (playerData != null) {
            return playerData.get("name").getAsString();
        }
        return null;
    }

    public static int getPlayerTokens(@NotNull final String uuid) {
        JsonObject playerData = getPlayerData(uuid);
        if (playerData != null) {
            return playerData.get("tokens").getAsInt();
        }
        return 0;
    }

    public static double getPlayerMoney(@NotNull final String uuid) {
        JsonObject playerData = getPlayerData(uuid);
        if (playerData != null) {
            return playerData.get("money").getAsDouble();
        }
        return 0.0;
    }

    public static int getPlayerOmniToolLevel(@NotNull final String uuid, int toolIndex) {
        JsonObject playerData = getPlayerData(uuid);
        if (playerData != null && playerData.has("omnitoolLevels")) {
            JsonObject omniToolLevels = playerData.getAsJsonObject("omnitoolLevels");
            switch (toolIndex) {
                case 1:
                    return omniToolLevels.get("tool1").getAsInt();
                case 2:
                    return omniToolLevels.get("tool2").getAsInt();
                case 3:
                    return omniToolLevels.get("tool3").getAsInt();
                case 4:
                    return omniToolLevels.get("tool4").getAsInt();
                default:
                    throw new IllegalArgumentException("Invalid tool index: " + toolIndex);
            }
        }
        return 0;
    }

    public static int getPlayerBrokenBlocks(@NotNull final String uuid) {
        JsonObject playerData = getPlayerData(uuid);
        if (playerData != null) {
            return playerData.get("brokenBlocks").getAsInt();
        }
        return 0;
    }
}

