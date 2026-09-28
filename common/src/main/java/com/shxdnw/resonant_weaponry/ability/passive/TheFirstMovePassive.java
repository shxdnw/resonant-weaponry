package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.ability.FirstHitTracker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public final class TheFirstMovePassive implements Passive {
    @Override
    public float modifyDamage(ServerPlayer player, LivingEntity target, float amount) {
        if (FirstHitTracker.isFirstHit(player, target)) {
            DebugLog.log("First Move: doubling damage against {}", target.getName().getString());
            return amount * 2.0f;
        }
        return amount;
    }

    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        FirstHitTracker.markHit(player, target);
    }
}
