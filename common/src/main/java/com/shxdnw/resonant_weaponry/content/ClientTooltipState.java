package com.shxdnw.resonant_weaponry.content;

import net.minecraft.client.Minecraft;

public final class ClientTooltipState {
    private ClientTooltipState() {
    }

    public static boolean shiftDown() {
        return Minecraft.getInstance().hasShiftDown();
    }
}
