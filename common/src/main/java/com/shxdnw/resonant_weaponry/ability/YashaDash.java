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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class YashaDash {
    private static final Map<UUID, Dash> DASHES = new HashMap<>();
    private static final double DASH_SPEED = 1.6;

    private YashaDash() {
    }

    public static void start(ServerPlayer player) {
        ResonantWeaponryConfig.LegendaryWeapons.YashasEdge config =
                ResonantWeaponryConfig.legendaryWeapons.yashasEdge;
        Vec3 direction = new Vec3(player.getLookAngle().x, 0.0, player.getLookAngle().z);
        if (direction.lengthSqr() < 1.0E-4) {
            direction = Vec3.directionFromRotation(0.0f, player.getYRot());
        }
        direction = direction.normalize();

        player.setNoGravity(true);
        player.resetFallDistance();
        player.setDeltaMovement(direction.scale(DASH_SPEED));
        player.hurtMarked = true;
        DASHES.put(player.getUUID(), new Dash(direction, config.dashTicks));
        DebugLog.log("Yasha's Vengeance: dash started for {}", player.getName().getString());
    }

    public static void tick(MinecraftServer server) {
        ResonantWeaponryConfig.LegendaryWeapons.YashasEdge config =
                ResonantWeaponryConfig.legendaryWeapons.yashasEdge;
        for (UUID id : List.copyOf(DASHES.keySet())) {
            Dash dash = DASHES.get(id);
            if (dash == null) {
                continue;
            }
            ServerPlayer player = server.getPlayerList().getPlayer(id);
            if (player == null) {
                DASHES.remove(id);
                continue;
            }
            ServerLevel level = player.level();

            player.setDeltaMovement(dash.direction.scale(DASH_SPEED));
            player.resetFallDistance();
            player.hurtMarked = true;
            dash.path.add(player.position());

            Vec3 front = player.position().add(dash.direction.scale(2.0));
            AABB area = new AABB(front.x - 2.5, front.y - 1.5, front.z - 2.5, front.x + 2.5, front.y + 1.5, front.z + 2.5);
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
                if (!Targeting.canAffect(player, entity) || dash.hit.contains(entity.getUUID())) {
                    continue;
                }
                entity.invulnerableTime = 0;
                if (entity.hurtServer(level, level.damageSources().indirectMagic(player, player), config.dashDamage)) {
                    dash.hit.add(entity.getUUID());
                }
            }

            level.sendParticles(new DustParticleOptions(0x00DDDD, 1.5f),
                    player.getX(), player.getY() + 1.0, player.getZ(), 6, 0.3, 0.3, 0.3, 0.02);

            dash.ticksLeft--;
            if (dash.ticksLeft <= 0) {
                player.setNoGravity(false);
                DASHES.remove(id);
                scheduleAftercut(player, dash, config);
            }
        }
    }

    public static void cancel(ServerPlayer player) {
        if (DASHES.remove(player.getUUID()) != null) {
            player.setNoGravity(false);
        }
    }

    public static void clearAll() {
        DASHES.clear();
    }

    private static void scheduleAftercut(ServerPlayer owner, Dash dash,
                                         ResonantWeaponryConfig.LegendaryWeapons.YashasEdge config) {
        List<Vec3> path = List.copyOf(dash.path);
        Set<UUID> hitDuringDash = Set.copyOf(dash.hit);
        ServerLevel level = owner.level();
        Scheduler.schedule(level, config.aftercutDelay, resolved -> {
            if (owner.isRemoved() || resolved.getServer() == null
                    || resolved.getServer().getPlayerList().getPlayer(owner.getUUID()) == null) {
                DebugLog.log("Yasha aftercut: owner gone, skipped");
                return;
            }
            for (Vec3 point : path) {
                AABB area = new AABB(point.x - 5.0, point.y - 2.0, point.z - 5.0,
                        point.x + 5.0, point.y + 3.0, point.z + 5.0);
                for (LivingEntity entity : resolved.getEntitiesOfClass(LivingEntity.class, area)) {
                    if (!Targeting.canAffect(owner, entity)
                            || (!config.aftercutRehit && hitDuringDash.contains(entity.getUUID()))) {
                        continue;
                    }
                    entity.invulnerableTime = 0;
                    entity.hurtServer(resolved, resolved.damageSources().indirectMagic(owner, owner), config.aftercutDamage);
                }
                resolved.sendParticles(new DustParticleOptions(0x88FF88, 1.5f),
                        point.x, point.y + 1.0, point.z, 10, 0.5, 0.5, 0.5, 0.05);
                resolved.sendParticles(ParticleTypes.FIREWORK,
                        point.x, point.y + 1.0, point.z, 4, 0.3, 0.3, 0.3, 0.05);
            }
            if (!path.isEmpty()) {
                Vec3 middle = path.get(path.size() / 2);
                resolved.playSound(null, middle.x, middle.y, middle.z, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0f, 1.6f);
            }
            DebugLog.log("Yasha aftercut: {} points", path.size());
        });
    }

    private static final class Dash {
        private final Vec3 direction;
        private final List<Vec3> path = new ArrayList<>();
        private final Set<UUID> hit = new HashSet<>();
        private int ticksLeft;

        private Dash(Vec3 direction, int ticksLeft) {
            this.direction = direction;
            this.ticksLeft = ticksLeft;
        }
    }
}
