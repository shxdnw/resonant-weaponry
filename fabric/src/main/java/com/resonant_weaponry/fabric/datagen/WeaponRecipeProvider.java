package com.resonant_weaponry.fabric.datagen;

import com.resonant_weaponry.ResonantWeaponry;
import com.resonant_weaponry.content.MaterialTier;
import com.resonant_weaponry.content.WeaponType;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class WeaponRecipeProvider extends FabricRecipeProvider {
    public WeaponRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new WeaponRecipes(registries, output);
    }

    @Override
    public String getName() {
        return "Resonant Weaponry Recipes";
    }

    private static final class WeaponRecipes extends RecipeProvider {
        private WeaponRecipes(HolderLookup.Provider registries, RecipeOutput output) {
            super(registries, output);
        }

        @Override
        public void buildRecipes() {
            HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);

            for (MaterialTier tier : List.of(MaterialTier.IRON, MaterialTier.GOLD, MaterialTier.DIAMOND)) {
                Item material = materialItem(tier);
                for (WeaponType type : WeaponType.values()) {
                    shaped(items, tier, type, material);
                }
            }

            for (WeaponType type : WeaponType.values()) {
                smithing(items, type);
            }
        }

        private void shaped(HolderGetter<Item> items, MaterialTier tier, WeaponType type, Item material) {
            ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(
                    items, RecipeCategory.COMBAT, item(items, tier.id() + "_" + type.id()));
            builder.define('I', Ingredient.of(material));
            builder.define('S', Ingredient.of(Items.STICK));
            for (String pattern : patterns(type)) {
                builder.pattern(pattern);
            }
            builder.unlockedBy("has_material", has(material));
            builder.save(output);
        }

        private void smithing(HolderGetter<Item> items, WeaponType type) {
            String resultId = MaterialTier.NETHERITE.id() + "_" + type.id();
            String baseId = MaterialTier.DIAMOND.id() + "_" + type.id();

            SmithingTransformRecipeBuilder.smithing(
                            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                            Ingredient.of(item(items, baseId)),
                            Ingredient.of(Items.NETHERITE_INGOT),
                            RecipeCategory.COMBAT,
                            item(items, resultId))
                    .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                    .save(output, recipeKey(resultId));
        }
    }

    private static Item materialItem(MaterialTier tier) {
        return switch (tier) {
            case IRON -> Items.IRON_INGOT;
            case GOLD -> Items.GOLD_INGOT;
            case DIAMOND -> Items.DIAMOND;
            case NETHERITE -> Items.NETHERITE_INGOT;
        };
    }

    private static List<String> patterns(WeaponType type) {
        return switch (type) {
            case DAGGER -> List.of("SI");
            case SICKLE -> List.of(" I ", "  I", " S ");
            case RAPIER -> List.of("  I", " I ", "S  ");
            case KATANA -> List.of("  I", " I ", " S ");
            case LONGSWORD -> List.of(" I ", " I ", "ISI");
            case SCYTHE -> List.of("IIS", " S ", "S  ");
            case HALBERD -> List.of("III", " SI", "S  ");
            case GREATSWORD -> List.of("  I", "II ", "SI ");
        };
    }

    private static Item item(HolderGetter<Item> items, String id) {
        return items.getOrThrow(ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, id))).value();
    }

    private static ResourceKey<Recipe<?>> recipeKey(String id) {
        return ResourceKey.create(Registries.RECIPE,
                Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, id));
    }
}
