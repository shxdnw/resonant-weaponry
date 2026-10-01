package com.shxdnw.resonant_weaponry.neoforge;

import com.shxdnw.resonant_weaponry.content.ClientTooltipState;
import net.minecraft.client.Minecraft;

// only place allowed to touch Minecraft
public final class ResonantWeaponryNeoForgeClient {
    private ResonantWeaponryNeoForgeClient() {
    }

    public static void init() {
        ClientTooltipState.setShiftDown(() -> Minecraft.getInstance().hasShiftDown());
    }
}
