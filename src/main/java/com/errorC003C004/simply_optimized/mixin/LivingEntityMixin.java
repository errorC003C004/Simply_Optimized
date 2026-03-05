package com.errorC003C004.simply_optimized.mixin;

import com.errorC003C004.simply_optimized.ConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(
            method = "applyArmorToDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ignoreArmorForSpecificPlayers(
            DamageSource source,
            float amount,
            CallbackInfoReturnable<Float> cir
    ) {
        Entity attacker = source.getAttacker();

        if (attacker instanceof ServerPlayerEntity player) {
            if (ConfigManager.isArmorBypass(player.getUuid())) {
                cir.setReturnValue(amount);
            }   
        }
    }
}