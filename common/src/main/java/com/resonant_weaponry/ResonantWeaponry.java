package com.resonant_weaponry;

import com.resonant_weaponry.config.ResonantWeaponryConfig;
import com.resonant_weaponry.registry.ModItems;

public final class ResonantWeaponry {
    public static final String MOD_ID = "resonant_weaponry";
    public static final String WEAPONRY_TAB_KEY = "itemGroup." + MOD_ID + ".weaponry_tab";

    private ResonantWeaponry() {
    }

    public static void init() {
        ResonantWeaponryConfig.init();
        ModItems.register();
    }
}
