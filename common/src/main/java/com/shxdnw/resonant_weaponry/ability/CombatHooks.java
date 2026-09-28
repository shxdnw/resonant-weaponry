package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.ability.passive.LegendaryPassives;
import com.shxdnw.resonant_weaponry.ability.passive.Passive;
import com.shxdnw.resonant_weaponry.content.LegendaryWeaponItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class CombatHooks {
    private static boolean dispatching;

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
        if (player == null || dispatching) {
            return afterArmor;
        }
        float result = afterArmor;
        for (Passive passive : passives(player)) {
            result = passive.modifyArmorPenetration(player, target, damage, result);
        }
        return result;
    }

    public static void onHit(DamageSource source, LivingEntity target) {
        ServerPlayer player = meleeAttacker(source);
        if (player == null || dispatching) {
            return;
        }
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
        if (dispatching || !(victim instanceof ServerPlayer player)) {
            return false;
        }
        for (Passive passive : passives(player)) {
            if (passive.nullifyIncoming(player, source)) {
                return true;
            }
        }
        return false;
    }

    private static List<Passive> passives(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        return stack.getItem() instanceof LegendaryWeaponItem item
                ? LegendaryPassives.of(item.definition().id())
                : List.of();
    }

    private static ServerPlayer meleeAttacker(DamageSource source) {
        if (!source.is(DamageTypes.PLAYER_ATTACK)) {
            return null;
        }
        return source.getEntity() instanceof ServerPlayer player && source.getDirectEntity() == player
                ? player
                : null;
    }
}
