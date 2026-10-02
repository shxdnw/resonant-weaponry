package com.shxdnw.resonant_weaponry.mixin;

import com.shxdnw.resonant_weaponry.ability.CombatHooks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// dont touch this
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    // nullifies instead of zeroing
    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
    private void resonantWeaponry$nullify(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (CombatHooks.nullifyIncoming((LivingEntity) (Object) this, source)) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), argsOnly = true)
    private float resonantWeaponry$modifyDamage(float amount, ServerLevel level, DamageSource source) {
        return CombatHooks.modifyDamage((LivingEntity) (Object) this, source, amount);
    }

    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("RETURN"))
    private void resonantWeaponry$onHit(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            CombatHooks.onHit(source, (LivingEntity) (Object) this);
        }
    }

    @Inject(method = "getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", at = @At("RETURN"), cancellable = true)
    private void resonantWeaponry$armorPenetration(DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(CombatHooks.armorPenetration((LivingEntity) (Object) this, source, damage, cir.getReturnValueF()));
    }
}
