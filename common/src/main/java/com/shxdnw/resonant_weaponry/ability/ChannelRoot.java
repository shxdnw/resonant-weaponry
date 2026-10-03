package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class ChannelRoot {
    private static final Identifier MOVE_ID =
            Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, "channel_root_move");
    private static final Identifier JUMP_ID =
            Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, "channel_root_jump");
    private static final Set<UUID> ROOTED = new HashSet<>();

    private ChannelRoot() {
    }

    public static void apply(ServerPlayer player) {
        ROOTED.add(player.getUUID());
        set(player, Attributes.MOVEMENT_SPEED, MOVE_ID);
        set(player, Attributes.JUMP_STRENGTH, JUMP_ID);
    }

    public static void clear(ServerPlayer player) {
        ROOTED.remove(player.getUUID());
        remove(player, Attributes.MOVEMENT_SPEED, MOVE_ID);
        remove(player, Attributes.JUMP_STRENGTH, JUMP_ID);
    }

    public static void clearAll() {
        ROOTED.clear();
    }

    private static void set(ServerPlayer player, Holder<Attribute> attribute, Identifier id) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.addOrUpdateTransientModifier(new AttributeModifier(id, -1.0,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    private static void remove(ServerPlayer player, Holder<Attribute> attribute, Identifier id) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(id);
        }
    }
}
