package com.errorC003C004.simply_optimized.networking;

import com.errorC003C004.simply_optimized.CommandInit;

import com.errorC003C004.simply_optimized.ConfigManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HandshakeServer {
    private HandshakeServer() {}
    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");

    private static long tickCounter = 0;

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final String FILE_NAME = "Simply_Optimised.json";

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            UUID uuid = player.getUuid();

            HandshakeTracker.JOIN_TICK.put(uuid, tickCounter);
            HandshakeTracker.DONE.put(uuid, false);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            UUID uuid = handler.getPlayer().getUuid();

            HandshakeTracker.JOIN_TICK.remove(uuid);
            HandshakeTracker.DONE.remove(uuid);

            if (ConfigManager.DETECTED_CLIENTS.remove(uuid)) {
                removeDetectedClient(uuid);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(PingPayload.ID, (payload, context) -> {

            ServerPlayerEntity player = context.player();
            UUID uuid = player.getUuid();

            if (HandshakeTracker.DONE.getOrDefault(uuid, false)) return;

            context.server().execute(() -> {
                HandshakeTracker.DONE.put(uuid, true);
                player.sendMessage(Text.literal("Connected!"), false);
                addDetectedClient(uuid);
                ConfigManager.loadConfig();
                context.server().getCommandManager().sendCommandTree(player);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(
                ClientActionPayload.ID,
                (payload, context) -> {

                    ServerPlayerEntity player = context.player();

                    context.server().execute(() -> {

                        switch (payload.action()) {

                            case IMMORTALITY_TOGGLE -> IMMORTALITY_TOG(player);

                            case TOGGLE_FEATURE ->
                                    player.sendMessage(Text.literal("Feature toggled"), false);

                            case OPEN_MENU ->
                                    player.sendMessage(Text.literal("Menu opened"), false);
                        }
                    });
                }
        );
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID uuid = player.getUuid();

                Long joinTick = HandshakeTracker.JOIN_TICK.get(uuid);
                if (joinTick == null) continue;

                boolean done = HandshakeTracker.DONE.getOrDefault(uuid, false);
                if (done) continue;

                if (tickCounter - joinTick >= 100) {
                    HandshakeTracker.DONE.put(uuid, true);
                }
            }
        });
    }


    private static void IMMORTALITY_TOG(ServerPlayerEntity player) {
        UUID id = player.getUuid();

        boolean immortal;

        if (!ConfigManager.isImmortal(id)) {
            ConfigManager.addImmortal(id);
            immortal = true;
        } else {
            ConfigManager.removeImmortal(id);
            immortal = false;
        }

        ServerPlayNetworking.send(
                player,
                new ImmortalityStatusPayload(immortal)
        );

        //player.sendMessage(
        //        Text.literal("Immortality is " + immortal + "!"),
       //         false
       // );
    }


    private static void addDetectedClient(UUID uuid) {
        Path path = FabricLoader.getInstance()
                .getConfigDir()
                .resolve(FILE_NAME);

        try {
            JsonObject root;

            if (Files.exists(path)) {
                String json = Files.readString(path);
                root = GSON.fromJson(json, JsonObject.class);
                if (root == null) root = new JsonObject();
            } else {
                root = new JsonObject();
            }

            JsonArray detected = root.has("detected_clients")
                    ? root.getAsJsonArray("detected_clients")
                    : new JsonArray();

            root.add("detected_clients", detected);

            String uuidString = uuid.toString();

            boolean exists = false;
            for (int i = 0; i < detected.size(); i++) {
                if (detected.get(i).getAsString().equals(uuidString)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                detected.add(uuidString);
                Files.writeString(path, GSON.toJson(root));
            }
        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] Error creating HandshakeServer.addDetectedClient", e);
        }
    }
    private static void removeDetectedClient(UUID uuid) {
        Path path = FabricLoader.getInstance()
                .getConfigDir()
                .resolve(FILE_NAME);

        try {
            if (!Files.exists(path)) return;

            String json = Files.readString(path);
            JsonObject root = GSON.fromJson(json, JsonObject.class);

            if (root == null || !root.has("detected_clients")) return;

            JsonArray detected = root.getAsJsonArray("detected_clients");
            String uuidString = uuid.toString();

            JsonArray updated = new JsonArray();
            boolean changed = false;

            for (int i = 0; i < detected.size(); i++) {
                String entry = detected.get(i).getAsString();

                if (!entry.equals(uuidString)) {
                    updated.add(entry);
                } else {
                    changed = true;
                }
            }

            if (changed) {
                root.add("detected_clients", updated);
                Files.writeString(path, GSON.toJson(root));
            }
            ConfigManager.saveConfig();

        } catch (Exception e) {
            LOGGER.error("[SimplyOptimised] Error creating HandshakeServer.removeDetectedClient", e);
        }
    }
}