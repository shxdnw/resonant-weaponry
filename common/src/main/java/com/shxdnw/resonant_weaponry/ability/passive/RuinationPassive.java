package com.shxdnw.resonant_weaponry.ability.passive;

import com.shxdnw.resonant_weaponry.DebugLog;
import com.shxdnw.resonant_weaponry.ability.PassiveCooldowns;
import com.shxdnw.resonant_weaponry.ability.Targeting;
import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class RuinationPassive implements Passive {
    // shared convention tag, add bosses there
    private static final TagKey<EntityType<?>> BOSSES =
            TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("c", "bosses"));

    @Override
    public void onHit(ServerPlayer player, LivingEntity target) {
        ResonantWeaponryConfig.LegendaryWeapons.Calamity config =
                ResonantWeaponryConfig.legendaryWeapons.calamity;
        if (!target.isAlive() || target.getType().builtInRegistryHolder().is(BOSSES)) {
            return;
        }
        if (target.getHealth() / target.getMaxHealth() >= config.ruinationHpThreshold) {
            return;
        }
        if (!PassiveCooldowns.ready(player, "calamity:ruination", config.ruinationCooldown)) {
            return;
        }

        ServerLevel level = player.level();
        Vec3 position = target.position();
        var random = level.getRandom();

        level.playSound(null, target.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.PLAYERS, 1.5f, 0.5f);

        target.invulnerableTime = 0;
        target.hurtServer(level, level.damageSources().indirectMagic(player, player),
                target.getHealth() + target.getAbsorptionAmount() + 1000.0f);

        AABB area = new AABB(
                position.x - config.ruinationAoERadius, position.y - 2.0, position.z - config.ruinationAoERadius,
                position.x + config.ruinationAoERadius, position.y + 4.0, position.z + config.ruinationAoERadius);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (entity == target || !Targeting.canAffect(player, entity)) {
                continue;
            }
            double dx = entity.getX() - position.x;
            double dz = entity.getZ() - position.z;
            if (Math.sqrt(dx * dx + dz * dz) > config.ruinationAoERadius) {
                continue;
            }
            entity.invulnerableTime = 0;
            entity.hurtServer(level, level.damageSources().explosion(player, player), config.ruinationAoEDamage);
            Vec3 knock = entity.position().subtract(position);
            if (knock.lengthSqr() > 1.0E-6) {
                knock = knock.normalize();
                entity.push(knock.x * 3.0, 1.0, knock.z * 3.0);
                entity.hurtMarked = true;
            }
        }

        for (int i = 0; i < 30; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = random.nextDouble() * config.ruinationAoERadius;
            level.sendParticles(new DustParticleOptions(0xFF2200, 3.0f + random.nextFloat() * 5.0f),
                    position.x + Math.cos(angle) * distance,
                    position.y + random.nextDouble() * 3.0,
                    position.z + Math.sin(angle) * distance,
                    1, 0.1, 0.1, 0.1, 0.1);
        }
        for (int i = 0; i < 20; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = random.nextDouble() * config.ruinationAoERadius * 0.5;
            level.sendParticles(ParticleTypes.EXPLOSION,
                    position.x + Math.cos(angle) * distance,
                    position.y + 0.5 + random.nextDouble() * 2.0,
                    position.z + Math.sin(angle) * distance,
                    1, 0.2, 0.2, 0.2, 0.1);
        }
        DebugLog.log("Ruination: executed {}", target.getName().getString());
    }
}
