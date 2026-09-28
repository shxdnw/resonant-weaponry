package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class AntiTankPassive implements Passive {
    @Override
    public float modifyArmorPenetration(ServerPlayer player, LivingEntity target, float damage, float afterArmor) {
        ResonantWeaponryConfig.LegendaryWeapons.Calamity config =
                ResonantWeaponryConfig.legendaryWeapons.calamity;
        return afterArmor + (damage - afterArmor) * config.armorPenetration;
    }

    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmor()) {
                continue;
            }
            ItemStack armor = target.getItemBySlot(slot);
            if (!armor.isEmpty()) {
                armor.hurtAndBreak(1, target, slot);
            }
        }
    }
}
