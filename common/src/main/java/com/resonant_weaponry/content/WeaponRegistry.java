package com.resonant_weaponry.content;

import java.util.ArrayList;
import java.util.List;

public final class WeaponRegistry {
    public static final List<WeaponDefinition> STANDARD_WEAPONS = buildStandardWeapons();

    private WeaponRegistry() {
    }

    private static List<WeaponDefinition> buildStandardWeapons() {
        List<WeaponDefinition> weapons = new ArrayList<>();
        for (MaterialTier tier : MaterialTier.values()) {
            for (WeaponType type : WeaponType.values()) {
                weapons.add(new WeaponDefinition(WeaponDefinition.idOf(tier, type), tier, type));
            }
        }
        return List.copyOf(weapons);
    }
}
