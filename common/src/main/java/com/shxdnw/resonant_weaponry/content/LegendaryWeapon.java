package com.shxdnw.resonant_weaponry.content;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import net.minecraft.ChatFormatting;

import java.util.List;

public record LegendaryWeapon(
        String id,
        String displayName,
        WeaponType archetype,
        ChatFormatting nameColor,
        float attackDamage,
        float attackSpeed,
        Activation activation,
        Text text) {

    public String nameKey() {
        return "item." + ResonantWeaponry.MOD_ID + "." + id;
    }

    public record Text(List<String> lore, String abilityName, String abilityDescription, List<Passive> passives) {
    }

    public record Passive(String name, String description) {
    }

    public record Activation(ActivationStyle style, int cooldownTicks, int chargeTicks, int channelTicks) {
        public static Activation instant(int cooldownTicks) {
            return new Activation(ActivationStyle.INSTANT, cooldownTicks, 0, 0);
        }

        public static Activation charged(int cooldownTicks, int chargeTicks) {
            return new Activation(ActivationStyle.CHARGED, cooldownTicks, chargeTicks, 0);
        }

        public static Activation channeled(int cooldownTicks, int chargeTicks, int channelTicks) {
            return new Activation(ActivationStyle.CHANNELED, cooldownTicks, chargeTicks, channelTicks);
        }
    }
}
