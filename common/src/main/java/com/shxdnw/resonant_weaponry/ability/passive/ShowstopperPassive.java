package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;

public final class ShowstopperPassive implements Passive {
    @Override
    public float modifyDamage(ServerPlayer player, LivingEntity target, float amount) {
        if (target.isSprinting() || !target.onGround()) {
            float multiplier = ResonantWeaponryConfig.legendaryWeapons.gildedArbiter.showstopperMult;
            DebugLog.log("Showstopper: {}x damage against {}", multiplier, target.getName().getString());
            return amount * multiplier;
        }
        return amount;
    }

    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        if (target instanceof Enemy) {
            ResonantWeaponryConfig.LegendaryWeapons.GildedArbiter config =
                    ResonantWeaponryConfig.legendaryWeapons.gildedArbiter;
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, config.witherTime, config.witherLevel));
            DebugLog.log("Showstopper: wither applied to {}", target.getName().getString());
        }
    }
}
