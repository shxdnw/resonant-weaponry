package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FirstHitTracker {
    private static final int MAX_ENTRIES = 1000;
    private static final int SWEEP_INTERVAL = 1200;
    private static final Map<UUID, Map<UUID, Long>> LAST_HITS = new HashMap<>();
    private static long tick;

    private FirstHitTracker() {
    }

    public static void tick() {
        tick++;
        if (tick % SWEEP_INTERVAL == 0) {
            sweep();
        }
    }

    public static boolean isFirstHit(Player attacker, LivingEntity target) {
        int window = ResonantWeaponryConfig.legendaryWeapons.theRealKnife.firstHitWindow;
        Long last = LAST_HITS.getOrDefault(attacker.getUUID(), Map.of()).get(target.getUUID());
        return last == null || tick - last > window;
    }

    public static void markHit(Player attacker, LivingEntity target) {
        LAST_HITS.computeIfAbsent(attacker.getUUID(), key -> new HashMap<>()).put(target.getUUID(), tick);
        if (entries() > MAX_ENTRIES) {
            sweep();
        }
    }

    public static void onTargetDeath(LivingEntity target) {
        for (Map<UUID, Long> targets : LAST_HITS.values()) {
            targets.remove(target.getUUID());
        }
    }

    public static void clear(Player player) {
        LAST_HITS.remove(player.getUUID());
    }

    public static void clearAll() {
        LAST_HITS.clear();
        tick = 0;
    }

    private static void sweep() {
        int window = ResonantWeaponryConfig.legendaryWeapons.theRealKnife.firstHitWindow;
        LAST_HITS.values().forEach(targets -> targets.entrySet().removeIf(entry -> tick - entry.getValue() > window));
        LAST_HITS.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    private static int entries() {
        return LAST_HITS.values().stream().mapToInt(Map::size).sum();
    }
}
