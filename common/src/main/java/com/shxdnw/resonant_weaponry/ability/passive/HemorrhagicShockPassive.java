package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.ability.PassiveCooldowns;
import com.shxdnw.resonant_weaponry.ability.Targeting;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class HemorrhagicShockPassive implements Passive {
    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        ResonantWeaponryConfig.LegendaryWeapons.BloodScourge config =
                ResonantWeaponryConfig.legendaryWeapons.bloodScourge;
        boolean debuffed = target.getActiveEffects().stream()
                .anyMatch(effect -> !effect.getEffect().value().isBeneficial());
        if (!debuffed || !PassiveCooldowns.ready(player, "blood_scourge:hemo", config.hemoCooldown)) {
            return;
        }

        ServerLevel level = player.level();
        Vec3 position = target.position();
        AABB area = new AABB(
                position.x - config.hemoAoERadius, position.y - 2.0, position.z - config.hemoAoERadius,
                position.x + config.hemoAoERadius, position.y + 4.0, position.z + config.hemoAoERadius);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (!Targeting.canAffect(player, entity)) {
                continue;
            }
            entity.invulnerableTime = 0;
            entity.hurtServer(level, level.damageSources().indirectMagic(player, player), config.hemoAoEDamage);
        }

        var random = level.getRandom();
        for (int i = 0; i < 25; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = random.nextDouble() * config.hemoAoERadius;
            level.sendParticles(new DustParticleOptions(0xCC0000, 2.0f + random.nextFloat() * 4.0f),
                    position.x + Math.cos(angle) * distance,
                    position.y + random.nextDouble() * 3.0,
                    position.z + Math.sin(angle) * distance,
                    1, 0.1, 0.1, 0.1, 0.05);
        }
        DebugLog.log("Hemorrhagic Shock: burst at {}", target.getName().getString());
    }
}
