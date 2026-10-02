package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.ability.VoidStacks;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;

public final class EventHorizonPassive implements Passive {
    @Override
    public boolean nullifyIncoming(ServerPlayer player, DamageSource source) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        if (VoidStacks.get(player) < 1) {
            return false;
        }
        ResonantWeaponryConfig.LegendaryWeapons.Voidfang config =
                ResonantWeaponryConfig.legendaryWeapons.voidfang;
        if (player.getRandom().nextDouble() >= config.nullifyChance) {
            return false;
        }
        VoidStacks.decrement(player);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0f, 0.6f);
        player.level().sendParticles(new DustParticleOptions(0x440044, 2.0f),
                player.getX(), player.getY() + 1.0, player.getZ(), 30, 0.5, 0.8, 0.5, 0.05);
        DebugLog.log("Event Horizon: nullified damage for {}", player.getName().getString());
        return true;
    }
}
