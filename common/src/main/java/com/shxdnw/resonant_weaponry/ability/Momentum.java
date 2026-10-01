package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import com.shxdnw.resonant_weaponry.content.LegendaryWeaponItem;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Momentum {
    private static final Map<UUID, Integer> STACKS = new HashMap<>();
    private static final Map<UUID, Long> LAST_HIT = new HashMap<>();
    private static final Map<UUID, Long> LAST_DECAY = new HashMap<>();
    private static final Identifier MODIFIER_ID =
            Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, "momentum");

    private Momentum() {
    }

    public static void onHit(ServerPlayer player) {
        ResonantWeaponryConfig.LegendaryWeapons.YashasEdge config =
                ResonantWeaponryConfig.legendaryWeapons.yashasEdge;
        int stacks = Math.min(config.maxStacks, STACKS.getOrDefault(player.getUUID(), 0) + 1);
        STACKS.put(player.getUUID(), stacks);
        LAST_HIT.put(player.getUUID(), ServerClock.now());
        LAST_DECAY.remove(player.getUUID());
        apply(player);
        DebugLog.log("Momentum: {} stacks for {}", stacks, player.getName().getString());
    }

    public static void tick(MinecraftServer server) {
        ResonantWeaponryConfig.LegendaryWeapons.YashasEdge config =
                ResonantWeaponryConfig.legendaryWeapons.yashasEdge;
        long now = ServerClock.now();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            int stacks = STACKS.getOrDefault(player.getUUID(), 0);
            if (!holdsYasha(player)) {
                if (stacks > 0) {
                    clear(player);
                }
                continue;
            }
            if (stacks <= 0) {
                continue;
            }
            Long lastHit = LAST_HIT.get(player.getUUID());
            if (lastHit == null || now - lastHit < config.decayAfter) {
                continue;
            }
            Long lastDecay = LAST_DECAY.get(player.getUUID());
            if (lastDecay == null || now - lastDecay >= config.decayEvery) {
                STACKS.put(player.getUUID(), stacks - 1);
                LAST_DECAY.put(player.getUUID(), now);
                apply(player);
            }
        }
    }

    public static void forget(ServerPlayer player) {
        clear(player);
        LAST_HIT.remove(player.getUUID());
        LAST_DECAY.remove(player.getUUID());
    }

    public static void clearAll() {
        STACKS.clear();
        LAST_HIT.clear();
        LAST_DECAY.clear();
    }

    // transient, so we remove it ourselves on cleanup
    private static void apply(ServerPlayer player) {
        AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_SPEED);
        if (attribute == null) {
            return;
        }
        attribute.removeModifier(MODIFIER_ID);
        int stacks = STACKS.getOrDefault(player.getUUID(), 0);
        if (stacks > 0) {
            ResonantWeaponryConfig.LegendaryWeapons.YashasEdge config =
                    ResonantWeaponryConfig.legendaryWeapons.yashasEdge;
            attribute.addTransientModifier(new AttributeModifier(MODIFIER_ID, stacks * config.stackBonus,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    private static void clear(ServerPlayer player) {
        STACKS.remove(player.getUUID());
        AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_SPEED);
        if (attribute != null) {
            attribute.removeModifier(MODIFIER_ID);
        }
    }

    private static boolean holdsYasha(ServerPlayer player) {
        return player.getMainHandItem().getItem() instanceof LegendaryWeaponItem item
                && item.definition().id().equals("yashas_edge");
    }
}
