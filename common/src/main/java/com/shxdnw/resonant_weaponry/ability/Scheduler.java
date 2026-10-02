package com.shxdnw.resonant_weaponry.ability;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// delayed effects, ticked each server tick
public final class Scheduler {
    private static final List<Task> TASKS = new ArrayList<>();

    private Scheduler() {
    }

    public static void schedule(ServerLevel level, int delayTicks, Consumer<ServerLevel> action) {
        TASKS.add(new Task(level, ServerClock.now() + delayTicks, action));
    }

    public static void tick(MinecraftServer server) {
        long now = ServerClock.now();
        for (Task task : List.copyOf(TASKS)) {
            if (now >= task.resolveTick) {
                TASKS.remove(task);
                // skip if the server is going down
                if (task.level.getServer() != null) {
                    task.action.accept(task.level);
                }
            }
        }
    }

    public static void clear() {
        TASKS.clear();
    }

    private static final class Task {
        private final ServerLevel level;
        private final long resolveTick;
        private final Consumer<ServerLevel> action;

        private Task(ServerLevel level, long resolveTick, Consumer<ServerLevel> action) {
            this.level = level;
            this.resolveTick = resolveTick;
            this.action = action;
        }
    }
}
