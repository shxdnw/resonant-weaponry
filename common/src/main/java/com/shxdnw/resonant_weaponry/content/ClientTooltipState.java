package com.shxdnw.resonant_weaponry.content;

import java.util.function.BooleanSupplier;

public final class ClientTooltipState {
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
