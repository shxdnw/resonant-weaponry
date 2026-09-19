package com.resonant_weaponry.content;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public enum MaterialTier {
    IRON("iron", 250, 2.0F, 14,
            BlockTags.INCORRECT_FOR_IRON_TOOL, ItemTags.IRON_TOOL_MATERIALS, false),
    GOLD("gold", 32, 0.0F, 22,
            BlockTags.INCORRECT_FOR_GOLD_TOOL, ItemTags.GOLD_TOOL_MATERIALS, false),
    DIAMOND("diamond", 1561, 3.0F, 10,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, ItemTags.DIAMOND_TOOL_MATERIALS, false),
    NETHERITE("netherite", 2031, 4.0F, 15,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, ItemTags.NETHERITE_TOOL_MATERIALS, true);

    private final String id;
    private final int durability;
    private final float attackDamageBonus;
    private final int enchantability;
    private final TagKey<Block> incorrectBlocksForDrops;
    private final TagKey<Item> repairItems;
    private final boolean fireResistant;

    MaterialTier(String id, int durability, float attackDamageBonus, int enchantability,
                 TagKey<Block> incorrectBlocksForDrops, TagKey<Item> repairItems, boolean fireResistant) {
        this.id = id;
        this.durability = durability;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantability = enchantability;
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.repairItems = repairItems;
        this.fireResistant = fireResistant;
    }

    public String id() {
        return id;
    }

    public int durability() {
        return durability;
    }

    public float attackDamageBonus() {
        return attackDamageBonus;
    }

    public int enchantability() {
        return enchantability;
    }

    public TagKey<Block> incorrectBlocksForDrops() {
        return incorrectBlocksForDrops;
    }

    public TagKey<Item> repairItems() {
        return repairItems;
    }

    public boolean fireResistant() {
        return fireResistant;
    }

    public int durabilityFor(WeaponType type) {
        return (int) Math.floor(durability * type.durabilityMultiplier());
    }
}
