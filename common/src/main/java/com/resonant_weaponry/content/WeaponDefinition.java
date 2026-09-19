package com.resonant_weaponry.content;

import com.resonant_weaponry.ResonantWeaponry;

public record WeaponDefinition(String id, MaterialTier tier, WeaponType type) {
    public static String idOf(MaterialTier tier, WeaponType type) {
        return tier.id() + "_" + type.id();
    }

    public String nameKey() {
        return "item." + ResonantWeaponry.MOD_ID + "." + id;
    }
}
