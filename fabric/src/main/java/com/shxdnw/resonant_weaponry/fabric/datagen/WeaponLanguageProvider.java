package com.shxdnw.resonant_weaponry.fabric.datagen;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import com.shxdnw.resonant_weaponry.content.LegendaryWeapon;
import com.shxdnw.resonant_weaponry.content.LegendaryWeapons;
import com.shxdnw.resonant_weaponry.content.MaterialTier;
import com.shxdnw.resonant_weaponry.content.WeaponDefinition;
import com.shxdnw.resonant_weaponry.content.WeaponRegistry;
import com.shxdnw.resonant_weaponry.content.WeaponType;
import com.shxdnw.resonant_weaponry.registry.ModTags;
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
            entry("attackDamage", "Attack Damage"),
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
            entry("armorPenetration", "Armour Penetration"),
            entry("legendary_weapons", "Legendary Weapons"),
            entry("theRealKnife", "The Real Knife"),
            entry("armorPen", "Armour Penetration"),
            entry("firstHitWindow", "First Hit Window"),
            entry("relentlessBelow", "Relentless Health Threshold"),
            entry("explosionRadius", "Explosion Radius"),
            entry("explosionDamage", "Explosion Damage"),
            entry("teleportDistance", "Teleport Distance"),
            entry("downReach", "Downward Reach"),
            entry("upReach", "Upward Reach"),
            entry("riseDuration", "Rise Duration"),
            entry("persistDuration", "Persist Duration"),
            entry("columnheight", "Column Height"),
            entry("columnDepth", "Column Depth"),
            entry("gildedArbiter", "Gilded Arbiter"),
            entry("defenseSlow", "Defence Slow Duration"),
            entry("defenseResistTime", "Resistance Duration"),
            entry("defenseResistLevel", "Resistance Level"),
            entry("showstopperMult", "Showstopper Multiplier"),
            entry("witherTime", "Wither Duration"),
            entry("witherLevel", "Wither Level"),
            entry("counterSlowTime", "Counterweight Slowness Duration"),
            entry("galeCutter", "Gale Cutter"),
            entry("cycloneRadius", "Cyclone Radius"),
            entry("cycloneDamage", "Cyclone Damage"),
            entry("tailwindSpeedDuration", "Tailwind Speed Duration"),
            entry("tailwindCooldown", "Tailwind Cooldown"),
            entry("aftercutMaxStacks", "Aftercut Hit Count"),
            entry("aftercutDelay", "Aftercut Delay"),
            entry("aftercutDamage", "Aftercut Damage"),
            entry("bloodScourge", "Blood Scourge"),
            entry("slashReach", "Slash Reach"),
            entry("slashDamage", "Slash Damage"),
            entry("witherDuration", "Wither Duration"),
            entry("blindnessDuration", "Blindness Duration"),
            entry("slowDuration", "Slowness Duration"),
            entry("sanguineHealPct", "Sanguine Heal Fraction"),
            entry("sanguineCooldown", "Sanguine Cooldown"),
            entry("hemoAoEDamage", "Hemorrhagic Shock Damage"),
            entry("hemoAoERadius", "Hemorrhagic Shock Radius"),
            entry("hemoCooldown", "Hemorrhagic Shock Cooldown"),
            entry("voidfang", "Voidfang"),
            entry("trailDamage", "Trail Damage"),
            entry("trailRadius", "Trail Radius"),
            entry("trailDelay", "Trail Delay"),
            entry("maxStacks", "Max Stacks"),
            entry("trueDamage", "True Damage"),
            entry("nullifyChance", "Nullify Chance"),
            entry("yashasEdge", "Yasha's Edge"),
            entry("dashTicks", "Dash Duration"),
            entry("dashDamage", "Dash Damage"),
            entry("aftercutRehit", "Aftercut Can Re-hit"),
            entry("decayAfter", "Momentum Decay Delay"),
            entry("decayEvery", "Momentum Decay Interval"),
            entry("stackBonus", "Momentum Per Stack"),
            entry("anchorCooldown", "Anchor Cooldown"),
            entry("anchorDuration", "Anchor Duration"),
            entry("anchorLevel", "Anchor Level"),
            entry("calamity", "Calamity"),
            entry("leapVelocityUp", "Leap Upward Velocity"),
            entry("leapVelocityForward", "Leap Forward Velocity"),
            entry("impactRadius", "Impact Radius"),
            entry("impactDamage", "Impact Damage"),
            entry("ruinationHpThreshold", "Ruination HP Threshold"),
            entry("ruinationAoEDamage", "Ruination Damage"),
            entry("ruinationAoERadius", "Ruination Radius"),
            entry("ruinationCooldown", "Ruination Cooldown"));

    private static final Map<String, String> DESCRIPTIONS = Map.ofEntries(
            entry("debugLogging", "Emit extra diagnostic logs for combat and ability events."),
            entry("legendaryLootChance", "Chance (0-1) that a legendary weapon appears in eligible chest loot."),
            entry("lootTableBlacklist", "Skip chest loot tables whose path contains any of these substrings."),
            entry("friendlyFire", "Allow area abilities to hit same-team players. The wielder is always excluded."),
            entry("durability", "Base durability before the weapon type multiplier."),
            entry("attackDamage", "Final item attack damage for this weapon."),
            entry("legendary_weapons.durability", "Max durability for this weapon."),
            entry("attackDamageBonus", "Flat damage added to the weapon type's base damage."),
            entry("enchantability", "Higher means better enchantment rolls."),
            entry("basedmg", "Base damage before the material bonus."),
            entry("atkspeed", "Modifier added to the baseline of 4.0 attacks per second."),
            entry("duramulti", "Multiplier applied to the material's durability."),
            entry("armorPenetration", "Fraction of the target's armour ignored (0-1)."),
            entry("armorPen", "Fraction of the target's armour ignored while below the health threshold (0-1)."),
            entry("firstHitWindow", "Ticks without a hit before the first-hit bonus memory expires."),
            entry("relentlessBelow", "Health fraction below which Relentless activates (0-1)."),
            entry("explosionRadius", "Horizontal radius of the Erasure blast."),
            entry("explosionDamage", "Damage at the centre of the Erasure blast."),
            entry("teleportDistance", "Distance the wielder is teleported to safety."),
            entry("downReach", "How far below the origin the Erasure blast reaches."),
            entry("upReach", "How far above the origin the Erasure blast reaches."),
            entry("riseDuration", "Ticks the Erasure column takes to rise."),
            entry("persistDuration", "Ticks the Erasure column lingers."),
            entry("columnheight", "Maximum height of the Erasure column."),
            entry("columnDepth", "Maximum depth of the Erasure column."),
            entry("defenseSlow", "Ticks of Slowness I applied to the wielder."),
            entry("defenseResistTime", "Ticks of Resistance applied to the wielder."),
            entry("defenseResistLevel", "Resistance amplifier (1 = level II)."),
            entry("showstopperMult", "Damage multiplier against sprinting or airborne targets."),
            entry("witherTime", "Wither duration applied to hostile mobs on hit."),
            entry("witherLevel", "Wither amplifier (0 = level I)."),
            entry("counterSlowTime", "Slowness duration applied when hitting while moving backward."),
            entry("cycloneRadius", "Horizontal radius of the Cyclone blast."),
            entry("cycloneDamage", "Magic damage dealt by Cyclone."),
            entry("tailwindSpeedDuration", "Ticks of Speed II granted on hit."),
            entry("tailwindCooldown", "Ticks between Tailwind procs."),
            entry("aftercutMaxStacks", "Hits needed to trigger an aftercut."),
            entry("aftercutDelay", "Ticks before the aftercut lands."),
            entry("aftercutDamage", "Magic damage dealt by the aftercut."),
            entry("slashReach", "Forward reach of the Eviscerate slash."),
            entry("slashDamage", "Damage dealt by the Eviscerate slash."),
            entry("witherDuration", "Wither duration applied by Eviscerate."),
            entry("blindnessDuration", "Blindness duration applied by Eviscerate."),
            entry("slowDuration", "Slowness duration applied by Eviscerate."),
            entry("sanguineHealPct", "Fraction of max health healed on a hostile kill (0-1)."),
            entry("sanguineCooldown", "Ticks between Sanguine heals."),
            entry("hemoAoEDamage", "Magic damage dealt by Hemorrhagic Shock."),
            entry("hemoAoERadius", "Radius of the Hemorrhagic Shock burst."),
            entry("hemoCooldown", "Ticks between Hemorrhagic Shock procs."),
            entry("trailDamage", "Damage per trail segment at the centre."),
            entry("trailRadius", "Radius of each trail segment."),
            entry("trailDelay", "Ticks before the trail erupts."),
            entry("maxStacks", "Voidscar stacks needed for the bonus hit."),
            entry("trueDamage", "Bonus damage at full Voidscar stacks (bypasses armour)."),
            entry("nullifyChance", "Chance to nullify an incoming hit while you have Voidscar stacks (0-1)."),
            entry("dashTicks", "Ticks the dash lasts."),
            entry("dashDamage", "Magic damage dealt per dash hit."),
            entry("aftercutRehit", "Whether the aftercut can hit entities already struck by the dash."),
            entry("decayAfter", "Ticks without a hit before Momentum starts decaying."),
            entry("decayEvery", "Ticks per stack lost while decaying."),
            entry("stackBonus", "Attack-speed fraction granted per Momentum stack."),
            entry("anchorCooldown", "Ticks between Anchor procs per weapon."),
            entry("anchorDuration", "Slowness duration applied by Anchor."),
            entry("anchorLevel", "Slowness amplifier (0 = level I)."),
            entry("legendary_weapons.yashasEdge.maxStacks", "Momentum stacks needed to reach the attack-speed cap."),
            entry("leapVelocityUp", "Upward velocity applied at the start of the Cataclysm leap."),
            entry("leapVelocityForward", "Forward velocity applied at the start of the Cataclysm leap."),
            entry("impactRadius", "Horizontal radius of the Cataclysm slam."),
            entry("impactDamage", "Slam damage at the centre, falling off with distance."),
            entry("ruinationHpThreshold", "Health fraction below which Ruination can execute (0-1)."),
            entry("ruinationAoEDamage", "Explosion damage dealt by the Ruination blast."),
            entry("ruinationAoERadius", "Radius of the Ruination blast."),
            entry("ruinationCooldown", "Ticks between Ruination executions per weapon."));

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
        for (LegendaryWeapon definition : LegendaryWeapons.ALL) {
            builder.add(definition.nameKey(), definition.displayName());
        }

        config(builder, ResonantWeaponryConfig.General.class, "general");
        config(builder, ResonantWeaponryConfig.MaterialTiers.class, "material_tiers");
        config(builder, ResonantWeaponryConfig.WeaponTypes.class, "weapon_types");
        config(builder, ResonantWeaponryConfig.LegendaryWeapons.class, "legendary_weapons");
    }

    private static void config(TranslationBuilder builder, Class<?> configClass, String configId) {
        addTranslation(builder, key(configId), configId, configId, configId);
        for (Field field : fields(configClass)) {
            String fieldPath = configId + "." + field.getName();
            String fieldKey = key(fieldPath);
            addTranslation(builder, fieldKey, fieldPath, configId, field.getName());
            if (ConfigSection.class.isAssignableFrom(field.getType())) {
                for (Field sectionField : fields(field.getType())) {
                    addTranslation(builder, fieldKey + "." + sectionField.getName(),
                            fieldPath + "." + sectionField.getName(), configId, sectionField.getName());
                }
            }
        }
    }

    private static void addTranslation(TranslationBuilder builder, String key, String path, String configId, String name) {
        String label = firstNonNull(LABELS.get(path), LABELS.get(configId + "." + name), LABELS.get(name));
        if (label == null) {
            throw new IllegalStateException("Missing config label for '" + path + "'");
        }
        builder.add(key, label);

        String description = firstNonNull(DESCRIPTIONS.get(path), DESCRIPTIONS.get(configId + "." + name), DESCRIPTIONS.get(name));
        if (description != null) {
            builder.add(key + ".desc", description);
        }
    }

    private static String firstNonNull(String... values) {
        for (String value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
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
