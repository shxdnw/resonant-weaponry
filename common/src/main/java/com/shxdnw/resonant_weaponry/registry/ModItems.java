package com.shxdnw.resonant_weaponry.registry;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import com.shxdnw.resonant_weaponry.content.LegendaryWeapon;
import com.shxdnw.resonant_weaponry.content.LegendaryWeaponItem;
import com.shxdnw.resonant_weaponry.content.LegendaryWeapons;
import com.shxdnw.resonant_weaponry.content.MaterialTier;
import com.shxdnw.resonant_weaponry.content.WeaponDefinition;
import com.shxdnw.resonant_weaponry.content.WeaponRegistry;
import com.shxdnw.resonant_weaponry.content.WeaponType;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ResonantWeaponry.MOD_ID, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(ResonantWeaponry.MOD_ID, Registries.CREATIVE_MODE_TAB);

    private static final Map<String, RegistrySupplier<Item>> WEAPONS = new LinkedHashMap<>();
    private static final Map<String, RegistrySupplier<Item>> LEGENDARIES = new LinkedHashMap<>();

    static {
        for (WeaponDefinition definition : WeaponRegistry.STANDARD_WEAPONS) {
            WEAPONS.put(definition.id(), ITEMS.register(definition.id(), () -> createWeapon(definition)));
        }
        for (LegendaryWeapon definition : LegendaryWeapons.ALL) {
            LEGENDARIES.put(definition.id(), ITEMS.register(definition.id(), () -> createLegendary(definition)));
        }
    }

    public static final RegistrySupplier<CreativeModeTab> WEAPONRY_TAB = TABS.register("weaponry_tab",
            () -> CreativeTabRegistry.create(builder -> builder
                    .title(Component.translatable(ResonantWeaponry.WEAPONRY_TAB_KEY))
                    .icon(() -> new ItemStack(WEAPONS.get(WeaponDefinition.idOf(MaterialTier.IRON, WeaponType.LONGSWORD)).get()))
                    .displayItems((parameters, output) -> {
                        for (RegistrySupplier<Item> weapon : WEAPONS.values()) {
                            output.accept(weapon.get());
                        }
                        for (RegistrySupplier<Item> weapon : LEGENDARIES.values()) {
                            output.accept(weapon.get());
                        }
                    })));

    private ModItems() {
    }

    public static void register() {
        ITEMS.register();
        TABS.register();
    }

    private static Item createWeapon(WeaponDefinition definition) {
        MaterialTier tier = definition.tier();
        WeaponType type = definition.type();
        ResonantWeaponryConfig.MaterialTiers.TierSection tierStats =
                ResonantWeaponryConfig.materialTiers.forTier(tier);
        ResonantWeaponryConfig.WeaponTypes.TypeSection typeStats =
                ResonantWeaponryConfig.weaponTypes.forType(type);

        ToolMaterial material = new ToolMaterial(
                tier.incorrectBlocksForDrops(),
                (int) Math.floor(tierStats.durability * typeStats.duramulti),
                1.0F,
                tierStats.attackDamageBonus,
                tierStats.enchantability,
                tier.repairItems());

        Identifier id = Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, definition.id());
        Item.Properties properties = new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .sword(material, typeStats.basedmg, typeStats.atkspeed);

        if (tier.fireResistant()) {
            properties.fireResistant();
        }

        return new Item(properties);
    }

    private static Item createLegendary(LegendaryWeapon definition) {
        ToolMaterial material = new ToolMaterial(
                MaterialTier.NETHERITE.incorrectBlocksForDrops(),
                ResonantWeaponryConfig.legendaryWeapons.durability,
                1.0F,
                0.0F,
                MaterialTier.NETHERITE.enchantability(),
                MaterialTier.NETHERITE.repairItems());

        Identifier id = Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, definition.id());
        return new LegendaryWeaponItem(definition, new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .sword(material, definition.attackDamage(), definition.attackSpeed())
                .fireResistant());
    }
}
