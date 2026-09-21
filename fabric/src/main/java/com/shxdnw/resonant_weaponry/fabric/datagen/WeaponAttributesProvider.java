package com.shxdnw.resonant_weaponry.fabric.datagen;

import com.google.gson.JsonObject;
import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import com.shxdnw.resonant_weaponry.content.LegendaryWeapon;
import com.shxdnw.resonant_weaponry.content.LegendaryWeapons;
import com.shxdnw.resonant_weaponry.content.WeaponDefinition;
import com.shxdnw.resonant_weaponry.content.WeaponRegistry;
import com.shxdnw.resonant_weaponry.content.WeaponType;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class WeaponAttributesProvider implements DataProvider {
    private final FabricPackOutput output;

    public WeaponAttributesProvider(FabricPackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        PackOutput.PathProvider paths =
                output.createPathProvider(PackOutput.Target.DATA_PACK, "weapon_attributes");
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (WeaponDefinition definition : WeaponRegistry.STANDARD_WEAPONS) {
            futures.add(write(cache, paths, definition.id(), definition.type()));
        }
        for (LegendaryWeapon definition : LegendaryWeapons.ALL) {
            futures.add(write(cache, paths, definition.id(), definition.archetype()));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Resonant Weaponry Weapon Attributes";
    }

    private static CompletableFuture<?> write(CachedOutput cache, PackOutput.PathProvider paths, String id, WeaponType type) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", "bettercombat:" + preset(type));
        Path path = paths.json(Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, id));
        return DataProvider.saveStable(cache, json, path);
    }

    private static String preset(WeaponType type) {
        return switch (type) {
            case DAGGER -> "dagger";
            case SICKLE -> "sickle";
            case RAPIER -> "rapier";
            case KATANA -> "katana";
            case LONGSWORD -> "sword";
            case SCYTHE -> "scythe";
            case HALBERD -> "halberd";
            case GREATSWORD -> "claymore";
        };
    }
}
