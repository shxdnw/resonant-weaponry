package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Aftercuts {
    private static final Map<UUID, Integer> STACKS = new HashMap<>();

    private Aftercuts() {
    }

    public static void onHit(ServerPlayer player, LivingEntity target) {
        ResonantWeaponryConfig.LegendaryWeapons.GaleCutter config =
                ResonantWeaponryConfig.legendaryWeapons.galeCutter;
        int stacks = STACKS.merge(player.getUUID(), 1, Integer::sum);
        DebugLog.log("Aftercut: {} stacks for {}", stacks, player.getName().getString());
        if (stacks < config.aftercutMaxStacks) {
            return;
        }
        STACKS.put(player.getUUID(), 0);
        DebugLog.log("Aftercut: scheduled against {} in {} ticks", target.getName().getString(), config.aftercutDelay);
        LivingEntity owner = player;
        ServerLevel level = player.level();
        Scheduler.schedule(level, config.aftercutDelay, resolved -> execute(owner, target, resolved, config));
    }

    private static void execute(LivingEntity owner, LivingEntity target, ServerLevel level,
                                ResonantWeaponryConfig.LegendaryWeapons.GaleCutter config) {
        // owner might be gone by now
        if (owner.isRemoved() || level.getServer() == null
                || owner.level() != level
                || level.getServer().getPlayerList().getPlayer(owner.getUUID()) == null) {
            DebugLog.log("Aftercut: owner gone, skipped");
            return;
        }
        if (target.isRemoved() || !target.isAlive()) {
            DebugLog.log("Aftercut: target already gone, skipped");
            return;
        }
        DebugLog.log("Aftercut: hit {} for {}", target.getName().getString(), config.aftercutDamage);
        target.invulnerableTime = 0;
        target.hurtServer(level, level.damageSources().indirectMagic(owner, owner), config.aftercutDamage);

        Vec3 away = target.position().subtract(owner.position());
        Vec3 horizontal = new Vec3(away.x, 0.0, away.z);
        if (horizontal.lengthSqr() > 1.0E-4) {
            Vec3 direction = horizontal.normalize();
            target.push(direction.x * 4.0, 1.0, direction.z * 4.0);
            target.hurtMarked = true;
        }

        level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0f, 1.6f);
        level.sendParticles(new DustParticleOptions(0x00DD66, 1.5f),
                target.getX(), target.getY() + 1.0, target.getZ(), 30, 0.5, 0.5, 0.5, 0.05);
    }

    public static void forget(Player player) {
        STACKS.remove(player.getUUID());
    }

    public static void clearAll() {
        STACKS.clear();
    }
}
