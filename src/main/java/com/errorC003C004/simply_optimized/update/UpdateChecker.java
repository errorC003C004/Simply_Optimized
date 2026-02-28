package com.errorC003C004.simply_optimized.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public final class UpdateChecker {

    private static final String MOD_ID = "simply_optimized";
    private static final String VERSION_URL = "https://pastebin.com/raw/PNgUtuvT";
    //private static final long CACHE_SECONDS = 60 * 60 * 12; // 12 hours
    private static final long CACHE_SECONDS = 5; // 5 secs

    private static Instant lastCheck = Instant.EPOCH;
    private static String latestVersion;
    private static String downloadUrl;

    private static final HttpClient client = HttpClient.newHttpClient();
    private UpdateChecker() {}

    public static void check(MinecraftServer server) {
        if (Instant.now().isBefore(lastCheck.plusSeconds(CACHE_SECONDS))) {
            notifyIfOutdated(server);
            return;
        }

        fetch().thenAccept(response -> {
            if (response == null) return;

            try {
                JsonObject json = JsonParser.parseString(response).getAsJsonObject();

                if (!json.has("latest") || !json.has("download")) return;

                latestVersion = json.get("latest").getAsString();
                downloadUrl = json.get("download").getAsString();
                lastCheck = Instant.now();

                notifyIfOutdated(server);
            } catch (Exception ignored) {
            }
        });
    }
    private static CompletableFuture<String> fetch() {
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(VERSION_URL))
                    .GET()
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .exceptionally(e -> null);

        } catch (Exception e) {
            return CompletableFuture.completedFuture(null);
        }
    }

    private static void notifyIfOutdated(MinecraftServer server) {
        if (latestVersion == null || downloadUrl == null) return;

        String currentVersion = FabricLoader.getInstance()
                .getModContainer(MOD_ID)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("0.0.0");

        if (!isOutdated(currentVersion, latestVersion)){
            server.execute(() -> {
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    player.sendMessage(
                            Text.literal("Up to Date!")
                                    .formatted(Formatting.GREEN),
                            false
                    );
                }
            });
            return;
        }

        server.execute(() -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {


                player.sendMessage(
                        Text.literal("[Simply Optimized] Update available!")
                                .formatted(Formatting.GOLD),
                        false
                );

                player.sendMessage(
                        Text.literal("Current: " + currentVersion + " | Latest: " + latestVersion)
                                .formatted(Formatting.GRAY),
                        false
                );

                player.sendMessage(
                        Text.literal("Click here to download")
                                .setStyle(
                                        Style.EMPTY
                                                .withFormatting(Formatting.AQUA)
                                                .withClickEvent(
                                                        new ClickEvent.OpenUrl(URI.create(downloadUrl))

                                                )
                                ),
                        false
                );
            }
        });
    }

    private static boolean isOutdated(String current, String latest) {
        try {
            String[] c = current.split("\\.");
            String[] l = latest.split("\\.");

            for (int i = 0; i < Math.max(c.length, l.length); i++) {
                int cv = i < c.length ? parseIntSafe(c[i]) : 0;
                int lv = i < l.length ? parseIntSafe(l[i]) : 0;

                if (cv < lv) return true;
                if (cv > lv) return false;
            }
        } catch (Exception ignored) {
        }

        return false;
    }

    private static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}