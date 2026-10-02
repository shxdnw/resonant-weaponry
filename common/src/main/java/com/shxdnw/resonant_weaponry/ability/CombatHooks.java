package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.ability.passive.LegendaryPassives;
import com.shxdnw.resonant_weaponry.ability.passive.Passive;
import com.shxdnw.resonant_weaponry.content.LegendaryWeaponItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class CombatHooks {
    // stops passives looping on their own damage
    private static boolean dispatching;
    private static final Map<UUID, Long> LAST_ON_HIT_TICK = new HashMap<>();
    private static final Map<UUID, Long> LAST_ON_KILL_TICK = new HashMap<>();

    private CombatHooks() {
    }

    public static float modifyDamage(LivingEntity target, DamageSource source, float amount) {
        ServerPlayer player = meleeAttacker(source);
        if (player == null || dispatching) {
            return amount;
        }
        dispatching = true;
        try {
            float result = amount;
            for (Passive passive : passives(player)) {
                result = passive.modifyDamage(player, target, result);
            }
            return result;
        } finally {
            dispatching = false;
        }
    }

    public static float armorPenetration(LivingEntity target, DamageSource source, float damage, float afterArmor) {
        ServerPlayer player = meleeAttacker(source);
        if (player != null && !dispatching) {
            float result = afterArmor;
            for (Passive passive : passives(player)) {
                result = passive.modifyArmorPenetration(player, target, damage, result);
            }
            return result;
        }
        // non-melee pierce, e.g. the Erasure blast
        Entity attacker = source.getEntity();
        float pierce = attacker == null ? 0.0f : ArmorPierce.fraction(attacker);
        return pierce > 0.0f ? afterArmor + (damage - afterArmor) * pierce : afterArmor;
    }

    public static void onHit(DamageSource source, LivingEntity target) {
        ServerPlayer player = meleeAttacker(source);
        if (player == null || dispatching) {
            return;
        }
        // mark every landed hit; the debounce below only gates the passives themselves
        FirstHitTracker.markHit(player, target);
        // one proc per swing: a sweep hits several entities in the same tick
        long now = player.level().getGameTime();
        Long last = LAST_ON_HIT_TICK.get(player.getUUID());
        if (last != null && last == now) {
            return;
        }
        LAST_ON_HIT_TICK.put(player.getUUID(), now);
        dispatching = true;
        try {
            for (Passive passive : passives(player)) {
                passive.onHit(player, target);
            }
        } finally {
            dispatching = false;
        }
    }

    public static void onKill(DamageSource source, LivingEntity target) {
        ServerPlayer player = meleeAttacker(source);
        if (player == null || dispatching) {
            return;
        }
        long now = player.level().getGameTime();
        Long last = LAST_ON_KILL_TICK.get(player.getUUID());
        if (last != null && last == now) {
            return;
        }
        LAST_ON_KILL_TICK.put(player.getUUID(), now);
        dispatching = true;
        try {
            for (Passive passive : passives(player)) {
                passive.onKill(player, target);
            }
        } finally {
            dispatching = false;
        }
    }

    public static boolean nullifyIncoming(LivingEntity victim, DamageSource source) {
        if (!(victim instanceof ServerPlayer player)) {
            return false;
        }
        for (Passive passive : passives(player)) {
            if (passive.nullifyIncoming(player, source)) {
                return true;
            }
        }
        return false;
    }

    public static void clearAll() {
        LAST_ON_HIT_TICK.clear();
        LAST_ON_KILL_TICK.clear();
        dispatching = false;
    }

    public static void forget(ServerPlayer player) {
        LAST_ON_HIT_TICK.remove(player.getUUID());
        LAST_ON_KILL_TICK.remove(player.getUUID());
    }

    private static List<Passive> passives(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        return stack.getItem() instanceof LegendaryWeaponItem item
                ? LegendaryPassives.of(item.definition().id())
                : List.of();
    }

    // dont touch this
    private static ServerPlayer meleeAttacker(DamageSource source) {
        if (!source.is(DamageTypes.PLAYER_ATTACK)) {
            return null;
        }
        return source.getEntity() instanceof ServerPlayer player && source.getDirectEntity() == player
                ? player
                : null;
    }
}
