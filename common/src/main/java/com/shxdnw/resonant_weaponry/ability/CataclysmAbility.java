package com.shxdnw.resonant_weaponry.ability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public final class CataclysmAbility implements LegendaryAbility {
    @Override
    public void activate(AbilityContext context) {
        ServerPlayer player = context.wielder();
        player.level().playSound(null, player.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM,
                SoundSource.PLAYERS, 4.0f, 0.5f);
        CataclysmLeap.start(player);
    }
}
