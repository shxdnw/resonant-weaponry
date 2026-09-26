package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.ability.VoidStacks;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public final class VoidscarPassive implements Passive {
    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        ResonantWeaponryConfig.LegendaryWeapons.Voidfang config =
                ResonantWeaponryConfig.legendaryWeapons.voidfang;
        int stacks = VoidStacks.increment(player);
        DebugLog.log("Voidscar: {} stacks", stacks);
        if (stacks < config.maxStacks) {
            return;
        }
        VoidStacks.reset(player);

        ServerLevel level = player.level();
        level.playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1.0f, 1.5f);
        target.invulnerableTime = 0;
        target.hurtServer(level, level.damageSources().indirectMagic(player, player), config.trueDamage);
        level.sendParticles(new DustParticleOptions(0x8800CC, 2.0f),
                target.getX(), target.getY() + 1.0, target.getZ(), 30, 0.5, 0.5, 0.5, 0.05);
        level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                target.getX(), target.getY() + 1.0, target.getZ(), 20, 0.4, 0.6, 0.4, 0.1);
        DebugLog.log("Voidscar: true damage dealt to {}", target.getName().getString());
    }
}
