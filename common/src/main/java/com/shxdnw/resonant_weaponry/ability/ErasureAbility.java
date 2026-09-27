package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import com.shxdnw.resonant_weaponry.content.LegendaryWeapon;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class ErasureAbility implements LegendaryAbility {
    @Override
    public void activate(AbilityContext context) {
        ServerPlayer player = context.wielder();
        ServerLevel level = player.level();
        LegendaryWeapon.Activation activation = context.weapon().activation();
        Vec3 origin = player.position();

        player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, activation.channelTicks(), 4));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 2.0f);
        DebugLog.log("Erasure: channel started for {}", player.getName().getString());

        Channels.start(player, activation.channelTicks(),
                elapsed -> {
                    if (elapsed == 40 || elapsed == 80) {
                        level.playSound(null, origin.x, origin.y, origin.z, SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.6f);
                    }
                    spawnPlate(level, origin);
                },
                self -> detonate(self, level, origin));
    }

    private static void spawnPlate(ServerLevel level, Vec3 origin) {
        ResonantWeaponryConfig.LegendaryWeapons.TheRealKnife config =
                ResonantWeaponryConfig.legendaryWeapons.theRealKnife;
        var random = level.getRandom();

        for (int i = 0; i < 50; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = config.explosionRadius * Math.sqrt(random.nextDouble());
            double x = origin.x + Math.cos(angle) * distance;
            double z = origin.z + Math.sin(angle) * distance;
            float size = 1.5f + random.nextFloat() * 2.0f;

            level.sendParticles(new DustParticleOptions(0xCC1111, size),
                    x, origin.y + 0.1 + random.nextDouble() * 0.3, z,
                    1, 0.05, 0.4 + random.nextDouble() * 0.5, 0.05, 0.1);
            if (random.nextBoolean()) {
                level.sendParticles(new DustParticleOptions(0x880000, size),
                        x, origin.y - random.nextDouble() * config.columnDepth, z,
                        1, 0.05, 0.1, 0.05, 0.02);
            }
        }
    }

    private static void detonate(ServerPlayer player, ServerLevel level, Vec3 origin) {
        ResonantWeaponryConfig.LegendaryWeapons.TheRealKnife config =
                ResonantWeaponryConfig.legendaryWeapons.theRealKnife;
        level.playSound(null, origin.x, origin.y, origin.z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 2.0f, 0.7f);

        AABB box = new AABB(
                origin.x - config.explosionRadius, origin.y - config.downReach, origin.z - config.explosionRadius,
                origin.x + config.explosionRadius, origin.y + config.upReach, origin.z + config.explosionRadius);
        DamageSource source = level.damageSources().explosion(player, player);

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box)) {
            if (!Targeting.canAffect(player, entity)) {
                continue;
            }
            double dx = entity.getX() - origin.x;
            double dz = entity.getZ() - origin.z;
            double distance = Math.sqrt(dx * dx + dz * dz);
            if (distance > config.explosionRadius) {
                continue;
            }
            float falloff = (float) (1.0 - distance / config.explosionRadius);
            entity.invulnerableTime = 0;
            entity.hurtServer(level, source, config.explosionDamage * falloff);
            if (distance > 0.001) {
                entity.push(dx / distance * 3.0 * falloff, 0.5 * falloff, dz / distance * 3.0 * falloff);
                entity.hurtMarked = true;
            }
        }

        double angle = level.getRandom().nextDouble() * Math.PI * 2.0;
        player.randomTeleport(
                origin.x + Math.cos(angle) * config.teleportDistance,
                origin.y,
                origin.z + Math.sin(angle) * config.teleportDistance,
                true);

        ErasureFx.start(level, origin);
    }
}
