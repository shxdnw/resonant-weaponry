package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class CycloneAbility implements LegendaryAbility {
    @Override
    public void activate(AbilityContext context) {
        ServerPlayer player = context.wielder();
        ServerLevel level = player.level();
        ResonantWeaponryConfig.LegendaryWeapons.GaleCutter config =
                ResonantWeaponryConfig.legendaryWeapons.galeCutter;
        double radius = config.cycloneRadius;
        Vec3 center = player.position();

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 1.5f, 0.7f);

        AABB box = new AABB(
                center.x - radius, center.y - 2.0, center.z - radius,
                center.x + radius, center.y + 5.0, center.z + radius);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box)) {
            if (!Targeting.canAffect(player, entity)) {
                continue;
            }
            double dx = entity.getX() - center.x;
            double dz = entity.getZ() - center.z;
            if (Math.sqrt(dx * dx + dz * dz) > radius) {
                continue;
            }
            entity.invulnerableTime = 0;
            entity.hurtServer(level, level.damageSources().indirectMagic(player, player), config.cycloneDamage);
            Vec3 knock = entity.position().subtract(center).normalize();
            entity.push(knock.x * 3.5, 1.2, knock.z * 3.5);
        }

        var random = level.getRandom();
        for (int ring = 0; ring < 8; ring++) {
            double ringRadius = radius * (ring + 1) / 8.0;
            double ringY = 0.3 + ring * 0.6;
            for (int i = 0; i < 30; i++) {
                double angle = random.nextDouble() * Math.PI * 2.0;
                double distance = random.nextDouble() * ringRadius;
                level.sendParticles(new DustParticleOptions(0x00DD66, 1.5f + random.nextFloat() * 3.0f),
                        center.x + Math.cos(angle) * distance,
                        center.y + ringY + random.nextDouble() * 0.5,
                        center.z + Math.sin(angle) * distance,
                        1, 0.1, 0.1, 0.1, 0.05);
            }
        }
        for (int i = 0; i < 40; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = random.nextDouble() * radius;
            level.sendParticles(ParticleTypes.GUST,
                    center.x + Math.cos(angle) * distance,
                    center.y + 0.5 + random.nextDouble() * 4.0,
                    center.z + Math.sin(angle) * distance,
                    1, 0.3, 0.3, 0.3, 0.08);
        }

        DebugLog.log("Cyclone activated for {}", player.getName().getString());
    }
}
