package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.ability.PassiveCooldowns;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;

public final class SanguinePassive implements Passive {
    @Override
    public void onKill(ServerPlayer player, LivingEntity target) {
        if (!(target instanceof Enemy)) {
            return;
        }
        ResonantWeaponryConfig.LegendaryWeapons.BloodScourge config =
                ResonantWeaponryConfig.legendaryWeapons.bloodScourge;
        if (!PassiveCooldowns.ready(player, "blood_scourge:sanguine", config.sanguineCooldown)) {
            return;
        }
        player.heal(player.getMaxHealth() * config.sanguineHealPct);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.6f, 1.8f);
        DebugLog.log("Sanguine: healed {}", player.getName().getString());
    }
}
