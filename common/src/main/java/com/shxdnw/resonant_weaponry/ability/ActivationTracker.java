package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.content.ActivationStyle;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class ActivationTracker {
    private static final Map<UUID, ActivationSession> ACTIVE = new HashMap<>();

    private ActivationTracker() {
    }

    public static ActivationSession start(ServerPlayer player, ItemStack stack, InteractionHand hand,
                                          ActivationStyle style, ActivationState state) {
        ActivationSession session = new ActivationSession(stack, hand, style, state);
        ACTIVE.put(player.getUUID(), session);
        return session;
    }

    public static void setState(ServerPlayer player, ActivationState state) {
        Optional.ofNullable(ACTIVE.get(player.getUUID()))
                .map(current -> new ActivationSession(current.stack(), current.hand(), current.style(), state))
                .ifPresent(next -> ACTIVE.put(player.getUUID(), next));
    }

    public static Optional<ActivationSession> get(ServerPlayer player) {
        return Optional.ofNullable(ACTIVE.get(player.getUUID()));
    }

    public static boolean isActive(ServerPlayer player) {
        return ACTIVE.containsKey(player.getUUID());
    }

    public static void clear(ServerPlayer player) {
        ACTIVE.remove(player.getUUID());
    }

    public record ActivationSession(ItemStack stack, InteractionHand hand, ActivationStyle style, ActivationState state) {
    }
}
