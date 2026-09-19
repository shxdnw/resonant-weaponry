package com.resonant_weaponry.registry;

import com.resonant_weaponry.ResonantWeaponry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModTags {
    public static final TagKey<Item> WEAPONS = TagKey.create(
            Registries.ITEM, Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, "weapons"));

    private ModTags() {
    }
}
