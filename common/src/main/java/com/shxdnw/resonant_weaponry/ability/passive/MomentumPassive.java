package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.ability.Momentum;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public final class MomentumPassive implements Passive {
    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        Momentum.onHit(player);
    }
}
