package com.errorC003C004.simply_optimized.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManagerClient {

    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("error_SO.json");

    public static boolean isClientWhitelisted = false;
    public static boolean showImage = false;

    public static void addClientWhitelist() {
        isClientWhitelisted = true;
        saveConfig();
    }

    public static void removeClientWhitelist() {
        isClientWhitelisted = false;
        saveConfig();
    }

    public static void showImageTrue() {
        showImage = true;
        saveConfig();
    }

    public static void showImageFalse() {
        showImage = false;
        saveConfig();
    }

    public static void loadConfig() {
        try {

            if (Files.notExists(CONFIG_PATH)) {
                createDefaultConfig();
            }

            JsonObject json;

            try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
                json = GSON.fromJson(reader, JsonObject.class);
            }

            if (json == null) {
                createDefaultConfig();
                return;
            }

            if (json.has("is_client_whitelisted")) {
                isClientWhitelisted = json.get("is_client_whitelisted").getAsBoolean();
            }
            if (json.has("show_image")) {
                showImage = json.get("show_image").getAsBoolean();
            }

            LOGGER.info("[SimplyOptimised] Player is "
                    + (isClientWhitelisted ? "" : "NOT ")
                    + "whitelisted." + "Show Image: " + showImage);

        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] Config corrupted. Recreating.", e);
            createDefaultConfig();
        }
    }

    public static void saveConfig() {
        try {
            JsonObject json = new JsonObject();

            json.addProperty("is_client_whitelisted", isClientWhitelisted);
            json.addProperty("show_image", showImage);

            Files.createDirectories(CONFIG_PATH.getParent());

            try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(json, writer);
            }

        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] Error saving config", e);
        }
    }

    public static void createDefaultConfig() {
        try {
            JsonObject json = new JsonObject();

            json.addProperty("is_client_whitelisted", false);

            Files.createDirectories(CONFIG_PATH.getParent());

            try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(json, writer);
            }

            isClientWhitelisted = false;

            LOGGER.info("[SimplyOptimised] Default Client Config Created.");

        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] Error creating default config", e);
        }
    }
}