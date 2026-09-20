package com.resonant_weaponry.content;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public enum MaterialTier {
    IRON("iron", ToolMaterial.IRON, false),
    GOLD("gold", ToolMaterial.GOLD, false),
    DIAMOND("diamond", ToolMaterial.DIAMOND, false),
    NETHERITE("netherite", ToolMaterial.NETHERITE, true);

    private final String id;
    private final ToolMaterial material;
    private final boolean fireResistant;

    MaterialTier(String id, ToolMaterial material, boolean fireResistant) {
        this.id = id;
        this.material = material;
        this.fireResistant = fireResistant;
    }

    public String id() {
        return id;
    }

    public TagKey<Block> incorrectBlocksForDrops() {
        return material.incorrectBlocksForDrops();
    }

    public TagKey<Item> repairItems() {
        return material.repairItems();
    }

    public boolean fireResistant() {
        return fireResistant;
    }
}
