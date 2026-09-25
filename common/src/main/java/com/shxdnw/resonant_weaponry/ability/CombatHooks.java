package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import com.shxdnw.resonant_weaponry.content.LegendaryWeaponItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class CombatHooks {
    private CombatHooks() {
    }

    public static float modifyDamage(LivingEntity target, DamageSource source, float amount) {
        if (!(source.getEntity() instanceof Player player)) {
            return amount;
        }
        String weapon = weaponId(player);
        if (weapon == null) {
            return amount;
        }
        if (weapon.equals("the_real_knife") && FirstHitTracker.isFirstHit(player, target)) {
            DebugLog.log("First Move: doubling damage against {}", target.getName().getString());
            return amount * 2.0f;
        }
        if (weapon.equals("gilded_arbiter") && (target.isSprinting() || !target.onGround())) {
            float multiplier = ResonantWeaponryConfig.legendaryWeapons.gildedArbiter.showstopperMult;
            DebugLog.log("Showstopper: {}x damage against {}", multiplier, target.getName().getString());
            return amount * multiplier;
        }
        return amount;
    }

    public static float armorPenetration(DamageSource source, float damage, float afterArmor) {
        if (!(source.getEntity() instanceof Player player) || !"the_real_knife".equals(weaponId(player))) {
            return afterArmor;
        }
        ResonantWeaponryConfig.LegendaryWeapons.TheRealKnife config =
                ResonantWeaponryConfig.legendaryWeapons.theRealKnife;
        if (player.getHealth() < player.getMaxHealth() * config.relentlessBelow) {
            DebugLog.log("Relentless: {} armor penetration for {}", config.armorPen, player.getName().getString());
            return afterArmor + (damage - afterArmor) * config.armorPen;
        }
        return afterArmor;
    }

    public static void onHit(Player player, LivingEntity target) {
        String weapon = weaponId(player);
        if ("gilded_arbiter".equals(weapon)) {
            ResonantWeaponryConfig.LegendaryWeapons.GildedArbiter config =
                    ResonantWeaponryConfig.legendaryWeapons.gildedArbiter;
            if (target instanceof Enemy) {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, config.witherTime, config.witherLevel));
                DebugLog.log("Showstopper: wither applied to {}", target.getName().getString());
            }
            boolean backward = MovementTracker.isMovingBackward(player);
            DebugLog.log("Counterweight check for {}: backward={}", player.getName().getString(), backward);
            if (backward) {
                target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, config.counterSlowTime, 0));
                DebugLog.log("Counterweight: slowness applied to {}", target.getName().getString());
            }
            return;
        }
        if ("gale_cutter".equals(weapon) && player instanceof ServerPlayer serverPlayer) {
            ResonantWeaponryConfig.LegendaryWeapons.GaleCutter config =
                    ResonantWeaponryConfig.legendaryWeapons.galeCutter;
            if (PassiveCooldowns.ready(serverPlayer, "gale_cutter:tailwind", config.tailwindCooldown)) {
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.SPEED, config.tailwindSpeedDuration, 1));
                DebugLog.log("Tailwind: speed applied to {}", serverPlayer.getName().getString());
            }
            Aftercuts.onHit(serverPlayer, target);
        }
    }

    private static String weaponId(Player player) {
        ItemStack stack = player.getMainHandItem();
        return stack.getItem() instanceof LegendaryWeaponItem item ? item.definition().id() : null;
    }
}
