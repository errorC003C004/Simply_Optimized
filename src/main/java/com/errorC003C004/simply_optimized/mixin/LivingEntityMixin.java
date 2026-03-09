package com.errorC003C004.simply_optimized.mixin;

import com.errorC003C004.simply_optimized.ConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
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

    @Inject(
            method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventMobAggro(
            LivingEntity target,
            CallbackInfoReturnable<Boolean> cir
    ) {
        LivingEntity self = (LivingEntity)(Object)this;

        if (!(self instanceof MobEntity)) return;

        if (target instanceof PlayerEntity player) {
            if (ConfigManager.isNoAggro(player.getUuid())) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
            method = "modifyAppliedDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void instakill(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        Entity attacker = source.getAttacker();
        LivingEntity target = (LivingEntity)(Object)this;

        if (attacker instanceof ServerPlayerEntity player) {
            if (ConfigManager.isInstakill(player.getUuid())) {
                cir.setReturnValue(target.getMaxHealth());
            }
        }
    }
}