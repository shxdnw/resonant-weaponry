package com.resonant_weaponry.registry;

import com.resonant_weaponry.ResonantWeaponry;
import com.resonant_weaponry.content.MaterialTier;
import com.resonant_weaponry.content.WeaponDefinition;
import com.resonant_weaponry.content.WeaponRegistry;
import com.resonant_weaponry.content.WeaponType;
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

    static {
        for (WeaponDefinition definition : WeaponRegistry.STANDARD_WEAPONS) {
            WEAPONS.put(definition.id(), ITEMS.register(definition.id(), () -> createWeapon(definition)));
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

        ToolMaterial material = new ToolMaterial(
                tier.incorrectBlocksForDrops(),
                tier.durabilityFor(type),
                1.0F,
                tier.attackDamageBonus(),
                tier.enchantability(),
                tier.repairItems());

        Identifier id = Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, definition.id());
        Item.Properties properties = new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .sword(material, type.baseDamage(), type.attackSpeed());

        if (tier.fireResistant()) {
            properties.fireResistant();
        }

        return new Item(properties);
    }
}
