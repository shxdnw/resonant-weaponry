package com.shxdnw.resonant_weaponry.ability;

public final class ServerClock {
    private static long tick;

    private ServerClock() {
    }

    public static void tick() {
        tick++;
    }

    public static long now() {
        return tick;
    }

    public static void reset() {
        tick = 0;
    }
}
