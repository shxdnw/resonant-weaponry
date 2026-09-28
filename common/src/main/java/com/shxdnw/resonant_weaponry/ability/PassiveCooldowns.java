package com.shxdnw.resonant_weaponry.ability;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PassiveCooldowns {
    private static final Map<UUID, Map<String, Long>> LAST_USED = new HashMap<>();

    private PassiveCooldowns() {
    }

    public static boolean ready(Player player, String key, int cooldownTicks) {
        Map<String, Long> cooldowns = LAST_USED.computeIfAbsent(player.getUUID(), id -> new HashMap<>());
        Long last = cooldowns.get(key);
        long now = ServerClock.now();
        if (last != null && now - last < cooldownTicks) {
            return false;
        }
        cooldowns.put(key, now);
        return true;
    }

    public static void forget(Player player) {
        LAST_USED.remove(player.getUUID());
    }

    public static void clearAll() {
        LAST_USED.clear();
    }
}
