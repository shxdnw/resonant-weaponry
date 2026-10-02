package com.shxdnw.resonant_weaponry.content;

import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import com.shxdnw.resonant_weaponry.registry.ModItems;
import dev.architectury.event.events.common.LootEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public final class LootInjection {
    private LootInjection() {
    }

    @SuppressWarnings("removal")
    public static void register() {
        LootEvent.MODIFY_LOOT_TABLE.register((key, context, builtin) -> {
            String path = key.identifier().getPath();
            if (!path.startsWith("chests/")) {
                return;
            }
            for (String blacklisted : ResonantWeaponryConfig.general.lootTableBlacklist) {
                if (!blacklisted.isEmpty() && path.contains(blacklisted)) {
                    return;
                }
            }

            float chance = ResonantWeaponryConfig.general.legendaryLootChance;
            if (chance <= 0.0f) {
                return;
            }

            LootPool.Builder pool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(chance));
            for (Item legendary : ModItems.legendaryItems()) {
                pool.add(LootItem.lootTableItem(legendary));
            }
            context.addPool(pool);
        });
    }
}
