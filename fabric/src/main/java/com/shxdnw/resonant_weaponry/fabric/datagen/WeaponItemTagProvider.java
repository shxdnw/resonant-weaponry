package com.shxdnw.resonant_weaponry.fabric.datagen;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import com.shxdnw.resonant_weaponry.content.LegendaryWeapon;
import com.shxdnw.resonant_weaponry.content.LegendaryWeapons;
import com.shxdnw.resonant_weaponry.content.WeaponDefinition;
import com.shxdnw.resonant_weaponry.content.WeaponRegistry;
import com.shxdnw.resonant_weaponry.registry.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public final class WeaponItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public WeaponItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        var weapons = builder(ModTags.WEAPONS);
        for (WeaponDefinition definition : WeaponRegistry.STANDARD_WEAPONS) {
            weapons.add(itemKey(definition.id()));
        }
        for (LegendaryWeapon definition : LegendaryWeapons.ALL) {
            weapons.add(itemKey(definition.id()));
        }

        builder(ItemTags.MELEE_WEAPON_ENCHANTABLE).addTag(ModTags.WEAPONS);
        builder(ItemTags.DURABILITY_ENCHANTABLE).addTag(ModTags.WEAPONS);
        builder(ItemTags.SWEEPING_ENCHANTABLE).addTag(ModTags.WEAPONS);
    }

    private static ResourceKey<Item> itemKey(String id) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, id));
    }
}
