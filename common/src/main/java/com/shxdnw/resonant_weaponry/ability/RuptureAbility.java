package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class RuptureAbility implements LegendaryAbility {
    @Override
    public void activate(AbilityContext context) {
        ServerPlayer player = context.wielder();
        ServerLevel level = player.level();
        ResonantWeaponryConfig.LegendaryWeapons.Voidfang config =
                ResonantWeaponryConfig.legendaryWeapons.voidfang;

        Vec3 start = player.position();
        Vec3 direction = new Vec3(player.getLookAngle().x, 0.0, player.getLookAngle().z);
        if (direction.lengthSqr() < 1.0E-4) {
            direction = Vec3.directionFromRotation(0.0f, player.getYRot());
        }
        direction = direction.normalize();

        Vec3 destination = start;
        for (double distance = 0.5; distance <= config.teleportDistance; distance += 0.5) {
            Vec3 candidate = start.add(direction.scale(distance));
            if (!level.noCollision(player, player.getBoundingBox().move(candidate.subtract(start)))) {
                break;
            }
            destination = candidate;
        }

        player.teleportTo(destination.x, destination.y, destination.z);
        level.playSound(null, start.x, start.y, start.z, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0f, 0.7f);
        DebugLog.log("Rupture: surged {} blocks", start.distanceTo(destination));

        Vec3 origin = start;
        Vec3 target = destination;
        Scheduler.schedule(level, config.trailDelay, resolved -> erupt(player, resolved, origin, target, config));
    }

    private static void erupt(ServerPlayer owner, ServerLevel level, Vec3 start, Vec3 end,
                              ResonantWeaponryConfig.LegendaryWeapons.Voidfang config) {
        // owner might be gone by now
        if (owner.isRemoved() || level.getServer() == null
                || owner.level() != level
                || level.getServer().getPlayerList().getPlayer(owner.getUUID()) == null) {
            DebugLog.log("Rupture: owner gone, skipped");
            return;
        }
        List<Vec3> points = new ArrayList<>();
        Vec3 path = end.subtract(start);
        double length = path.length();
        if (length < 0.001) {
            points.add(start);
        } else {
            for (double travelled = 0.0; travelled <= length; travelled += 1.5) {
                points.add(start.add(path.normalize().scale(travelled)));
            }
            points.add(end);
        }

        Set<UUID> knocked = new HashSet<>();
        Set<UUID> damaged = new HashSet<>();
        var random = level.getRandom();
        for (Vec3 point : points) {
            level.playSound(null, point.x, point.y, point.z, SoundEvents.GENERIC_EXPLODE.value(),
                    SoundSource.PLAYERS, 0.8f, 1.2f + random.nextFloat() * 0.4f);
            AABB area = new AABB(
                    point.x - config.trailRadius, point.y - 2.0, point.z - config.trailRadius,
                    point.x + config.trailRadius, point.y + 3.0, point.z + config.trailRadius);
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
                if (!Targeting.canAffect(owner, entity)) {
                    continue;
                }
                double dx = entity.getX() - point.x;
                double dz = entity.getZ() - point.z;
                double distance = Math.sqrt(dx * dx + dz * dz);
                if (distance > config.trailRadius) {
                    continue;
                }
                if (!damaged.add(entity.getUUID())) {
                    continue;
                }
                float falloff = (float) (1.0 - distance / config.trailRadius);
                entity.invulnerableTime = 0;
                entity.hurtServer(level, level.damageSources().explosion(owner, owner), config.trailDamage * falloff);
                if (knocked.add(entity.getUUID()) && distance > 0.001) {
                    entity.push(dx / distance * 2.0 * falloff, 0.5 * falloff, dz / distance * 2.0 * falloff);
                    entity.hurtMarked = true;
                }
            }
            for (int i = 0; i < 12; i++) {
                double angle = random.nextDouble() * Math.PI * 2.0;
                double distance = random.nextDouble() * config.trailRadius;
                level.sendParticles(new DustParticleOptions(0x8800CC, 1.5f),
                        point.x + Math.cos(angle) * distance,
                        point.y + random.nextDouble() * 1.5,
                        point.z + Math.sin(angle) * distance,
                        1, 0.05, 0.05, 0.05, 0.02);
            }
        }
        DebugLog.log("Rupture: trail erupted over {} points", points.size());
    }
}
