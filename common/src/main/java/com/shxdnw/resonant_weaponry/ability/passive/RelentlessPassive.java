package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public final class RelentlessPassive implements Passive {
    @Override
    public float modifyArmorPenetration(ServerPlayer player, LivingEntity target, float damage, float afterArmor) {
        ResonantWeaponryConfig.LegendaryWeapons.TheRealKnife config =
                ResonantWeaponryConfig.legendaryWeapons.theRealKnife;
        if (player.getHealth() < player.getMaxHealth() * config.relentlessBelow) {
            DebugLog.log("Relentless: {} armor penetration for {}", config.armorPen, player.getName().getString());
            return afterArmor + (damage - afterArmor) * config.armorPen;
        }
        return afterArmor;
    }
}
