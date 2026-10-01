package com.shxdnw.resonant_weaponry.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class ResonantWeaponryDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(WeaponItemTagProvider::new);
        pack.addProvider(WeaponLanguageProvider::new);
        pack.addProvider(WeaponRecipeProvider::new);
        pack.addProvider(WeaponAttributesProvider::new);
        pack.addProvider(WeaponAdvancementProvider::new);
    }
}
