package com.errorC003C004.simply_optimized.util;

import com.errorC003C004.simply_optimized.ConfigManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InvisibilityUtil {

    private static final Set<UUID> vanished = new HashSet<>();

    public static void registerInvisibility() {

        ServerTickEvents.END_SERVER_TICK.register(server -> {

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                UUID id = player.getUuid();
                boolean shouldBeInvisible = ConfigManager.isInvis(id);
                boolean currentlyInvisible = vanished.contains(id);

                if (shouldBeInvisible && !currentlyInvisible) {
                    hidePlayer(player);
                    vanished.add(id);
                }

                if (!shouldBeInvisible && currentlyInvisible) {
                    showPlayer(player);
                    vanished.remove(id);
                }
            }
        });
    }

    private static void hidePlayer(ServerPlayerEntity player) {

        for (ServerPlayerEntity other : player.getEntityWorld().getPlayers()) {

            if (other == player) continue;

            other.networkHandler.sendPacket(
                    new EntitiesDestroyS2CPacket(player.getId())
            );
        }
    }

    private static void showPlayer(ServerPlayerEntity player) {

        player.getEntityWorld().getChunkManager().updatePosition(player);
    }
}
