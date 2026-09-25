package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.ability.MovementTracker;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public final class CounterweightPassive implements Passive {
    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        boolean backward = MovementTracker.isMovingBackward(player);
        DebugLog.log("Counterweight check for {}: backward={}", player.getName().getString(), backward);
        if (backward) {
            ResonantWeaponryConfig.LegendaryWeapons.GildedArbiter config =
                    ResonantWeaponryConfig.legendaryWeapons.gildedArbiter;
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, config.counterSlowTime, 0));
            DebugLog.log("Counterweight: slowness applied to {}", target.getName().getString());
        }
    }
}
