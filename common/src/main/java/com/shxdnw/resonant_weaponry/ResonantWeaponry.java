package com.shxdnw.resonant_weaponry;

import com.shxdnw.resonant_weaponry.ability.Channels;
import com.shxdnw.resonant_weaponry.ability.ErasureAbility;
import com.shxdnw.resonant_weaponry.ability.ErasureFx;
import com.shxdnw.resonant_weaponry.ability.FirstHitTracker;
import com.shxdnw.resonant_weaponry.ability.LegendaryAbilities;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import com.shxdnw.resonant_weaponry.registry.ModItems;
import dev.architectury.event.events.common.TickEvent;

public final class ResonantWeaponry {
    public static final String MOD_ID = "resonant_weaponry";
    public static final String WEAPONRY_TAB_KEY = "itemGroup." + MOD_ID + ".weaponry_tab";

    private ResonantWeaponry() {
    }

    public static void init() {
        ResonantWeaponryConfig.init();
        LegendaryAbilities.register("the_real_knife", new ErasureAbility());
        TickEvent.SERVER_POST.register(server -> {
            Channels.tick(server);
            ErasureFx.tick(server);
            FirstHitTracker.tick();
        });
        ModItems.register();
    }
}
