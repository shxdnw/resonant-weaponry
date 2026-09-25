package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.ability.PassiveCooldowns;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public final class TailwindPassive implements Passive {
    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        ResonantWeaponryConfig.LegendaryWeapons.GaleCutter config =
                ResonantWeaponryConfig.legendaryWeapons.galeCutter;
        if (PassiveCooldowns.ready(player, "gale_cutter:tailwind", config.tailwindCooldown)) {
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, config.tailwindSpeedDuration, 1));
            DebugLog.log("Tailwind: speed applied to {}", player.getName().getString());
        }
    }
}
