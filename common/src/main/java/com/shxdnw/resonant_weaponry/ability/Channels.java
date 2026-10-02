package com.shxdnw.resonant_weaponry.ability;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class Channels {
    private static final List<Channel> CHANNELS = new ArrayList<>();

    private Channels() {
    }

    public static void start(ServerPlayer player, int durationTicks, IntConsumer onTick, Consumer<ServerPlayer> onComplete) {
        CHANNELS.removeIf(channel -> channel.playerId.equals(player.getUUID()));
        CHANNELS.add(new Channel(player.getUUID(), durationTicks, onTick, onComplete));
    }

    public static void clear(ServerPlayer player) {
        CHANNELS.removeIf(channel -> channel.playerId.equals(player.getUUID()));
    }

    public static void clearAll() {
        CHANNELS.clear();
    }

    public static boolean isChanneling(ServerPlayer player) {
        return CHANNELS.stream().anyMatch(channel -> channel.playerId.equals(player.getUUID()));
    }

    public static void tick(MinecraftServer server) {
        for (Channel channel : List.copyOf(CHANNELS)) {
            ServerPlayer player = server.getPlayerList().getPlayer(channel.playerId);
            if (player == null) {
                CHANNELS.remove(channel);
                continue;
            }
            channel.onTick.accept(channel.elapsed);
            channel.elapsed++;
            if (channel.elapsed >= channel.durationTicks) {
                CHANNELS.remove(channel);
                channel.onComplete.accept(player);
            }
        }
    }

    private static final class Channel {
        private final UUID playerId;
        private final int durationTicks;
        private final IntConsumer onTick;
        private final Consumer<ServerPlayer> onComplete;
        private int elapsed;

        private Channel(UUID playerId, int durationTicks, IntConsumer onTick, Consumer<ServerPlayer> onComplete) {
            this.playerId = playerId;
            this.durationTicks = durationTicks;
            this.onTick = onTick;
            this.onComplete = onComplete;
        }
    }
}
