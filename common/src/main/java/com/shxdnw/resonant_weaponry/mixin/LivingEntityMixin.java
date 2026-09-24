package com.shxdnw.resonant_weaponry.mixin;

import com.shxdnw.resonant_weaponry.ability.FirstHitTracker;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import com.shxdnw.resonant_weaponry.content.LegendaryWeaponItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), argsOnly = true)
    private float resonantWeaponry$firstMove(float amount, ServerLevel level, DamageSource source) {
        if (source.getEntity() instanceof Player player
                && isTheRealKnife(player.getMainHandItem())
                && FirstHitTracker.isFirstHit(player, (LivingEntity) (Object) this)) {
            return amount * 2.0f;
        }
        return amount;
    }

    @Inject(method = "getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", at = @At("RETURN"), cancellable = true)
    private void resonantWeaponry$armorPenetration(DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
        if (source.getEntity() instanceof Player player
                && isTheRealKnife(player.getMainHandItem())
                && player.getHealth() < player.getMaxHealth() * ResonantWeaponryConfig.legendaryWeapons.theRealKnife.relentlessBelow) {
            float absorbed = damage - cir.getReturnValueF();
            cir.setReturnValue(cir.getReturnValueF() + absorbed * ResonantWeaponryConfig.legendaryWeapons.theRealKnife.armorPen);
        }
    }

    private static boolean isTheRealKnife(ItemStack stack) {
        return stack.getItem() instanceof LegendaryWeaponItem item && item.definition().id().equals("the_real_knife");
    }
}
