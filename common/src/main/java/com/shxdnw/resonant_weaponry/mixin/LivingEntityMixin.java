package com.shxdnw.resonant_weaponry.mixin;

import com.shxdnw.resonant_weaponry.ability.CombatHooks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), argsOnly = true)
    private float resonantWeaponry$modifyDamage(float amount, ServerLevel level, DamageSource source) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (CombatHooks.nullifyIncoming(target, source)) {
            return 0.0f;
        }
        return CombatHooks.modifyDamage(target, source, amount);
    }

    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("RETURN"))
    private void resonantWeaponry$onHit(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && source.getEntity() instanceof ServerPlayer serverPlayer) {
            CombatHooks.onHit(serverPlayer, (LivingEntity) (Object) this);
        }
    }

    @Inject(method = "getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", at = @At("RETURN"), cancellable = true)
    private void resonantWeaponry$armorPenetration(DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(CombatHooks.armorPenetration((LivingEntity) (Object) this, source, damage, cir.getReturnValueF()));
    }
}
