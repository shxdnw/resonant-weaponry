package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class CataclysmLeap {
    private static final Map<UUID, Long> LEAPS = new HashMap<>();

    private CataclysmLeap() {
    }

    public static void start(ServerPlayer player) {
        ResonantWeaponryConfig.LegendaryWeapons.Calamity config =
                ResonantWeaponryConfig.legendaryWeapons.calamity;
        Vec3 direction = new Vec3(player.getLookAngle().x, 0.0, player.getLookAngle().z);
        if (direction.lengthSqr() < 1.0E-4) {
            direction = Vec3.directionFromRotation(0.0f, player.getYRot());
        }
        direction = direction.normalize();

        player.setDeltaMovement(direction.x * config.leapVelocityForward, config.leapVelocityUp,
                direction.z * config.leapVelocityForward);
        player.hurtMarked = true;
        // keep gravity, just skip fall damage
        player.resetFallDistance();
        LEAPS.put(player.getUUID(), ServerClock.now());
        DebugLog.log("Cataclysm: leap started for {}", player.getName().getString());
    }

    public static void tick(MinecraftServer server) {
        ResonantWeaponryConfig.LegendaryWeapons.Calamity config =
                ResonantWeaponryConfig.legendaryWeapons.calamity;
        long now = ServerClock.now();
        // snapshot, the slam can mutate this
        for (UUID id : List.copyOf(LEAPS.keySet())) {
            Long started = LEAPS.get(id);
            if (started == null) {
                continue;
            }
            ServerPlayer player = server.getPlayerList().getPlayer(id);
            if (player == null) {
                LEAPS.remove(id);
                continue;
            }

            Vec3 direction = new Vec3(player.getLookAngle().x, 0.0, player.getLookAngle().z);
            if (direction.lengthSqr() >= 1.0E-4) {
                direction = direction.normalize();
                player.setDeltaMovement(direction.x * config.leapVelocityForward * 0.5,
                        player.getDeltaMovement().y, direction.z * config.leapVelocityForward * 0.5);
                player.hurtMarked = true;
            }
            player.resetFallDistance();

            long elapsed = now - started;
            if ((elapsed >= 5 && player.onGround()) || elapsed >= 60) {
                LEAPS.remove(id);
                slam(player, config);
            }
        }
    }

    public static void cancel(ServerPlayer player) {
        LEAPS.remove(player.getUUID());
    }

    public static void clearAll() {
        LEAPS.clear();
    }

    private static void slam(ServerPlayer player, ResonantWeaponryConfig.LegendaryWeapons.Calamity config) {
        ServerLevel level = player.level();
        Vec3 center = player.position();
        var random = level.getRandom();

        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.PLAYERS, 2.0f, 0.6f);
        level.playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND,
                SoundSource.PLAYERS, 2.0f, 1.5f);

        AABB area = new AABB(
                center.x - config.impactRadius, center.y - 3.0, center.z - config.impactRadius,
                center.x + config.impactRadius, center.y + 5.0, center.z + config.impactRadius);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (!Targeting.canAffect(player, entity)) {
                continue;
            }
            double dx = entity.getX() - center.x;
            double dz = entity.getZ() - center.z;
            double distance = Math.sqrt(dx * dx + dz * dz);
            if (distance > config.impactRadius) {
                continue;
            }
            float falloff = (float) (1.0 - distance / config.impactRadius);
            entity.invulnerableTime = 0;
            entity.hurtServer(level, level.damageSources().explosion(player, player), config.impactDamage * falloff);
            if (distance > 0.001) {
                entity.push(dx / distance * 4.0 * falloff, 1.5 * falloff, dz / distance * 4.0 * falloff);
            } else {
                entity.push(0.0, 1.5 * falloff, 0.0);
            }
            entity.hurtMarked = true;
        }

        for (int i = 0; i < 87; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = random.nextDouble() * config.impactRadius;
            level.sendParticles(new DustParticleOptions(0xFF4400, 2.0f + random.nextFloat() * 5.0f),
                    center.x + Math.cos(angle) * distance,
                    center.y + random.nextDouble() * 0.5,
                    center.z + Math.sin(angle) * distance,
                    1, 0.05, 0.1, 0.05, 0.05);
        }
        for (int i = 0; i < 37; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = random.nextDouble() * config.impactRadius * 0.6;
            level.sendParticles(ParticleTypes.EXPLOSION,
                    center.x + Math.cos(angle) * distance,
                    center.y + 0.2 + random.nextDouble() * 3.0,
                    center.z + Math.sin(angle) * distance,
                    1, 0.2, 0.3, 0.2, 0.1);
        }
        DebugLog.log("Cataclysm: slam radius={}, damage={}", config.impactRadius, config.impactDamage);
    }
}
