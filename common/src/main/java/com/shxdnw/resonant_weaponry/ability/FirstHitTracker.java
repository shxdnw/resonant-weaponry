package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FirstHitTracker {
    private static final Map<UUID, Map<UUID, Long>> LAST_HITS = new HashMap<>();
    private static long tick;

    private FirstHitTracker() {
    }

    public static void tick() {
        tick++;
    }

    public static boolean isFirstHit(Player attacker, LivingEntity target) {
        int window = ResonantWeaponryConfig.legendaryWeapons.theRealKnife.firstHitWindow;
        Map<UUID, Long> targets = LAST_HITS.computeIfAbsent(attacker.getUUID(), key -> new HashMap<>());
        Long last = targets.get(target.getUUID());
        boolean first = last == null || tick - last > window;
        targets.put(target.getUUID(), tick);
        return first;
    }

    public static void clear(Player player) {
        LAST_HITS.remove(player.getUUID());
    }

    public static void clearAll() {
        LAST_HITS.clear();
        tick = 0;
    }
}
