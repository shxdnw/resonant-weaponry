package com.shxdnw.resonant_weaponry.ability;

import com.shxdnw.resonant_weaponry.config.ResonantWeaponryConfig;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ErasureFx {
    private static final List<Effect> EFFECTS = new ArrayList<>();

    private ErasureFx() {
    }

    public static void start(ServerLevel level, Vec3 origin) {
        EFFECTS.add(new Effect(level, origin));
    }

    public static void tick(MinecraftServer server) {
        ResonantWeaponryConfig.LegendaryWeapons.TheRealKnife config =
                ResonantWeaponryConfig.legendaryWeapons.theRealKnife;
        int lifetime = config.riseDuration + config.persistDuration;

        Iterator<Effect> iterator = EFFECTS.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            if (effect.tick >= lifetime) {
                iterator.remove();
                continue;
            }
            effect.tick(config);
        }
    }

    private static final class Effect {
        private final ServerLevel level;
        private final Vec3 origin;
        private int tick;

        private Effect(ServerLevel level, Vec3 origin) {
            this.level = level;
            this.origin = origin;
        }

        private void tick(ResonantWeaponryConfig.LegendaryWeapons.TheRealKnife config) {
            var random = level.getRandom();
            boolean rising = tick < config.riseDuration;
            double progress = rising ? (double) tick / config.riseDuration : 1.0;
            double maxHeight = progress * config.columnheight;
            double maxDepth = progress * config.columnDepth;
            double radius = config.explosionRadius;

            if (rising) {
                for (int i = 0; i < 40; i++) {
                    double angle = random.nextDouble() * Math.PI * 2.0;
                    double distance = radius * Math.sqrt(random.nextDouble()) * 0.5;
                    double x = origin.x + Math.cos(angle) * distance;
                    double z = origin.z + Math.sin(angle) * distance;
                    level.sendParticles(ParticleTypes.EXPLOSION, x, origin.y + random.nextDouble() * maxHeight, z, 1, 0.0, 0.05, 0.0, 0.02);
                    if (random.nextBoolean()) {
                        level.sendParticles(ParticleTypes.EXPLOSION, x, origin.y - random.nextDouble() * maxDepth, z, 1, 0.0, 0.05, 0.0, 0.02);
                    }
                }
            }

            for (int i = 0; i < 160; i++) {
                double angle = random.nextDouble() * Math.PI * 2.0;
                double distance = radius * Math.sqrt(random.nextDouble());
                double x = origin.x + Math.cos(angle) * distance;
                double z = origin.z + Math.sin(angle) * distance;
                float size = 2.0f + random.nextFloat() * 5.0f;
                level.sendParticles(new DustParticleOptions(0xCC1111, size),
                        x, origin.y + random.nextDouble() * maxHeight, z, 1, 0.02, 0.15, 0.02, 0.05);
                if (random.nextBoolean()) {
                    level.sendParticles(new DustParticleOptions(0xCC1111, size),
                            x, origin.y - random.nextDouble() * maxDepth, z, 1, 0.02, 0.15, 0.02, 0.05);
                }
            }

            tick++;
        }
    }
}
