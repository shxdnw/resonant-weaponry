package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.content.LegendaryWeapon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record AbilityContext(ServerPlayer wielder, ItemStack stack, InteractionHand hand, LegendaryWeapon weapon) {
}
