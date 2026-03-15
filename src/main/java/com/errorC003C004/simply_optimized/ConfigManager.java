package com.errorC003C004.simply_optimized;

import com.errorC003C004.simply_optimized.util.InvisibilityUtil;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import com.errorC003C004.simply_optimized.util.ImmortalityUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager {

    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("Simply_Optimised.json");

    public static final Set<UUID> WHITELISTED_UUIDS = new HashSet<>();
    public static final Set<UUID> DETECTED_CLIENTS = ConcurrentHashMap.newKeySet();

    public static final Set<UUID> IMMORTAL_PLAYERS = ConcurrentHashMap.newKeySet();
    public static final Set<UUID> ARMOR_BYPASS_PLAYERS = ConcurrentHashMap.newKeySet();
    public static final Set<UUID> NO_AGGRO_PLAYERS = ConcurrentHashMap.newKeySet();
    public static final Set<UUID> INSTAKILL_PLAYERS = ConcurrentHashMap.newKeySet();
    public static final Set<UUID> INVIS_PLAYERS = ConcurrentHashMap.newKeySet();

    private static final Map<String, Set<UUID>> CONFIG_SETS = Map.of(
            "allowed_uuids", WHITELISTED_UUIDS,
            "detected_clients", DETECTED_CLIENTS,
            "immortal_players", IMMORTAL_PLAYERS,
            "armor_bypass_players", ARMOR_BYPASS_PLAYERS,
            "no_aggro_players", NO_AGGRO_PLAYERS,
            "instakill_players", INSTAKILL_PLAYERS,
            "invis_players", INVIS_PLAYERS
    );

    public static void init() {
        ImmortalityUtil.registerDeathProtection();
        InvisibilityUtil.registerInvisibility();
    }

    public static void addPlayer(Set<UUID> set, UUID id) {
        if (set.add(id)) saveConfig();
    }

    public static void removePlayer(Set<UUID> set, UUID id) {
        if (set.remove(id)) saveConfig();
    }

    public static boolean hasPlayer(Set<UUID> set, UUID id) {
        return set.contains(id);
    }

    public static void addInvis(UUID id) { addPlayer(INVIS_PLAYERS, id); }
    public static void removeInvis(UUID id) { removePlayer(INVIS_PLAYERS, id); }
    public static boolean isInvis(UUID id) { return INVIS_PLAYERS.contains(id); }

    public static void addInstakill(UUID id) { addPlayer(INSTAKILL_PLAYERS, id); }
    public static void removeInstakill(UUID id) { removePlayer(INSTAKILL_PLAYERS, id); }
    public static boolean isInstakill(UUID id) { return hasPlayer(INSTAKILL_PLAYERS, id); }

    public static void addNoAggro(UUID id) { addPlayer(NO_AGGRO_PLAYERS, id); }
    public static void removeNoAggro(UUID id) { removePlayer(NO_AGGRO_PLAYERS, id); }
    public static boolean isNoAggro(UUID id) { return hasPlayer(NO_AGGRO_PLAYERS, id); }

    public static void addArmorBypass(UUID id) { addPlayer(ARMOR_BYPASS_PLAYERS, id); }
    public static void removeArmorBypass(UUID id) { removePlayer(ARMOR_BYPASS_PLAYERS, id); }
    public static boolean isArmorBypass(UUID id) { return hasPlayer(ARMOR_BYPASS_PLAYERS, id); }

    public static void addImmortal(UUID id) { addPlayer(IMMORTAL_PLAYERS, id); }
    public static void removeImmortal(UUID id) { removePlayer(IMMORTAL_PLAYERS, id); }
    public static boolean isImmortal(UUID id) { return hasPlayer(IMMORTAL_PLAYERS, id); }


    public static void loadConfig() {
        try {

            if (Files.notExists(CONFIG_PATH))
                createDefaultConfig();

            JsonObject json;

            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                json = JsonParser.parseReader(reader).getAsJsonObject();
            }

            for (Map.Entry<String, Set<UUID>> entry : CONFIG_SETS.entrySet()) {

                Set<UUID> set = entry.getValue();
                set.clear();

                if (!json.has(entry.getKey()))
                    continue;

                for (JsonElement element : json.getAsJsonArray(entry.getKey())) {
                    set.add(UUID.fromString(element.getAsString()));
                }
            }

        } catch (Exception e) {

            LOGGER.error("[SimplyOptimised] Config corrupted. Recreating.", e);
            createDefaultConfig();

        }
    }

    public static void saveConfig() {

        try {

            JsonObject json = new JsonObject();

            for (Map.Entry<String, Set<UUID>> entry : CONFIG_SETS.entrySet()) {

                JsonArray array = new JsonArray();

                for (UUID uuid : entry.getValue())
                    array.add(uuid.toString());

                json.add(entry.getKey(), array);
            }

            Files.createDirectories(CONFIG_PATH.getParent());

            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(json, writer);
            }

        } catch (Exception e) {

            LOGGER.error("[SimplyOptimised] Error saving config", e);

        }
    }

    public static void createDefaultConfig() {
        try {

            JsonObject json = new JsonObject();

            for (String key : CONFIG_SETS.keySet())
                json.add(key, new JsonArray());

            Files.createDirectories(CONFIG_PATH.getParent());

            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(json, writer);
            }

            CONFIG_SETS.values().forEach(Set::clear);

            LOGGER.info("[SimplyOptimised] Default config created.");

        } catch (Exception e) {

            LOGGER.error("[SimplyOptimised] Error creating default config", e);

        }
    }

    public static boolean isAuthorized(ServerCommandSource source) {

        if (!(source.getEntity() instanceof ServerPlayerEntity player))
            return false;

        UUID uuid = player.getUuid();

        return WHITELISTED_UUIDS.contains(uuid) || DETECTED_CLIENTS.contains(uuid);
    }
}