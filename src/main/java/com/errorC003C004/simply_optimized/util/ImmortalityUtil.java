package com.errorC003C004.simply_optimized.util;

import com.errorC003C004.simply_optimized.ConfigManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class ImmortalityUtil {

    public static void registerDeathProtection() {

        ServerLivingEntityEvents.ALLOW_DEATH.register(
                (entity, source, amount) -> {

                    if (!(entity instanceof ServerPlayerEntity player)) {
                        return true;
                    }

                    // SINGLE SOURCE OF TRUTH
                    if (!isImmortal(player)) {
                        return true;
                    }

                    // Let vanilla totems work normally
                    if (hasTotem(player)) {
                        return true;
                    }

                    // Prevent death safely
                    player.setHealth(1.0F);
                    return false;
                });
    }

    private static boolean hasTotem(ServerPlayerEntity player) {
        return player.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING)
                || player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING);
    }

    private static boolean isImmortal(ServerPlayerEntity player) {
        return ConfigManager.isImmortal(player.getUuid());
    }
}