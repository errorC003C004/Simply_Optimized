package com.errorC003C004.simply_optimized.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.glfw.GLFW;
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
            FabricLoader.getInstance().getConfigDir().resolve("error_simply_optimized_client.json");

    public static boolean isClientWhitelisted = false;
    public static boolean showVisualizer = false;

    public static boolean useKeybinds = false;
    public static int openMenuKey = GLFW.GLFW_KEY_RIGHT_SHIFT;
    public static int toggleImmortalityKey = GLFW.GLFW_KEY_G;

    public static void toggleKeybinds() {
        useKeybinds = !useKeybinds;
        UIFunctions.usingKeybinds = useKeybinds;
        saveConfig();
    }

    public static void addClientWhitelist() {
        isClientWhitelisted = true;
        saveConfig();
    }

    public static void removeClientWhitelist() {
        isClientWhitelisted = false;
        saveConfig();
    }

    public static void showVisualizerTrue() {
        showVisualizer = true;
        saveConfig();
    }

    public static boolean isShowVisualizer() {
        return showVisualizer;
    }

    public static void showVisualizerFalse() {
        showVisualizer = false;
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

            if (json.has("show_visualizer")) {
                showVisualizer = json.get("show_visualizer").getAsBoolean();
            }

            if (json.has("use_keybinds")) {
                useKeybinds = json.get("use_keybinds").getAsBoolean();
            }

            if (json.has("open_menu_key")) {
                openMenuKey = json.get("open_menu_key").getAsInt();
            }

            if (json.has("toggle_immortality_key")) {
                toggleImmortalityKey = json.get("toggle_immortality_key").getAsInt();
            }

            LOGGER.info("[SimplyOptimised] Player is "
                    + (isClientWhitelisted ? "" : "NOT ")
                    + "whitelisted. Show Image: " + showVisualizer);

        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] Config corrupted. Recreating.", e);
            createDefaultConfig();
        }
    }

    public static void saveConfig() {
        try {
            JsonObject json = new JsonObject();

            json.addProperty("is_client_whitelisted", isClientWhitelisted);
            json.addProperty("show_visualizer", showVisualizer);

            json.addProperty("use_keybinds", useKeybinds);
            json.addProperty("open_menu_key", openMenuKey);
            json.addProperty("toggle_immortality_key", toggleImmortalityKey);


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
            json.addProperty("show_visualizer", false);

            json.addProperty("use_keybinds", false);
            json.addProperty("open_menu_key", GLFW.GLFW_KEY_RIGHT_SHIFT);
            json.addProperty("toggle_feature_key", GLFW.GLFW_KEY_G);


            Files.createDirectories(CONFIG_PATH.getParent());

            try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(json, writer);
            }

            isClientWhitelisted = false;
            showVisualizer = false;
            useKeybinds = false;

            LOGGER.info("[SimplyOptimised] Default Client Config Created.");

        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] Error creating default config", e);
        }
    }
}