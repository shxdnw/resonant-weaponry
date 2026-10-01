package com.shxdnw.resonant_weaponry.fabric;

import com.shxdnw.resonant_weaponry.content.ClientTooltipState;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;

// only place allowed to touch Minecraft
public final class ResonantWeaponryFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTooltipState.setShiftDown(() -> Minecraft.getInstance().hasShiftDown());
    }
}
