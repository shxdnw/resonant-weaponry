package com.shxdnw.resonant_weaponry.ability;

import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ArmorPierce {
    private static final Map<UUID, Float> ACTIVE = new HashMap<>();

    private ArmorPierce() {
    }

    public static void set(Entity attacker, float fraction) {
        ACTIVE.put(attacker.getUUID(), fraction);
    }

    public static void clear(Entity attacker) {
        ACTIVE.remove(attacker.getUUID());
    }

    public static float fraction(Entity attacker) {
        return ACTIVE.getOrDefault(attacker.getUUID(), 0.0f);
    }

    public static void clearAll() {
        ACTIVE.clear();
    }
}
