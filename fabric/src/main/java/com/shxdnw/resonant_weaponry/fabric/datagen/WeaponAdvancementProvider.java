package com.shxdnw.resonant_weaponry.fabric.datagen;

import com.shxdnw.resonant_weaponry.ResonantWeaponry;
import com.shxdnw.resonant_weaponry.content.LegendaryAdvancements;
import com.shxdnw.resonant_weaponry.registry.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class WeaponAdvancementProvider extends FabricAdvancementProvider {
    private static final Identifier BACKGROUND =
            Identifier.withDefaultNamespace("block/chiseled_tuff");

    public WeaponAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer) {
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
        Identifier rootId = Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, LegendaryAdvancements.ROOT.file());
        Item rootIcon = item(items, LegendaryAdvancements.ROOT.weaponId());

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(rootIcon,
                        Component.translatable(LegendaryAdvancements.titleKey(LegendaryAdvancements.ROOT)),
                        Component.translatable(LegendaryAdvancements.descriptionKey(LegendaryAdvancements.ROOT)),
                        BACKGROUND,
                        AdvancementType.TASK, true, true, false)
                .addCriterion("has_weapon", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(items, ModTags.WEAPONS)))
                .save(consumer, rootId.toString());

        for (LegendaryAdvancements.Entry entry : LegendaryAdvancements.ALL) {
            Item icon = item(items, entry.weaponId());
            Identifier id = Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, entry.file());
            Advancement.Builder.advancement()
                    .parent(root)
                    .display(icon,
                            Component.translatable(LegendaryAdvancements.titleKey(entry)),
                            Component.translatable(LegendaryAdvancements.descriptionKey(entry)),
                            null,
                            AdvancementType.CHALLENGE, true, true, false)
                    .addCriterion("has_weapon", InventoryChangeTrigger.TriggerInstance.hasItems(icon))
                    .save(consumer, id.toString());
        }
    }

    @Override
    public String getName() {
        return "Resonant Weaponry Advancements";
    }

    private static Item item(HolderGetter<Item> items, String id) {
        return items.getOrThrow(ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(ResonantWeaponry.MOD_ID, id))).value();
    }
}
