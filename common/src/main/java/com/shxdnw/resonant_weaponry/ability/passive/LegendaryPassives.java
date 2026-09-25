package com.shxdnw.resonant_weaponry.ability.passive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LegendaryPassives {
    private static final Map<String, List<Passive>> PASSIVES = new HashMap<>();

    private LegendaryPassives() {
    }

    public static void register(String weaponId, Passive... passives) {
        PASSIVES.put(weaponId, List.of(passives));
    }

    public static List<Passive> of(String weaponId) {
        return PASSIVES.getOrDefault(weaponId, List.of());
    }
}
