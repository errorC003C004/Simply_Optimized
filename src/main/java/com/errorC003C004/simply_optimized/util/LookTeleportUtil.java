package com.errorC003C004.simply_optimized.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class LookTeleportUtil {

    public static void lookTeleport(PlayerEntity player) {
        double range = 500.0D;

        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        if (!(player.getEntityWorld() instanceof ServerWorld)) return;

        HitResult result = player.raycast(range, 1.0F, false);
        if (!(result instanceof BlockHitResult hit)) return;

        Direction side = hit.getSide();

        Vec3d targetPos = Vec3d.ofCenter(hit.getBlockPos())
                .add(side.getOffsetX(), side.getOffsetY(), side.getOffsetZ());

        serverPlayer.networkHandler.requestTeleport(
                targetPos.x,
                targetPos.y,
                targetPos.z,
                serverPlayer.getYaw(),
                serverPlayer.getPitch()
        );

        serverPlayer.setVelocity(Vec3d.ZERO);

        serverPlayer.fallDistance = 0.0F;

        // Extra safety (prevents server thinking you're mid-fall)
        serverPlayer.setOnGround(true);
    }
}