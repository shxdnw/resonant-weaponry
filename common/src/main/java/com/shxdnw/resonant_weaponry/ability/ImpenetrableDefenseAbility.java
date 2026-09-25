package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class ImpenetrableDefenseAbility implements LegendaryAbility {
    @Override
    public void activate(AbilityContext context) {
        ServerPlayer player = context.wielder();
        ResonantWeaponryConfig.LegendaryWeapons.GildedArbiter config =
                ResonantWeaponryConfig.legendaryWeapons.gildedArbiter;

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
        player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, config.defenseSlow, 0));
        player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, config.defenseResistTime, config.defenseResistLevel));
        DebugLog.log("Impenetrable Defense activated for {}", player.getName().getString());
    }
}
