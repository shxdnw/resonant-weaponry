package com.resonant_weaponry.fabric.datagen;

import com.resonant_weaponry.ResonantWeaponry;
import com.resonant_weaponry.config.ResonantWeaponryConfig;
import com.resonant_weaponry.content.MaterialTier;
import com.resonant_weaponry.content.WeaponDefinition;
import com.resonant_weaponry.content.WeaponRegistry;
import com.resonant_weaponry.content.WeaponType;
import com.resonant_weaponry.registry.ModTags;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.Map.entry;

public final class WeaponLanguageProvider extends FabricLanguageProvider {
    private static final Map<String, String> LABELS = Map.ofEntries(
            entry("general", "General"),
            entry("debugLogging", "Debug Logging"),
            entry("legendaryLootChance", "Legendary Loot Chance"),
            entry("lootTableBlacklist", "Loot Table Blacklist"),
            entry("friendlyFire", "Friendly Fire"),
            entry("material_tiers", "Material Tiers"),
            entry("iron", "Iron"),
            entry("gold", "Golden"),
            entry("diamond", "Diamond"),
            entry("netherite", "Netherite"),
            entry("durability", "Durability"),
            entry("attackDamageBonus", "Attack Damage Bonus"),
            entry("enchantability", "Enchantability"),
            entry("weapon_types", "Weapon Types"),
            entry("dagger", "Dagger"),
            entry("sickle", "Sickle"),
            entry("rapier", "Rapier"),
            entry("katana", "Katana"),
            entry("longsword", "Longsword"),
            entry("scythe", "Scythe"),
            entry("halberd", "Halberd"),
            entry("greatsword", "Greatsword"),
            entry("basedmg", "Base Damage"),
            entry("atkspeed", "Attack Speed"),
            entry("duramulti", "Durability Multiplier"),
            entry("armorPenetration", "Armour Penetration"));

    private static final Map<String, String> DESCRIPTIONS = Map.ofEntries(
            entry("debugLogging", "Emit extra diagnostic logs for combat and ability events."),
            entry("legendaryLootChance", "Chance (0-1) that a legendary weapon appears in eligible chest loot."),
            entry("lootTableBlacklist", "Skip chest loot tables whose path contains any of these substrings."),
            entry("friendlyFire", "Allow area abilities to hit same-team players. The wielder is always excluded."),
            entry("durability", "Base durability before the weapon type multiplier."),
            entry("attackDamageBonus", "Flat damage added to the weapon type's base damage."),
            entry("enchantability", "Higher means better enchantment rolls."),
            entry("basedmg", "Base damage before the material bonus."),
            entry("atkspeed", "Modifier added to the baseline of 4.0 attacks per second."),
            entry("duramulti", "Multiplier applied to the material's durability."),
            entry("armorPenetration", "Fraction of the target's armour ignored (0-1)."));

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

        config(builder, ResonantWeaponryConfig.General.class, "general");
        config(builder, ResonantWeaponryConfig.MaterialTiers.class, "material_tiers");
        config(builder, ResonantWeaponryConfig.WeaponTypes.class, "weapon_types");
    }

    private static void config(TranslationBuilder builder, Class<?> configClass, String configId) {
        addTranslation(builder, key(configId), configId);
        for (Field field : fields(configClass)) {
            String fieldKey = key(configId + "." + field.getName());
            addTranslation(builder, fieldKey, field.getName());
            if (ConfigSection.class.isAssignableFrom(field.getType())) {
                for (Field sectionField : fields(field.getType())) {
                    addTranslation(builder, fieldKey + "." + sectionField.getName(), sectionField.getName());
                }
            }
        }
    }

    private static void addTranslation(TranslationBuilder builder, String key, String name) {
        String label = LABELS.get(name);
        if (label == null) {
            throw new IllegalStateException("Missing config label for '" + name + "'");
        }
        builder.add(key, label);

        String description = DESCRIPTIONS.get(name);
        if (description != null) {
            builder.add(key + ".desc", description);
        }
    }

    private static List<Field> fields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Config.class && current != ConfigSection.class && current != Object.class) {
            Arrays.stream(current.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .forEach(fields::add);
            current = current.getSuperclass();
        }
        return fields;
    }

    private static String key(String path) {
        return ResonantWeaponry.MOD_ID + "." + path;
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
