package com.errorC003C004.simply_optimized.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LookExplosionUtil {

    public static void railgunTunnel(
            PlayerEntity player,
            double range,
            float power
    ) {

        World world = player.getEntityWorld();
        if (!(world instanceof ServerWorld serverWorld)) return;

        HitResult result = player.raycast(range, 1.0F, false);
        if (!(result instanceof BlockHitResult hit)) return;

        Vec3d start = hit.getPos();
        Vec3d dir = player.getRotationVec(1.0F).normalize();

        // --- TUNNEL SETTINGS ---
        double spacing = 1.0; // heavy overlap = tunnel
        int explosions = (int)(range / spacing);

        explosions = Math.max(1, Math.min(explosions, 600));

        for (int i = 0; i < explosions; i++) {

            Vec3d pos = start.add(dir.multiply(i * spacing));

            serverWorld.createExplosion(
                    player,
                    pos.x,
                    pos.y,
                    pos.z,
                    power,      // 4.0F = TNT
                    false,
                    World.ExplosionSourceType.MOB
            );
        }
    }
}