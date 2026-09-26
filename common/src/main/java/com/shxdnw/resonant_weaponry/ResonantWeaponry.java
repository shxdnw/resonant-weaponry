package com.shxdnw.resonant_weaponry;

import com.shxdnw.resonant_weaponry.ability.Aftercuts;
import com.shxdnw.resonant_weaponry.ability.Channels;
import com.shxdnw.resonant_weaponry.ability.CombatHooks;
import com.shxdnw.resonant_weaponry.ability.CycloneAbility;
import com.shxdnw.resonant_weaponry.ability.ErasureAbility;
import com.shxdnw.resonant_weaponry.ability.ErasureFx;
import com.shxdnw.resonant_weaponry.ability.EviscerateAbility;
import com.shxdnw.resonant_weaponry.ability.FirstHitTracker;
import com.shxdnw.resonant_weaponry.ability.ImpenetrableDefenseAbility;
import com.shxdnw.resonant_weaponry.ability.LegendaryAbilities;
import com.shxdnw.resonant_weaponry.ability.MovementTracker;
import com.shxdnw.resonant_weaponry.ability.PassiveCooldowns;
import com.shxdnw.resonant_weaponry.ability.RuptureAbility;
import com.shxdnw.resonant_weaponry.ability.Scheduler;
import com.shxdnw.resonant_weaponry.ability.ServerClock;
import com.shxdnw.resonant_weaponry.ability.VoidStacks;
import com.shxdnw.resonant_weaponry.ability.passive.AftercutPassive;
import com.shxdnw.resonant_weaponry.ability.passive.CounterweightPassive;
import com.shxdnw.resonant_weaponry.ability.passive.EventHorizonPassive;
import com.shxdnw.resonant_weaponry.ability.passive.HemorrhagicShockPassive;
import com.shxdnw.resonant_weaponry.ability.passive.LegendaryPassives;
import com.shxdnw.resonant_weaponry.ability.passive.RelentlessPassive;
import com.shxdnw.resonant_weaponry.ability.passive.SanguinePassive;
import com.shxdnw.resonant_weaponry.ability.passive.ShowstopperPassive;
import com.shxdnw.resonant_weaponry.ability.passive.TailwindPassive;
import com.shxdnw.resonant_weaponry.ability.passive.TheFirstMovePassive;
import com.shxdnw.resonant_weaponry.ability.passive.VoidscarPassive;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import com.shxdnw.resonant_weaponry.registry.ModItems;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.server.level.ServerPlayer;

public final class ResonantWeaponry {
    public static final String MOD_ID = "resonant_weaponry";
    public static final String WEAPONRY_TAB_KEY = "itemGroup." + MOD_ID + ".weaponry_tab";

    private ResonantWeaponry() {
    }

    public static void init() {
        ResonantWeaponryConfig.init();

        LegendaryAbilities.register("the_real_knife", new ErasureAbility());
        LegendaryAbilities.register("gilded_arbiter", new ImpenetrableDefenseAbility());
        LegendaryAbilities.register("gale_cutter", new CycloneAbility());
        LegendaryAbilities.register("blood_scourge", new EviscerateAbility());
        LegendaryAbilities.register("voidfang", new RuptureAbility());

        LegendaryPassives.register("the_real_knife", new TheFirstMovePassive(), new RelentlessPassive());
        LegendaryPassives.register("gilded_arbiter", new ShowstopperPassive(), new CounterweightPassive());
        LegendaryPassives.register("gale_cutter", new TailwindPassive(), new AftercutPassive());
        LegendaryPassives.register("blood_scourge", new SanguinePassive(), new HemorrhagicShockPassive());
        LegendaryPassives.register("voidfang", new VoidscarPassive(), new EventHorizonPassive());

        TickEvent.SERVER_POST.register(server -> {
            ServerClock.tick();
            Channels.tick(server);
            ErasureFx.tick(server);
            Scheduler.tick(server);
            FirstHitTracker.tick();
            MovementTracker.tick(server);
        });
        PlayerEvent.PLAYER_QUIT.register(ResonantWeaponry::clearState);
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayer dying) {
                clearState(dying);
            }
            if (source.getEntity() instanceof ServerPlayer killer) {
                CombatHooks.onKill(killer, entity);
            }
            return EventResult.pass();
        });

        ModItems.register();
    }

    private static void clearState(ServerPlayer player) {
        FirstHitTracker.clear(player);
        Channels.clear(player);
        MovementTracker.forget(player);
        PassiveCooldowns.forget(player);
        Aftercuts.forget(player);
        VoidStacks.forget(player);
    }
}
