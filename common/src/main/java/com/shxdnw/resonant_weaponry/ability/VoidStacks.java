package com.shxdnw.resonant_weaponry.ability;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class VoidStacks {
    private static final Map<UUID, Integer> STACKS = new HashMap<>();

    private VoidStacks() {
    }

    public static int increment(Player player) {
        return STACKS.merge(player.getUUID(), 1, Integer::sum);
    }

    public static int get(Player player) {
        return STACKS.getOrDefault(player.getUUID(), 0);
    }

    public static void reset(Player player) {
        STACKS.put(player.getUUID(), 0);
    }

    public static void forget(Player player) {
        STACKS.remove(player.getUUID());
    }

    public static void clearAll() {
        STACKS.clear();
    }
}
