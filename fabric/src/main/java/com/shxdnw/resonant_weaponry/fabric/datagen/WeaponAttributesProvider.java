package com.shxdnw.resonant_weaponry.fabric.datagen;

import com.google.gson.JsonObject;
import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import com.shxdnw.resonant_weaponry.content.WeaponDefinition;
import com.shxdnw.resonant_weaponry.content.WeaponRegistry;
import com.shxdnw.resonant_weaponry.content.WeaponType;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
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
        CompletableFuture<?>[] futures = WeaponRegistry.STANDARD_WEAPONS.stream()
                .map(definition -> {
                    JsonObject json = new JsonObject();
                    json.addProperty("parent", "bettercombat:" + preset(definition.type()));
                    Path path = paths.json(identifier(definition));
                    return DataProvider.saveStable(cache, json, path);
                })
                .toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getName() {
        return "Resonant Weaponry Weapon Attributes";
    }

    private static Identifier identifier(WeaponDefinition definition) {
        return Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, definition.id());
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
