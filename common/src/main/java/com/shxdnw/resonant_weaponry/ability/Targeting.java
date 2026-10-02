package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;

public final class Targeting {
    private Targeting() {
    }

    public static boolean canAffect(Player owner, LivingEntity target) {
        if (target == owner) {
            return false;
        }
        ResonantWeaponryConfig.General general = ResonantWeaponryConfig.general;
        if (general != null && general.friendlyFire) {
            return true;
        }
        if (target.isAlliedTo(owner)) {
            return false;
        }
        if (target instanceof OwnableEntity ownable) {
            LivingEntity petOwner = ownable.getOwner();
            if (petOwner != null && owner.isAlliedTo(petOwner)) {
                return false;
            }
        }
        if (target instanceof Player) {
            return !(owner.level() instanceof ServerLevel level && !level.isPvpAllowed());
        }
        return true;
    }
}
