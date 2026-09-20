package com.shxdnw.resonant_weaponry.fabric;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import net.fabricmc.api.ModInitializer;

public final class ResonantWeaponryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ResonantWeaponry.init();
    }
}
