package com.resonant_weaponry.fabric.datagen;

import com.resonant_weaponry.ResonantWeaponry;
import com.resonant_weaponry.content.MaterialTier;
import com.resonant_weaponry.content.WeaponDefinition;
import com.resonant_weaponry.content.WeaponRegistry;
import com.resonant_weaponry.content.WeaponType;
import com.resonant_weaponry.registry.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public final class WeaponLanguageProvider extends FabricLanguageProvider {
    public WeaponLanguageProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registries, TranslationBuilder builder) {
        builder.add(ResonantWeaponry.WEAPONRY_TAB_KEY, "Resonant Weaponry");
        builder.add(ModTags.WEAPONS, "Weapons");

        for (WeaponDefinition definition : WeaponRegistry.STANDARD_WEAPONS) {
            builder.add(definition.nameKey(), tierName(definition.tier()) + " " + typeName(definition.type()));
        }
    }

    private static String tierName(MaterialTier tier) {
        return switch (tier) {
            case IRON -> "Iron";
            case GOLD -> "Golden";
            case DIAMOND -> "Diamond";
            case NETHERITE -> "Netherite";
        };
    }

    private static String typeName(WeaponType type) {
        String id = type.id();
        return Character.toUpperCase(id.charAt(0)) + id.substring(1);
    }
}
