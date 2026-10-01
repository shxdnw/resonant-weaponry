package com.shxdnw.resonant_weaponry.content;

import java.util.function.BooleanSupplier;

public final class ClientTooltipState {
    // set by the loader client entrypoint
    private static BooleanSupplier shiftDown = () -> false;

    private ClientTooltipState() {
    }

    public static void setShiftDown(BooleanSupplier supplier) {
        shiftDown = supplier;
    }

    public static boolean shiftDown() {
        return shiftDown.getAsBoolean();
    }
}
