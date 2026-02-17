package com.errorC003C004.simply_optimized;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigManager {
    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("Simply_Optimised.json");

    private static final Set<UUID> ALLOWED_UUIDS = new HashSet<>();
    private static final Set<UUID> DETECTED_CLIENTS = new HashSet<>();

    public static void load() {
        try {

            if (Files.notExists(CONFIG_PATH)) {
                createDefaultConfig();
            }

            JsonObject json = GSON.fromJson(new FileReader(CONFIG_PATH.toFile()), JsonObject.class);

            ALLOWED_UUIDS.clear();
            DETECTED_CLIENTS.clear();

            JsonArray array = json.getAsJsonArray("allowed_uuids");
            for (int i = 0; i < array.size(); i++) {
                ALLOWED_UUIDS.add(UUID.fromString(array.get(i).getAsString()));
            }
            JsonArray array2 = json.getAsJsonArray("Detected Clients");
            for (int i = 0; i < array2.size(); i++) {
                DETECTED_CLIENTS.add(UUID.fromString(array2.get(i).getAsString()));
            }

            //LOGGER.info("[SimplyOptimised] Loaded " + ALLOWED_UUIDS.size() + " UUIDs");
            //LOGGER.info("[SimplyOptimised] Loaded " + DETECTED_CLIENTS.size() + " Clients");

        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] An error occurred during ConfigManager.load", e);
        }
    }
    public static void save() {
        try {
            JsonObject json = new JsonObject();
            JsonArray array = new JsonArray();

            for (UUID uuid : ALLOWED_UUIDS) {
                array.add(uuid.toString());
            }

            json.add("allowed_uuids", array);

            Files.createDirectories(CONFIG_PATH.getParent());

            try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(json, writer);
            }

            LOGGER.info("[SimplyOptimised] Config saved.");

        } catch (Exception e) {
            System.err.println("[SimplyOptimised] Failed to save config!");
            LOGGER.error("[SimplyOptimised] An error occurred during ConfigManager.save", e);
        }
    }

    private static void createDefaultConfig() {
        try {
            JsonObject json = new JsonObject();
            JsonArray array = new JsonArray();

            //array.add("f093b6f8-b062-4764-abb0-a3d6d7cd727a"); // Creator
            //array.add("baf382d2-8686-4b4c-b4b8-fe8ef9ebfca6"); // Dartmonkey

            json.add("allowed_uuids", array);

            try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(json, writer);
            }
            LOGGER.info("[SimplyOptimised] Created default Simply_Optimised.json");
            LOGGER.info("[SimplyOptimised] Created default Simply_Optimised.json");

        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] An error occurred during ConfigManager.createDefaultConfig", e);
        }
    }

    public static boolean isAllowed(UUID uuid) {
        return ALLOWED_UUIDS.contains(uuid) || DETECTED_CLIENTS.contains(uuid);
    }

    public static Set<UUID> getAllowedUuids() {
        return ALLOWED_UUIDS;
    }
}
