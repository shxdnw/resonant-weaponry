package com.shxdnw.resonant_weaponry.neoforge;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(ResonantWeaponry.MOD_ID)
public final class ResonantWeaponryNeoForge {
    public ResonantWeaponryNeoForge() {
        ResonantWeaponry.init();
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            ResonantWeaponryNeoForgeClient.init();
        }
    }
}
