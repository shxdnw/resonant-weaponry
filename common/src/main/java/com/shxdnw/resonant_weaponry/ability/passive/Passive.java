package com.shxdnw.resonant_weaponry.ability.passive;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public interface Passive {
    default float modifyDamage(ServerPlayer player, LivingEntity target, float amount) {
        return amount;
    }

    default float modifyArmorPenetration(ServerPlayer player, LivingEntity target, float damage, float afterArmor) {
        return afterArmor;
    }

    default void onHit(ServerPlayer player, LivingEntity target) {
    }

    default void onKill(ServerPlayer player, LivingEntity target) {
    }
}
