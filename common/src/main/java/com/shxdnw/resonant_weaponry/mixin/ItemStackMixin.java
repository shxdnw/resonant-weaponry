package com.shxdnw.resonant_weaponry.mixin;

import com.shxdnw.resonant_weaponry.content.ClientTooltipState;
import com.shxdnw.resonant_weaponry.content.LegendaryWeaponItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @SuppressWarnings("rawtypes")
    @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
    private void resonantWeaponry$hideEnchantTooltip(DataComponentType type, Item.TooltipContext context,
                                                     TooltipDisplay display, Consumer<Component> consumer,
                                                     TooltipFlag flag, CallbackInfo ci) {
        if (type == DataComponents.ENCHANTMENTS && hideExtras((ItemStack) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "addAttributeTooltips", at = @At("HEAD"), cancellable = true)
    private void resonantWeaponry$hideAttributeTooltip(Consumer<Component> consumer, TooltipDisplay display,
                                                       Player player, CallbackInfo ci) {
        if (hideExtras((ItemStack) (Object) this)) {
            ci.cancel();
        }
    }

    private static boolean hideExtras(ItemStack stack) {
        return ClientTooltipState.shiftDown() && stack.getItem() instanceof LegendaryWeaponItem;
    }
}
