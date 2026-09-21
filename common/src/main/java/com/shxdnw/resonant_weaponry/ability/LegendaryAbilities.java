package com.shxdnw.resonant_weaponry.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class LegendaryAbilities {
    private static final Map<String, LegendaryAbility> ABILITIES = new HashMap<>();

    private LegendaryAbilities() {
    }

    public static void register(String weaponId, LegendaryAbility ability) {
        ABILITIES.put(weaponId, ability);
    }

    public static Optional<LegendaryAbility> abilityFor(String weaponId) {
        return Optional.ofNullable(ABILITIES.get(weaponId));
    }
}
