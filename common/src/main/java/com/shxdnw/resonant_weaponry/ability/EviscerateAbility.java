package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class EviscerateAbility implements LegendaryAbility {
    @Override
    public void activate(AbilityContext context) {
        ServerPlayer player = context.wielder();
        ServerLevel level = player.level();
        ResonantWeaponryConfig.LegendaryWeapons.BloodScourge config =
                ResonantWeaponryConfig.legendaryWeapons.bloodScourge;
        Vec3 look = player.getLookAngle();
        Vec3 center = player.position().add(look.scale(config.slashReach / 2.0));

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1.0f, 0.5f);

        AABB box = new AABB(
                center.x - config.slashReach / 2.0, player.getY() - 1.0, center.z - config.slashReach / 2.0,
                center.x + config.slashReach / 2.0, player.getY() + 3.0, center.z + config.slashReach / 2.0);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box)) {
            if (!Targeting.canAffect(player, entity)) {
                continue;
            }
            entity.invulnerableTime = 0;
            entity.hurtServer(level, level.damageSources().playerAttack(player), config.slashDamage);
            Vec3 pull = player.position().subtract(entity.position()).normalize().scale(0.8);
            entity.push(pull.x, 0.2, pull.z);
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, config.witherDuration, 2));
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, config.blindnessDuration, 0));
            entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, config.slowDuration, 0));
        }

        var random = level.getRandom();
        Vec3 direction = look.multiply(1.0, 0.0, 1.0).normalize();
        if (direction.lengthSqr() < 0.001) {
            direction = Vec3.directionFromRotation(0.0f, player.getYRot());
        }
        double baseYaw = Math.atan2(direction.x, direction.z);

        for (int arc = 0; arc < 30; arc++) {
            double angle = baseYaw + (random.nextDouble() - 0.5) * Math.PI * 0.8;
            double dx = Math.sin(angle);
            double dz = Math.cos(angle);
            for (int d = 0; d < 3; d++) {
                double distance = d * 1.5 + random.nextDouble() * 1.5;
                double y = player.getY() + 0.8 + random.nextDouble() * 1.2;
                level.sendParticles(
                        random.nextBoolean()
                                ? new DustParticleOptions(0xCC0000, 2.0f + random.nextFloat() * 3.0f)
                                : new DustParticleOptions(0x110000, 1.5f + random.nextFloat() * 2.0f),
                        player.getX() + dx * distance, y, player.getZ() + dz * distance,
                        1, 0.03, 0.03, 0.03, 0.02);
            }
        }
        for (int sweep = 0; sweep < 12; sweep++) {
            double angle = baseYaw + (random.nextDouble() - 0.5) * Math.PI * 0.6;
            double distance = random.nextDouble() * config.slashReach;
            level.sendParticles(ParticleTypes.SWEEP_ATTACK,
                    player.getX() + Math.sin(angle) * distance,
                    player.getY() + 1.0 + random.nextDouble() * 1.5,
                    player.getZ() + Math.cos(angle) * distance,
                    1, 0.0, 0.0, 0.0, 0.0);
        }

        DebugLog.log("Eviscerate activated for {}", player.getName().getString());
    }
}
