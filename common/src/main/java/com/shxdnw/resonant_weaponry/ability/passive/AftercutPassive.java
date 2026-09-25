package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.ability.Aftercuts;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public final class AftercutPassive implements Passive {
    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        Aftercuts.onHit(player, target);
    }
}
