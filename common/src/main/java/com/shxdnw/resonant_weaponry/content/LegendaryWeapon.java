package com.shxdnw.resonant_weaponry.content;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import net.minecraft.ChatFormatting;

import java.util.List;

public record LegendaryWeapon(
        String id,
        String displayName,
        WeaponType archetype,
        ChatFormatting nameColor,
        Activation activation,
        Text text) {

    public String nameKey() {
        return "item." + ResonantWeaponry.MOD_ID + "." + id;
    }

    public record Text(List<String> lore, String abilityName, String abilityDescription, List<Passive> passives) {
    }

    public record Passive(String name, String description) {
    }

    // timings come from config, only the style lives here
    public record Activation(ActivationStyle style) {
        public static Activation instant() {
            return new Activation(ActivationStyle.INSTANT);
        }

        public static Activation charged() {
            return new Activation(ActivationStyle.CHARGED);
        }

        public static Activation channeled() {
            return new Activation(ActivationStyle.CHANNELED);
        }
    }
}
