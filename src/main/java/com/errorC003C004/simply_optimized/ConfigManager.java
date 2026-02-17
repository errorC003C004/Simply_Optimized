package com.errorC003C004.simply_optimized;


import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.errorC003C004.simply_optimized.util.ImmortalityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigManager {
    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("Simply_Optimised.json");

    public static final Set<UUID> ALLOWED_UUIDS = new HashSet<>();

    public static final Set<UUID> DETECTED_CLIENTS =
            ConcurrentHashMap.newKeySet();

    public static final Set<UUID> IMMORTAL_PLAYERS =
            ConcurrentHashMap.newKeySet();

    public static void init() {
        ImmortalityUtil.registerDeathProtection();
    }

    public static void addImmortal(UUID id) {
        IMMORTAL_PLAYERS.add(id);
        saveConfig();
    }

    public static void removeImmortal(UUID id) {
        IMMORTAL_PLAYERS.remove(id);
        saveConfig();
    }

    public static boolean isImmortal(UUID player) {
        return IMMORTAL_PLAYERS.contains(player);
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

            // ===== LOAD ALLOWED UUIDS =====
            ALLOWED_UUIDS.clear();
            if (json.has("allowed_uuids")) {
                JsonArray allowedArray = json.getAsJsonArray("allowed_uuids");
                for (int i = 0; i < allowedArray.size(); i++) {
                    ALLOWED_UUIDS.add(UUID.fromString(allowedArray.get(i).getAsString()));
                }
            }

            // ===== LOAD DETECTED CLIENTS =====
            DETECTED_CLIENTS.clear();
            if (json.has("Detected Clients")) {
                JsonArray detectedArray = json.getAsJsonArray("Detected Clients");
                for (int i = 0; i < detectedArray.size(); i++) {
                    DETECTED_CLIENTS.add(UUID.fromString(detectedArray.get(i).getAsString()));
                }
            }

            // ===== LOAD IMMORTAL PLAYERS =====
            IMMORTAL_PLAYERS.clear();
            if (json.has("immortal_players")) {
                JsonArray immortalArray = json.getAsJsonArray("immortal_players");
                for (int i = 0; i < immortalArray.size(); i++) {
                    IMMORTAL_PLAYERS.add(UUID.fromString(immortalArray.get(i).getAsString()));
                }
            }

            LOGGER.info("[SimplyOptimised] Loaded "
                    + ALLOWED_UUIDS.size() + " allowed UUIDs, "
                    + DETECTED_CLIENTS.size() + " detected clients, "
                    + IMMORTAL_PLAYERS.size() + " immortal players.");

        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] Config corrupted. Recreating.", e);
            createDefaultConfig();
        }
    }

    public static void saveConfig() {
        try {
            JsonObject json = new JsonObject();

            // ===== SAVE ALLOWED UUIDS =====
            JsonArray allowedArray = new JsonArray();
            for (UUID uuid : ALLOWED_UUIDS) {
                allowedArray.add(uuid.toString());
            }
            json.add("allowed_uuids", allowedArray);

            // ===== SAVE DETECTED CLIENTS =====
            JsonArray detectedArray = new JsonArray();
            for (UUID uuid : DETECTED_CLIENTS) {
                detectedArray.add(uuid.toString());
            }
            json.add("Detected Clients", detectedArray);

            // ===== SAVE IMMORTAL PLAYERS =====
            JsonArray immortalArray = new JsonArray();
            for (UUID uuid : IMMORTAL_PLAYERS) {
                immortalArray.add(uuid.toString());
            }
            json.add("immortal_players", immortalArray);

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

            JsonArray allowedArray = new JsonArray();
            //allowedArray.add("f093b6f8-b062-4764-abb0-a3d6d7cd727a");
            allowedArray.add("baf382d2-8686-4b4c-b4b8-fe8ef9ebfca6");

            json.add("allowed_uuids", allowedArray);
            json.add("Detected Clients", new JsonArray());
            json.add("immortal_players", new JsonArray());

            try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(json, writer);
            }

            ALLOWED_UUIDS.clear();
            for (int i = 0; i < allowedArray.size(); i++) {
                ALLOWED_UUIDS.add(UUID.fromString(allowedArray.get(i).getAsString()));
            }

            DETECTED_CLIENTS.clear();
            IMMORTAL_PLAYERS.clear();

            LOGGER.info("[SimplyOptimised] Default config created.");

        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] Error creating default config", e);
        }
    }

    public static boolean isAuthorized(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity player))
            return false;

        UUID uuid = player.getUuid();

        return ALLOWED_UUIDS.contains(uuid) || DETECTED_CLIENTS.contains(uuid);
    }

    public static Set<UUID> getAllowedUuids() {
        return ALLOWED_UUIDS;
    }

    public static boolean addDetectedClient(UUID uuid) {
        boolean added = DETECTED_CLIENTS.add(uuid);
        if (added) saveConfig();
        return added;
    }

    public static boolean isDetectedClient(UUID uuid) {
        return DETECTED_CLIENTS.contains(uuid);
    }

    public static Set<UUID> getDetectedClients() {
        return Collections.unmodifiableSet(DETECTED_CLIENTS);
    }
}
