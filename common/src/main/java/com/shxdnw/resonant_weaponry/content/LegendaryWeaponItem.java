package com.shxdnw.resonant_weaponry.content;

import com.shxdnw.resonant_weaponry.ability.AbilityContext;
import com.shxdnw.resonant_weaponry.ability.LegendaryAbilities;
import com.shxdnw.resonant_weaponry.ability.LegendaryAbility;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class LegendaryWeaponItem extends Item {
    private final LegendaryWeapon definition;

    public LegendaryWeaponItem(LegendaryWeapon definition, Item.Properties properties) {
        super(properties);
        this.definition = definition;
    }

    public LegendaryWeapon definition() {
        return definition;
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(definition.nameColor());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        for (String line : definition.text().lore()) {
            tooltip.accept(Component.literal(line).withStyle(ChatFormatting.GOLD));
        }
        if (!definition.text().lore().isEmpty()) {
            tooltip.accept(Component.empty());
        }

        if (ClientTooltipState.shiftDown()) {
            tooltip.accept(Component.literal("\u25C7 " + definition.text().abilityName())
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            tooltip.accept(Component.empty());
            for (String line : definition.text().abilityDescription().split("\n")) {
                tooltip.accept(Component.literal(line).withStyle(ChatFormatting.DARK_PURPLE));
            }
            tooltip.accept(Component.empty());
            tooltip.accept(Component.literal("Cooldown: " + stats().cooldownTicks / 20 + "s")
                    .withStyle(ChatFormatting.BLUE));

            for (LegendaryWeapon.Passive passive : definition.text().passives()) {
                tooltip.accept(Component.empty());
                tooltip.accept(Component.literal("\u25C6 " + passive.name())
                        .withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD));
                for (String line : passive.description().split("\n")) {
                    tooltip.accept(Component.literal(line).withStyle(ChatFormatting.GRAY));
                }
            }
        } else {
            tooltip.accept(Component.literal("Hold [SHIFT] for advanced information")
                    .withStyle(ChatFormatting.GREEN));
            tooltip.accept(Component.empty());
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }
        LegendaryAbility ability = LegendaryAbilities.abilityFor(definition.id()).orElse(null);
        if (ability == null) {
            return InteractionResult.PASS;
        }
        if (definition.activation().style() == ActivationStyle.INSTANT) {
            activate(level, player, stack, hand, ability);
            return InteractionResult.SUCCESS;
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return definition.activation().style() == ActivationStyle.INSTANT ? 0 : stats().chargeTicks;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return definition.activation().style() == ActivationStyle.INSTANT ? ItemUseAnimation.NONE : ItemUseAnimation.BOW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        LegendaryAbility ability = LegendaryAbilities.abilityFor(definition.id()).orElse(null);
        if (ability != null) {
            activate(level, entity, stack, entity.getUsedItemHand(), ability);
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        return false;
    }

    private void activate(Level level, LivingEntity entity, ItemStack stack, InteractionHand hand, LegendaryAbility ability) {
        if (level.isClientSide() || !(entity instanceof ServerPlayer serverPlayer)) {
            return;
        }
        ability.activate(new AbilityContext(serverPlayer, stack, hand, definition));
        serverPlayer.getCooldowns().addCooldown(stack, stats().cooldownTicks);
    }

    private ResonantWeaponryConfig.LegendaryWeapons.Stats stats() {
        return ResonantWeaponryConfig.legendaryWeapons.stats(definition.id());
    }
}
