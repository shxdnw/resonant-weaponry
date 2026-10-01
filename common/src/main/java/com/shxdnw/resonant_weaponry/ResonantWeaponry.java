package com.shxdnw.resonant_weaponry;

import com.shxdnw.resonant_weaponry.ability.Aftercuts;
import com.shxdnw.resonant_weaponry.ability.CataclysmAbility;
import com.shxdnw.resonant_weaponry.ability.CataclysmLeap;
import com.shxdnw.resonant_weaponry.ability.Channels;
import com.shxdnw.resonant_weaponry.ability.CombatHooks;
import com.shxdnw.resonant_weaponry.ability.CycloneAbility;
import com.shxdnw.resonant_weaponry.ability.ErasureAbility;
import com.shxdnw.resonant_weaponry.ability.ErasureFx;
import com.shxdnw.resonant_weaponry.ability.EviscerateAbility;
import com.shxdnw.resonant_weaponry.ability.FirstHitTracker;
import com.shxdnw.resonant_weaponry.ability.ImpenetrableDefenseAbility;
import com.shxdnw.resonant_weaponry.ability.LegendaryAbilities;
import com.shxdnw.resonant_weaponry.ability.Momentum;
import com.shxdnw.resonant_weaponry.ability.MovementTracker;
import com.shxdnw.resonant_weaponry.ability.PassiveCooldowns;
import com.shxdnw.resonant_weaponry.ability.RuptureAbility;
import com.shxdnw.resonant_weaponry.ability.Scheduler;
import com.shxdnw.resonant_weaponry.ability.ServerClock;
import com.shxdnw.resonant_weaponry.ability.VoidStacks;
import com.shxdnw.resonant_weaponry.ability.YashaDash;
import com.shxdnw.resonant_weaponry.ability.YashasVengeanceAbility;
import com.shxdnw.resonant_weaponry.ability.passive.AftercutPassive;
import com.shxdnw.resonant_weaponry.ability.passive.AnchorPassive;
import com.shxdnw.resonant_weaponry.ability.passive.AntiTankPassive;
import com.shxdnw.resonant_weaponry.ability.passive.CounterweightPassive;
import com.shxdnw.resonant_weaponry.ability.passive.EventHorizonPassive;
import com.shxdnw.resonant_weaponry.ability.passive.HemorrhagicShockPassive;
import com.shxdnw.resonant_weaponry.ability.passive.LegendaryPassives;
import com.shxdnw.resonant_weaponry.ability.passive.MomentumPassive;
import com.shxdnw.resonant_weaponry.ability.passive.RelentlessPassive;
import com.shxdnw.resonant_weaponry.ability.passive.RuinationPassive;
import com.shxdnw.resonant_weaponry.ability.passive.SanguinePassive;
import com.shxdnw.resonant_weaponry.ability.passive.ShowstopperPassive;
import com.shxdnw.resonant_weaponry.ability.passive.TailwindPassive;
import com.shxdnw.resonant_weaponry.ability.passive.TheFirstMovePassive;
import com.shxdnw.resonant_weaponry.ability.passive.VoidscarPassive;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import com.shxdnw.resonant_weaponry.registry.ModItems;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.server.MinecraftServer;
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
        LegendaryAbilities.register("yashas_edge", new YashasVengeanceAbility());
        LegendaryAbilities.register("calamity", new CataclysmAbility());

        LegendaryPassives.register("the_real_knife", new TheFirstMovePassive(), new RelentlessPassive());
        LegendaryPassives.register("gilded_arbiter", new ShowstopperPassive(), new CounterweightPassive());
        LegendaryPassives.register("gale_cutter", new TailwindPassive(), new AftercutPassive());
        LegendaryPassives.register("blood_scourge", new SanguinePassive(), new HemorrhagicShockPassive());
        LegendaryPassives.register("voidfang", new VoidscarPassive(), new EventHorizonPassive());
        LegendaryPassives.register("yashas_edge", new MomentumPassive(), new AnchorPassive());
        LegendaryPassives.register("calamity", new AntiTankPassive(), new RuinationPassive());

        TickEvent.SERVER_POST.register(server -> {
            ServerClock.tick();
            Channels.tick(server);
            ErasureFx.tick(server);
            Scheduler.tick(server);
            FirstHitTracker.tick();
            MovementTracker.tick(server);
            YashaDash.tick(server);
            Momentum.tick(server);
            CataclysmLeap.tick(server);
        });
        PlayerEvent.PLAYER_QUIT.register(ResonantWeaponry::clearState);
        PlayerEvent.CHANGE_DIMENSION.register((player, from, to) -> clearTransient(player));
        LifecycleEvent.SERVER_STOPPING.register(ResonantWeaponry::clearServerState);
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayer dying) {
                clearState(dying);
            }
            FirstHitTracker.onTargetDeath(entity);
            CombatHooks.onKill(source, entity);
            return EventResult.pass();
        });

        ModItems.register();
    }

    // dimension change keeps cooldowns
    private static void clearTransient(ServerPlayer player) {
        FirstHitTracker.clear(player);
        Channels.clear(player);
        MovementTracker.forget(player);
        Aftercuts.forget(player);
        VoidStacks.forget(player);
        YashaDash.cancel(player);
        Momentum.forget(player);
        CataclysmLeap.cancel(player);
    }

    // death/logout wipes everything
    private static void clearState(ServerPlayer player) {
        clearTransient(player);
        PassiveCooldowns.forget(player);
    }

    // or static maps leak between worlds
    private static void clearServerState(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            clearState(player);
        }
        Scheduler.clear();
        ServerClock.reset();
        Channels.clearAll();
        ErasureFx.clearAll();
        Aftercuts.clearAll();
        VoidStacks.clearAll();
        FirstHitTracker.clearAll();
        MovementTracker.clearAll();
        PassiveCooldowns.clearAll();
        Momentum.clearAll();
        YashaDash.clearAll();
        CataclysmLeap.clearAll();
    }
}
