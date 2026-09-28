package com.shxdnw.resonant_weaponry.ability;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MovementTracker {
    private static final Map<UUID, Vec3> LAST_POSITION = new HashMap<>();
    private static final Map<UUID, Vec3> LAST_MOTION = new HashMap<>();

    private MovementTracker() {
    }

    public static void tick(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            Vec3 current = player.position();
            Vec3 previous = LAST_POSITION.put(player.getUUID(), current);
            if (previous != null) {
                LAST_MOTION.put(player.getUUID(), new Vec3(current.x - previous.x, 0.0, current.z - previous.z));
            }
        }
    }

    public static boolean isMovingBackward(Player player) {
        Vec3 motion = LAST_MOTION.get(player.getUUID());
        if (motion == null) {
            return false;
        }
        if (motion.x * motion.x + motion.z * motion.z < 1.0E-6) {
            return false;
        }
        double lookX = player.getLookAngle().x;
        double lookZ = player.getLookAngle().z;
        double lookLength = Math.sqrt(lookX * lookX + lookZ * lookZ);
        if (lookLength < 1.0E-4) {
            return false;
        }
        return (motion.x * lookX + motion.z * lookZ) / lookLength < -0.005;
    }

    public static void forget(Player player) {
        LAST_POSITION.remove(player.getUUID());
        LAST_MOTION.remove(player.getUUID());
    }

    public static void clearAll() {
        LAST_POSITION.clear();
        LAST_MOTION.clear();
    }
}
