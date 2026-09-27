package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.ability.PassiveCooldowns;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public final class AnchorPassive implements Passive {
    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        ResonantWeaponryConfig.LegendaryWeapons.YashasEdge config =
                ResonantWeaponryConfig.legendaryWeapons.yashasEdge;
        if (PassiveCooldowns.ready(player, "yashas_edge:anchor", config.anchorCooldown)) {
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, config.anchorDuration, config.anchorLevel));
            DebugLog.log("Anchor: slowness applied to {}", target.getName().getString());
        }
    }
}
